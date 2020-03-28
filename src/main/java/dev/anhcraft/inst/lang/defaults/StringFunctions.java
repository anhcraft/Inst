package dev.anhcraft.inst.lang.defaults;

import dev.anhcraft.inst.VM;
import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.values.BoolVal;
import dev.anhcraft.inst.values.IntVal;
import dev.anhcraft.inst.values.StringVal;
import org.jetbrains.annotations.NotNull;

@Namespace("String")
public class StringFunctions {
    @Function("Concat")
    public void concat(VM vm, StringVal a, StringVal b, StringVal resultVar){
        vm.setVariable(resultVar.get(), new StringVal() {
            final String str = a.get() + b.get();

            @NotNull
            @Override
            public String get() {
                return str;
            }
        });
    }

    @Function("Repeat")
    public void repeat(VM vm, StringVal str, IntVal times, StringVal resultVar){
        vm.setVariable(resultVar.get(), new StringVal() {
            String c;

            {
                int t = times.get();
                switch (t) {
                    case 0: {
                        c = "";
                        break;
                    }
                    case 1: {
                        c = str.get();
                        break;
                    }
                    default: {
                        StringBuilder sb = new StringBuilder();
                        String p = str.get();
                        for (int i = 0; i < t; i++) {
                            sb.append(p);
                        }
                        c = sb.toString();
                    }
                }
            }

            @NotNull
            @Override
            public String get() {
                return c;
            }
        });
    }

    @Function("Length")
    public void length(VM vm, StringVal str, StringVal resultVar){
        vm.setVariable(resultVar.get(), new IntVal() {
            final int len = str.get().length();

            @NotNull
            @Override
            public Integer get() {
                return len;
            }
        });
    }

    @Function("ToLowerCase")
    public void toLowerCase(VM vm, StringVal str, StringVal resultVar){
        vm.setVariable(resultVar.get(), new StringVal() {
            final String s = str.get().toLowerCase();

            @NotNull
            @Override
            public String get() {
                return s;
            }
        });
    }

    @Function("ToUpperCase")
    public void toUpperCase(VM vm, StringVal str, StringVal resultVar){
        vm.setVariable(resultVar.get(), new StringVal() {
            final String s = str.get().toUpperCase();

            @NotNull
            @Override
            public String get() {
                return s;
            }
        });
    }

    @Function("HasPrefix")
    public void hasPrefix(VM vm, StringVal str, StringVal prefix, StringVal resultVar){
        vm.setVariable(resultVar.get(), new BoolVal() {
            final boolean b = str.get().startsWith(prefix.get());

            @NotNull
            @Override
            public Boolean get() {
                return b;
            }
        });
    }

    @Function("HasSuffix")
    public void hasSuffix(VM vm, StringVal str, StringVal suffix, StringVal resultVar){
        vm.setVariable(resultVar.get(), new BoolVal() {
            final boolean b = str.get().endsWith(suffix.get());

            @NotNull
            @Override
            public Boolean get() {
                return b;
            }
        });
    }

    @Function("Trim")
    public void trim(VM vm, StringVal str, StringVal resultVar){
        vm.setVariable(resultVar.get(), new StringVal() {
            final String s = str.get().trim();

            @NotNull
            @Override
            public String get() {
                return s;
            }
        });
    }

    @Function("Find")
    public void find(VM vm, StringVal str, StringVal sub, StringVal resultVar){
        vm.setVariable(resultVar.get(), new IntVal() {
            final int i = str.get().indexOf(sub.get());

            @NotNull
            @Override
            public Integer get() {
                return i;
            }
        });
    }

    @Function("Contains")
    public void contains(VM vm, StringVal str, StringVal sub, StringVal resultVar){
        vm.setVariable(resultVar.get(), new BoolVal() {
            final boolean b = str.get().contains(sub.get());

            @NotNull
            @Override
            public Boolean get() {
                return b;
            }
        });
    }

    @Function("Replace")
    public void replace(VM vm, StringVal str, StringVal target, StringVal replacement, StringVal resultVar){
        vm.setVariable(resultVar.get(), new StringVal() {
            final String s = str.get().replace(target.get(), replacement.get());

            @NotNull
            @Override
            public String get() {
                return s;
            }
        });
    }

    @Function("ReplaceAll")
    public void replaceAll(VM vm, StringVal str, StringVal pattern, StringVal replacement, StringVal resultVar){
        vm.setVariable(resultVar.get(), new StringVal() {
            final String s = str.get().replaceAll(pattern.get(), replacement.get());

            @NotNull
            @Override
            public String get() {
                return s;
            }
        });
    }
}
