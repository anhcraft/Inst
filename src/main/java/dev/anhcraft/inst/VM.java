package dev.anhcraft.inst;

import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.exceptions.FunctionRegisterFailed;
import dev.anhcraft.inst.exceptions.InstructionCompileFailed;
import dev.anhcraft.inst.exceptions.RuntimeError;
import dev.anhcraft.inst.lang.*;
import dev.anhcraft.inst.lang.defaults.CacheFunctions;
import dev.anhcraft.inst.lang.defaults.StringFunctions;
import dev.anhcraft.inst.lang.defaults.SystemFunctions;
import dev.anhcraft.inst.utils.MathUtil;
import dev.anhcraft.inst.values.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public class VM {
    private static final Predicate<Character> NAME_CHECK = c -> Character.isLetterOrDigit(c) || c == '_' || c == '-';
    private final Map<String, Val<?>> variables = new HashMap<>();
    private final Map<Integer, FunctionLinker> functions = new HashMap<>();

    public VM() {
        try {
            registerFunctions(CacheFunctions.class);
            registerFunctions(SystemFunctions.class);
            registerFunctions(StringFunctions.class);
        } catch (FunctionRegisterFailed functionRegisterFailed) {
            functionRegisterFailed.printStackTrace();
        }
    }

    private int hashFunc(String namespace, String func, DataType... params) {
        Object[] objects = new Object[params.length + 2];
        objects[0] = namespace.toLowerCase();
        objects[1] = func.toLowerCase();
        System.arraycopy(params, 0, objects, 2, params.length);
        return Arrays.hashCode(objects);
    }

    @NotNull
    public Val<?> compileVariable(@NotNull String str, boolean stringify){
        if(stringify){
            return (StringVal) () -> str;
        }
        String word = str.trim();
        if(MathUtil.isNumber(word)) {
            if (word.indexOf('.') >= 0) {
                return new DoubleVal() {
                    private final double n = Double.parseDouble(word);

                    @NotNull
                    @Override
                    public Double get() {
                        return n;
                    }
                };
            } else {
                boolean b = (word.charAt(0) == '-' || word.charAt(0) == '+') ? word.length() <= 10 : word.length() <= 9;
                return b ? new IntVal() {
                    private final int n = Integer.parseInt(word);

                    @NotNull
                    @Override
                    public Integer get() {
                        return n;
                    }
                } : new LongVal() {
                    private final long n = Long.parseLong(word);

                    @NotNull
                    @Override
                    public Long get() {
                        return n;
                    }
                };
            }
        } else if(str.equalsIgnoreCase("true")) {
            return (BoolVal) () -> true;
        } else if(str.equalsIgnoreCase("false")) {
            return (BoolVal) () -> false;
        } else {
            if(str.length() >= 2 && str.startsWith("$")) {
                return new Reference(str.substring(1)) {
                    @NotNull
                    public Val<?> getVar() {
                        Val<?> v = getVariable(getTarget());
                        if(v == null){
                            throw new RuntimeError("Unknown variable: " + getTarget());
                        }
                        return v;
                    }

                    @Override
                    public @NotNull DataType type() {
                        return getVar().type();
                    }

                    @NotNull
                    @Override
                    public Object get() {
                        return getVar().get();
                    }
                };
            } else {
                return (StringVal) () -> str;
            }
        }
    }

    @Nullable
    public Val<?> getVariable(@NotNull String name) {
        return variables.get(name);
    }

    @Nullable
    public Val<?> setVariable(@NotNull String name, @Nullable Val<?> val) {
        return variables.put(name, val);
    }

    @Nullable
    public FunctionLinker getFunction(@NotNull String namespace, @NotNull String func, @NotNull DataType... params) {
        return functions.get(hashFunc(namespace, func, params));
    }

    public synchronized int registerFunctions(@NotNull Class<?> namespace) throws FunctionRegisterFailed {
        return registerFunctions(namespace, null);
    }

    public synchronized <T> int registerFunctions(@NotNull Class<T> namespace, @Nullable T ins) throws FunctionRegisterFailed {
        Namespace ns = namespace.getDeclaredAnnotation(Namespace.class);
        if (ns == null) {
            throw new FunctionRegisterFailed("Namespace was not declared");
        }
        int count = 0;
        try {
            if(ins == null) {
                ins = namespace.newInstance();
            }
            for (Method m : namespace.getMethods()) {
                m.setAccessible(true);
                Function f = m.getAnnotation(Function.class);
                if (f != null) {
                    Class<?>[] params = m.getParameterTypes();
                    DataType first = null;
                    int hash;
                    if (params.length > 0) {
                        List<DataType> pts = new ArrayList<>();
                        for (Class<?> clazz : params) {
                            DataType pt = DataType.fromClass(clazz);
                            if(pt == null) {
                                throw new FunctionRegisterFailed(String.format("Unknown parameter type %s (%s:%s)", clazz.getName(), ns.value(), f.value()));
                            }
                            if(pt == DataType.SESSION || pt == DataType.VM) {
                                if (pts.isEmpty()) {
                                    first = pt;
                                } else {
                                    throw new FunctionRegisterFailed(String.format("Parameter type %s must be put at head (%s:%s)", clazz.getName(), ns.value(), f.value()));
                                }
                                continue;
                            }
                            pts.add(pt);
                        }
                        hash = hashFunc(ns.value(), f.value(), pts.toArray(new DataType[0]));
                    } else {
                        hash = hashFunc(ns.value(), f.value());
                    }
                    if (functions.get(hash) != null) {
                        throw new FunctionRegisterFailed(String.format("Function already registered (%s:%s)",ns.value(), f.value()));
                    } else {
                        DataType ctx = first;
                        T finalIns = ins;
                        functions.put(hash, (session, args) -> {
                            for (int i = 0; i < args.length; i++) {
                                Val<?> v = args[i];
                                if(v instanceof Reference) {
                                    args[i] = ((Reference) v).getVar();
                                }
                            }
                            Object[] objects;
                            if(ctx != null) {
                                objects = new Object[args.length + 1];
                                objects[0] = ctx == DataType.VM ? session.getVM() : session;
                                System.arraycopy(args, 0, objects, 1, args.length);
                            } else {
                                objects = args;
                            }
                            try {
                                m.invoke(finalIns, objects);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeError(String.format("Function link broken (%s:%s)",ns.value(), f.value()));
                            }
                        });
                        count++;
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FunctionRegisterFailed("Could not create instance");
        }
        return count;
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

        Instruction inst = new Instruction(namespace, function, args.toArray(new Val<?>[0]));
        inst.setCondition(condition);
        return inst;
    }

    @NotNull
    public Session newSession(@NotNull Instruction... instructions) {
        return new Session(this, instructions);
    }
}
