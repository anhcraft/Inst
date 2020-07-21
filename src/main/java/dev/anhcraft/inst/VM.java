package dev.anhcraft.inst;

import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.exceptions.FunctionRegisterFailed;
import dev.anhcraft.inst.exceptions.InstructionCompileFailed;
import dev.anhcraft.inst.exceptions.RuntimeError;
import dev.anhcraft.inst.lang.*;
import dev.anhcraft.inst.lang.defaults.*;
import dev.anhcraft.inst.utils.MathUtil;
import dev.anhcraft.inst.values.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public class VM {
    private static final Predicate<Character> NAME_CHECK = c -> Character.isLetterOrDigit(c) || c == '_' || c == '-';
    private final Map<String, Val<?>> variables = new HashMap<>();
    private final Map<String, Collection<Linker>> functions = new HashMap<>();

    public VM() {
        try {
            registerFunctions(InstFunctions.class);
            registerFunctions(VarFunctions.class);
            registerFunctions(SystemFunctions.class);
            registerFunctions(StringFunctions.class);
            registerFunctions(MathFunctions.class);
        } catch (FunctionRegisterFailed functionRegisterFailed) {
            functionRegisterFailed.printStackTrace();
        }
    }

    @NotNull
    public Val<?> compileVariable(@NotNull String str, boolean stringify){
        if(stringify){
            return new StringVal(str);
        }
        String word = str.trim();
        if(MathUtil.isNumber(word)) {
            if (word.indexOf('.') >= 0) {
                return new DoubleVal(Double.parseDouble(word));
            } else {
                boolean b = (word.charAt(0) == '-' || word.charAt(0) == '+') ? word.length() <= 10 : word.length() <= 9;
                return b ? new IntVal(Integer.parseInt(word)) : new LongVal(Long.parseLong(word));
            }
        } else if(str.equalsIgnoreCase("true")) {
            return new BoolVal(true);
        } else if(str.equalsIgnoreCase("false")) {
            return new BoolVal(false);
        } else {
            if(str.length() >= 2 && str.startsWith("$")) {
                String target = str.substring(1);
                if(target.chars().allMatch(value -> NAME_CHECK.test((char) value))) {
                    return new Reference(target) {
                        @NotNull
                        public Val<?> getVar() {
                            Val<?> v = getVariable(getTarget());
                            if (v == null) {
                                throw new RuntimeError("Unknown variable: " + getTarget());
                            }
                            return v;
                        }
                    };
                }
            }
            return new StringVal(str);
        }
    }

    @Nullable
    public Val<?> getVariable(@NotNull String name) {
        return variables.get(name);
    }

    @Nullable
    public Val<?> setVariable(@NotNull String name, @Nullable Val<?> val) {
        if(name.chars().allMatch(value -> NAME_CHECK.test((char) value))) {
            return variables.put(name, val);
        } else {
            throw new UnsupportedOperationException("Invalid variable name");
        }
    }

    @NotNull
    public Map<String, Val<?>> getVariables() {
        return Collections.unmodifiableMap(variables);
    }

    @NotNull
    public Map<String, Collection<Linker>> getFunctions() {
        return Collections.unmodifiableMap(functions);
    }

    public synchronized void registerFunctions(@NotNull Class<?> namespace) throws FunctionRegisterFailed {
        registerFunctions(namespace, null);
    }

    public synchronized <T> void registerFunctions(@NotNull Class<? extends T> namespace, @Nullable T ins) throws FunctionRegisterFailed {
        Namespace ns = namespace.getDeclaredAnnotation(Namespace.class);
        if (ns == null) {
            throw new FunctionRegisterFailed("Namespace was not declared");
        }
        try {
            if(ins == null) {
                ins = namespace.newInstance();
            }
            for (Method m : namespace.getMethods()) {
                m.setAccessible(true);
                Function f = m.getAnnotation(Function.class);
                if (f == null) continue;
                boolean varArgs = m.isVarArgs();
                List<ParamType> paramTypes = new ArrayList<>();
                Class<?>[] params = m.getParameterTypes();
                for (int i = 0, len = params.length; i < len; i++) {
                    Class<?> param = params[i];
                    if (varArgs && param.isArray() && i + 1 == len) {
                        param = param.getComponentType();
                    }
                    if (Reference.class.isAssignableFrom(param)) {
                        paramTypes.add(ParamType.REFERENCE);
                    } else if (Val.class.isAssignableFrom(param)) {
                        DataType dataType = DataType.findByValueClass(param);
                        paramTypes.add(dataType == null ? ParamType.VAL : ParamType.findByDataType(dataType));
                    } else if (VM.class.isAssignableFrom(param)) {
                        paramTypes.add(ParamType.VM);
                    } else if (Session.class.isAssignableFrom(param)) {
                        paramTypes.add(ParamType.SESSION);
                    } else {
                        throw new FunctionRegisterFailed("Unknown param type: " + param.getName());
                    }
                }
                if(m.getParameterCount() != paramTypes.size()){
                    throw new FunctionRegisterFailed(String.format("Mismatch parameter count: %d != %d", m.getParameterCount(), paramTypes.size()));
                }
                T finalIns = ins;
                functions.computeIfAbsent(ns.value()+f.value(), k -> new ArrayList<>()).add(new Linker(paramTypes, varArgs) {
                    @Override
                    public void call(@NotNull Session session, @NotNull List<Val<?>> args) {
                        int paramSize = getParamTypes().size();
                        Object[] values = new Object[m.getParameterCount()];
                        int j = 0;
                        for(int i = 0; i < values.length; i++){
                            ParamType pt = getParamTypes().get(i);
                            if(pt.getDataType() != null) {
                                if(args.size() == j) break;
                                Val<?> v = args.get(j++);
                                values[i] = v instanceof Reference ? ((Reference) v).getVar() : v;
                            } else if(pt == ParamType.VAL) {
                                if(args.size() == j) break;
                                values[i] = args.get(j++);
                            } else if(pt == ParamType.REFERENCE) {
                                if(args.size() == j) break;
                                Val<?> v = args.get(j++);
                                if(v instanceof Reference) {
                                    values[i] = v;
                                } else {
                                    throw new RuntimeError(String.format("Variable reference required (%s:%s)", ns.value(), f.value()));
                                }
                            } else if(pt == ParamType.VM) {
                                values[i] = session.getVM();
                            } else if(pt == ParamType.SESSION) {
                                values[i] = session;
                            }
                            if(i + 1 == paramSize && isVarArgs()) {
                                int len = args.size() - j + 1;
                                Object arr = Array.newInstance(pt.getParamClass(), len);
                                for (int k = 0; k < len; k++) {
                                    Array.set(arr, k, args.get(j++ - 1));
                                }
                                values[i] = arr;
                                break;
                            }
                        }
                        try {
                            m.invoke(finalIns, values);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                            throw new RuntimeError(String.format("Function link broken (%s:%s)", ns.value(), f.value()));
                        }
                    }
                });
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FunctionRegisterFailed("Could not create instance");
        }
    }

    @NotNull
    public Instruction compileInstruction(@NotNull String str) throws InstructionCompileFailed {
        String namespace = null;
        String function = null;
        List<Val<?>> args = new ArrayList<>();
        Val<?> cdnA = null;
        Val<?> cdnB = null;
        RelationalOp sign = null;

        StringBuilder temp = new StringBuilder();
        boolean inStr = false;
        boolean escaped = false;
        boolean cdnAStringify = false;
        // 0: namespace
        // 1: function
        // 2: mid 1 (between function name and opening argument-part)
        // 3: args
        // 4: mid 2 (between arguments)
        // 5: mid 3 (between argument-part and condition-1)
        // 6: condition - var 1 + sign A
        // 7: condition - sign B
        // 8: condition - var 2
        int mode = 0;
        for (char c : str.toCharArray()) {
            if(mode == 0) {
                if(c == ':') {
                    mode = 1;
                    namespace = temp.toString();
                    temp = new StringBuilder();
                    continue;
                } else if(!NAME_CHECK.test(c)) {
                    throw new InstructionCompileFailed("Invalid character in namespace: " + c);
                }
                temp.append(c);
            } else if(mode == 1) {
                if(c == ' ' || c == '(') {
                    mode = c == ' ' ? 2 : 3;
                    function = temp.toString();
                    temp = new StringBuilder();
                    continue;
                } else if(!NAME_CHECK.test(c)) {
                    throw new InstructionCompileFailed("Invalid character in function name: " + c);
                }
                temp.append(c);
            } else if(mode == 2) {
                if(c == '(') {
                    mode = 3;
                } else {
                    if(temp.length() == 0) {
                        throw new InstructionCompileFailed("Function name not found");
                    } else if(c != ' ') {
                        throw new InstructionCompileFailed("Invalid character between function name and opening argument-part: " + c);
                    }
                }
            } else if(mode == 3) {
                if(c == '"') {
                    if(!inStr) {
                        inStr = true;
                        continue;
                    } else if(!escaped) {
                        mode = 4;
                        args.add(compileVariable(temp.toString(), true));
                        temp = new StringBuilder();
                        inStr = false;
                        continue;
                    } else {
                        escaped = false;
                    }
                } else if(c == ' ') {
                    if(!inStr) {
                        // unless string is in "...", spaces are ignored if
                        // the string is currently empty
                        // for e.g: /  a/ ==> /a/
                        if(temp.length() == 0) {
                            continue;
                        }
                        // mode = ... no need to set mode here, we are still reading arguments
                        args.add(compileVariable(temp.toString().trim(), false));
                        temp = new StringBuilder();
                        continue;
                    }
                } else if(c == '\\') {
                    if(escaped) {
                        escaped = false;
                    } else {
                        escaped = true;
                        continue;
                    }
                } else if(c == ')') {
                    if(!inStr) {
                        mode = 5;
                        // empty argument is not allowed unless in "..."
                        if(temp.length() > 0) args.add(compileVariable(temp.toString().trim(), false));
                        temp = new StringBuilder();
                        continue;
                    } else if(!escaped) {
                        throw new InstructionCompileFailed("Argument-part closed while string is opening");
                    } else {
                        escaped = false;
                    }
                }
                if(escaped) {
                    temp.append('\\');
                    escaped = false;
                }
                temp.append(c);
            } else if(mode == 4) {
                if(c == ')') {
                    mode = 5;
                } else if(c == ' ') {
                    mode = 3;
                } else {
                    throw new InstructionCompileFailed("Invalid character between arguments: " + c);
                }
            } else if(mode == 5) {
                if(c == '?') {
                    mode = 6;
                    escaped = false;
                    inStr = false;
                } else if (c != ' ' && c != '\n'){
                    throw new InstructionCompileFailed("Invalid character between argument-part and condition: " + c);
                }
            } else if(mode == 6) {
                if(c == '<') {
                    if(temp.length() == 0) {
                        throw new InstructionCompileFailed("Conditional left-part not found");
                    }
                    mode = 7;
                    sign = RelationalOp.LESS;
                    cdnA = compileVariable(temp.toString(), cdnAStringify);
                    temp = new StringBuilder();
                    continue;
                } else if(c == '>') {
                    if(temp.length() == 0) {
                        throw new InstructionCompileFailed("Conditional left-part not found");
                    }
                    mode = 7;
                    sign = RelationalOp.GREATER;
                    cdnA = compileVariable(temp.toString(), cdnAStringify);
                    temp = new StringBuilder();
                    continue;
                } else if(c == '=') {
                    if(temp.length() == 0) {
                        throw new InstructionCompileFailed("Conditional left-part not found");
                    }
                    mode = 7;
                    sign = RelationalOp.EQUAL;
                    cdnA = compileVariable(temp.toString(), cdnAStringify);
                    temp = new StringBuilder();
                    continue;
                } else if(c == '!') {
                    if(temp.length() == 0) {
                        throw new InstructionCompileFailed("Conditional left-part not found");
                    }
                    mode = 7;
                    sign = RelationalOp.NOT_EQUAL;
                    cdnA = compileVariable(temp.toString(), cdnAStringify);
                    temp = new StringBuilder();
                    continue;
                } else if(cdnAStringify && c != ' ') {
                    throw new InstructionCompileFailed("Invalid character after left-part string: " + c);
                }
                if(c == ' ') {
                    continue;
                } else if(c == '"') {
                    if(!inStr) {
                        inStr = true;
                        continue;
                    } else if(!escaped) {
                        inStr = false;
                        cdnAStringify = true;
                        continue;
                    } else {
                        escaped = false;
                    }
                } else if(c == '\\') {
                    if(escaped) {
                        escaped = false;
                    } else {
                        escaped = true;
                        continue;
                    }
                }
                if(escaped) {
                    temp.append('\\');
                    escaped = false;
                }
                temp.append(c);
            } else if(mode == 7) {
                mode = 8;
                escaped = false;
                inStr = false;
                if(c == '=') {
                    if(sign == RelationalOp.LESS) {
                        sign = RelationalOp.LESS_OR_EQUAL;
                    } else if(sign == RelationalOp.GREATER) {
                        sign = RelationalOp.GREATER_OR_EQUAL;
                    }
                } else if(sign == RelationalOp.EQUAL) {
                    throw new InstructionCompileFailed("Invalid relational operator! Must be == instead of =");
                } else if(sign == RelationalOp.NOT_EQUAL) {
                    throw new InstructionCompileFailed("Invalid relational operator! Must be != instead of !");
                } else {
                    temp.append(c);
                }
            } else {
                if(cdnB != null && c != ' ' && c != '\n') {
                    throw new InstructionCompileFailed("Invalid character after condition: " + c);
                }
                if(c == '"') {
                    if(!inStr) {
                        inStr = true;
                        continue;
                    } else if(!escaped) {
                        inStr = false;
                        cdnB = compileVariable(temp.toString(), true);
                        continue;
                    } else {
                        escaped = false;
                    }
                } else if(c == ' ') {
                    if(!inStr) {
                        // unless string is in "...", spaces are ignored if
                        // the string is currently empty
                        // for e.g: "  a" ==> "a"
                        if(temp.length() == 0) {
                            continue;
                        }
                        cdnB = compileVariable(temp.toString(), false);
                        continue;
                    }
                } else if(c == '\\') {
                    if(escaped) {
                        escaped = false;
                    } else {
                        escaped = true;
                        continue;
                    }
                }
                if(escaped) {
                    temp.append('\\');
                    escaped = false;
                }
                temp.append(c);
            }
        }
        if(namespace == null) {
            throw new InstructionCompileFailed("Missing namespace");
        }
        if(mode == 1 || function == null) {
            throw new InstructionCompileFailed("Missing function name");
        }
        if(mode == 3 || mode == 4) {
            throw new InstructionCompileFailed("Argument-part not ended");
        }
        if(mode == 6) {
            throw new InstructionCompileFailed("Condition not finished (left -> ???)");
        }
        if(mode == 7) {
            throw new InstructionCompileFailed("Condition not finished (left -> sign -> ???)");
        }
        if(mode == 8 && cdnB == null) {
            if(inStr) {
                throw new InstructionCompileFailed("String in right-part condition not ended");
            } else if(temp.length() > 0) {
                cdnB = compileVariable(temp.toString().trim(), false);
            } else {
                throw new InstructionCompileFailed("Condition right-part not found");
            }
        }

        Condition condition = null;
        if(cdnA != null && cdnB != null && sign != null) {
            condition = new Condition(cdnA, cdnB, sign);
        } else if (!(cdnA == null && cdnB == null && sign == null)) {
            throw new InstructionCompileFailed("Invalid condition");
        }

        Collection<Linker> vs = functions.get(namespace+function);
        if(vs == null) {
            throw new InstructionCompileFailed(String.format("Function not found (%s:%s)", namespace, function));
        } else {
            Optional<Linker> linker = vs.stream().filter(v -> v.canLink(args)).findFirst();
            if(linker.isPresent()) {
                Instruction inst = new Instruction(namespace, function, args, linker.get());
                inst.setCondition(condition);
                return inst;
            } else {
                throw new InstructionCompileFailed(String.format("No function linked (%s:%s)", namespace, function));
            }
        }
    }

    @NotNull
    public Session newSession(@NotNull Instruction... instructions) {
        return new Session(this, instructions);
    }
}
