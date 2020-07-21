package dev.anhcraft.inst.lang.defaults;

import dev.anhcraft.inst.VM;
import dev.anhcraft.inst.annotations.Function;
import dev.anhcraft.inst.annotations.Namespace;
import dev.anhcraft.inst.lang.DataType;
import dev.anhcraft.inst.lang.Reference;
import dev.anhcraft.inst.utils.ObjectUtil;
import dev.anhcraft.inst.values.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

@Namespace("Math")
public class MathFunctions {
    private static final IntVal ONE = new IntVal(1);

    @Function("Round")
    public void round(VM vm, Reference resultVar, Val<?> num, IntVal scale) {
        String s = ObjectUtil.toString(num.getData());
        double v = new BigDecimal(s).setScale(scale.getData(), RoundingMode.HALF_UP).doubleValue();
        vm.setVariable(resultVar.getTarget(), new DoubleVal(v));
    }

    private void sum(VM vm, Reference resultVar, Val<?> num1, Val<?> num2, int c) {
        DataType t1 = num1.getType();
        DataType t2 = num2.getType();
        if(!t1.isNumber() || !t2.isNumber()) return;
        if(t1 == t2) {
            switch (t1) {
                case INT: {
                    int a = (Integer) num1.getData();
                    int b = (Integer) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new IntVal(a + b * c));
                    break;
                }
                case DOUBLE: {
                    double a = (Double) num1.getData();
                    double b = (Double) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new DoubleVal(a + b * c));
                    break;
                }
                case LONG: {
                    long a = (Long) num1.getData();
                    long b = (Long) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new LongVal(a + b * c));
                    break;
                }
            }
        } else if(t1 == DataType.INT) {
            if(t2 == DataType.DOUBLE) {
                int a = (Integer) num1.getData();
                double b = (Double) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a + b * c));
            }
            else if(t2 == DataType.LONG) {
                int a = (Integer) num1.getData();
                long b = (Long) num2.getData();
                vm.setVariable(resultVar.getTarget(), new LongVal(a + b * c));
            }
        } else if(t1 == DataType.DOUBLE) {
            if(t2 == DataType.INT) {
                double a = (Double) num1.getData();
                int b = (Integer) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a + b * c));
            }
            else if(t2 == DataType.LONG) {
                double a = (Double) num1.getData();
                long b = (Long) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a + b * c));
            }
        } else if(t1 == DataType.LONG) {
            if(t2 == DataType.INT) {
                long a = (Long) num1.getData();
                int b = (Integer) num2.getData();
                vm.setVariable(resultVar.getTarget(), new LongVal(a + b * c));
            }
            else if(t2 == DataType.DOUBLE) {
                long a = (Long) num1.getData();
                double b = (Double) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a + b * c));
            }
        }
    }

    @Function("Add")
    public void add(VM vm, Reference resultVar, Val<?> num1, Val<?> num2) {
        sum(vm, resultVar, num1, num2, 1);
    }

    @Function("Sub")
    public void sub(VM vm, Reference resultVar, Val<?> num1, Val<?> num2) {
        sum(vm, resultVar, num1, num2, -1);
    }

    @Function("Inc")
    public void dec(VM vm, Reference var) {
        sum(vm, var, var.getVar(), ONE, 1);
    }

    @Function("Dec")
    public void inc(VM vm, Reference var) {
        sum(vm, var, var.getVar(), ONE, -1);
    }

    @Function("Mul")
    public void mul(VM vm, Reference resultVar, Val<?> num1, Val<?> num2) {
        DataType t1 = num1.getType();
        DataType t2 = num2.getType();
        if(!t1.isNumber() || !t2.isNumber()) return;
        if(t1 == t2) {
            switch (t1) {
                case INT: {
                    int a = (Integer) num1.getData();
                    int b = (Integer) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new IntVal(a * b));
                    break;
                }
                case DOUBLE: {
                    double a = (Double) num1.getData();
                    double b = (Double) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new DoubleVal(a * b));
                    break;
                }
                case LONG: {
                    long a = (Long) num1.getData();
                    long b = (Long) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new LongVal(a * b));
                    break;
                }
            }
        } else if(t1 == DataType.INT) {
            if(t2 == DataType.DOUBLE) {
                int a = (Integer) num1.getData();
                double b = (Double) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a * b));
            }
            else if(t2 == DataType.LONG) {
                int a = (Integer) num1.getData();
                long b = (Long) num2.getData();
                vm.setVariable(resultVar.getTarget(), new LongVal(a * b));
            }
        } else if(t1 == DataType.DOUBLE) {
            if(t2 == DataType.INT) {
                double a = (Double) num1.getData();
                int b = (Integer) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a * b));
            }
            else if(t2 == DataType.LONG) {
                double a = (Double) num1.getData();
                long b = (Long) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a * b));
            }
        } else if(t1 == DataType.LONG) {
            if(t2 == DataType.INT) {
                long a = (Long) num1.getData();
                int b = (Integer) num2.getData();
                vm.setVariable(resultVar.getTarget(), new LongVal(a * b));
            }
            else if(t2 == DataType.DOUBLE) {
                long a = (Long) num1.getData();
                double b = (Double) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a * b));
            }
        }
    }

    @Function("Div")
    public void div(VM vm, Reference resultVar, Val<?> num1, Val<?> num2) {
        DataType t1 = num1.getType();
        DataType t2 = num2.getType();
        if(!t1.isNumber() || !t2.isNumber()) return;
        double a = ObjectUtil.toDouble(num1.getData());
        double b = ObjectUtil.toDouble(num2.getData());
        vm.setVariable(resultVar.getTarget(), new DoubleVal(a / b));
    }

    @Function("Exp")
    public void exp(VM vm, Reference resultVar, Val<?> num1, Val<?> num2) {
        DataType t1 = num1.getType();
        DataType t2 = num2.getType();
        if(!t1.isNumber() || !t2.isNumber()) return;
        double a = ObjectUtil.toDouble(num1.getData());
        double b = ObjectUtil.toDouble(num2.getData());
        vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.pow(a, b)));
    }

    @Function("Mod")
    public void mod(VM vm, Reference resultVar, Val<?> num1, Val<?> num2) {
        DataType t1 = num1.getType();
        DataType t2 = num2.getType();
        if(!t1.isNumber() || !t2.isNumber()) return;
        if(t1 == t2) {
            switch (t1) {
                case INT: {
                    int a = (Integer) num1.getData();
                    int b = (Integer) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new IntVal(a % b));
                    break;
                }
                case DOUBLE: {
                    double a = (Double) num1.getData();
                    double b = (Double) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new DoubleVal(a % b));
                    break;
                }
                case LONG: {
                    long a = (Long) num1.getData();
                    long b = (Long) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new LongVal(a % b));
                    break;
                }
            }
        } else if(t1 == DataType.INT) {
            if(t2 == DataType.DOUBLE) {
                int a = (Integer) num1.getData();
                double b = (Double) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a % b));
            }
            else if(t2 == DataType.LONG) {
                int a = (Integer) num1.getData();
                long b = (Long) num2.getData();
                vm.setVariable(resultVar.getTarget(), new LongVal(a % b));
            }
        } else if(t1 == DataType.DOUBLE) {
            if(t2 == DataType.INT) {
                double a = (Double) num1.getData();
                int b = (Integer) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a % b));
            }
            else if(t2 == DataType.LONG) {
                double a = (Double) num1.getData();
                long b = (Long) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a % b));
            }
        } else if(t1 == DataType.LONG) {
            if(t2 == DataType.INT) {
                long a = (Long) num1.getData();
                int b = (Integer) num2.getData();
                vm.setVariable(resultVar.getTarget(), new LongVal(a % b));
            }
            else if(t2 == DataType.DOUBLE) {
                long a = (Long) num1.getData();
                double b = (Double) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(a % b));
            }
        }
    }

    @Function("RandomInt")
    public void randomInt(VM vm, Reference resultVar, Val<?> min, Val<?> max) {
        vm.setVariable(resultVar.getTarget(), new IntVal(ThreadLocalRandom.current().nextInt(
                ObjectUtil.toInt(min.getData()),
                ObjectUtil.toInt(max.getData())+1
        )));
    }

    @Function("RandomLong")
    public void randomLong(VM vm, Reference resultVar, Val<?> min, Val<?> max) {
        vm.setVariable(resultVar.getTarget(), new LongVal(ThreadLocalRandom.current().nextLong(
                ObjectUtil.toLong(min.getData()),
                ObjectUtil.toLong(max.getData())+1
        )));
    }

    @Function("RandomDouble")
    public void randomDouble(VM vm, Reference resultVar, Val<?> min, Val<?> max) {
        vm.setVariable(resultVar.getTarget(), new DoubleVal(ThreadLocalRandom.current().nextDouble(
                ObjectUtil.toDouble(min.getData()),
                ObjectUtil.toDouble(max.getData())+1
        )));
    }

    @Function("RandomBool")
    public void randomBool(VM vm, Reference resultVar) {
        vm.setVariable(resultVar.getTarget(), new BoolVal(ThreadLocalRandom.current().nextBoolean()));
    }

    @Function("Negate")
    public void negate(VM vm, Reference var) {
        switch (var.getType()){
            case INT: {
                Integer x = (Integer) var.getData();
                vm.setVariable(var.getTarget(), new IntVal(-x));
                break;
            }
            case LONG: {
                Long x = (Long) var.getData();
                vm.setVariable(var.getTarget(), new LongVal(-x));
                break;
            }
            case DOUBLE: {
                Double x = (Double) var.getData();
                vm.setVariable(var.getTarget(), new DoubleVal(-x));
                break;
            }
            case BOOL: {
                Boolean x = (Boolean) var.getData();
                vm.setVariable(var.getTarget(), new BoolVal(!x));
                break;
            }
        }
    }

    @Function("Sqrt")
    public void sqrt(VM vm, Reference resultVar, Val<?> num) {
        DataType t = num.getType();
        if(!t.isNumber()) return;
        double x = ObjectUtil.toDouble(num.getData());
        vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.sqrt(x)));
    }

    @Function("Min")
    public void min(VM vm, Reference resultVar, Val<?> num1, Val<?> num2) {
        DataType t1 = num1.getType();
        DataType t2 = num2.getType();
        if(!t1.isNumber() || !t2.isNumber()) return;
        if(t1 == t2) {
            switch (t1) {
                case INT: {
                    int a = (Integer) num1.getData();
                    int b = (Integer) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new IntVal(Math.min(a, b)));
                    break;
                }
                case DOUBLE: {
                    double a = (Double) num1.getData();
                    double b = (Double) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.min(a, b)));
                    break;
                }
                case LONG: {
                    long a = (Long) num1.getData();
                    long b = (Long) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new LongVal(Math.min(a, b)));
                    break;
                }
            }
        } else if(t1 == DataType.INT) {
            if(t2 == DataType.DOUBLE) {
                int a = (Integer) num1.getData();
                double b = (Double) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.min(a, b)));
            }
            else if(t2 == DataType.LONG) {
                int a = (Integer) num1.getData();
                long b = (Long) num2.getData();
                vm.setVariable(resultVar.getTarget(), new LongVal(Math.min(a, b)));
            }
        } else if(t1 == DataType.DOUBLE) {
            if(t2 == DataType.INT) {
                double a = (Double) num1.getData();
                int b = (Integer) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.min(a, b)));
            }
            else if(t2 == DataType.LONG) {
                double a = (Double) num1.getData();
                long b = (Long) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.min(a, b)));
            }
        } else if(t1 == DataType.LONG) {
            if(t2 == DataType.INT) {
                long a = (Long) num1.getData();
                int b = (Integer) num2.getData();
                vm.setVariable(resultVar.getTarget(), new LongVal(Math.min(a, b)));
            }
            else if(t2 == DataType.DOUBLE) {
                long a = (Long) num1.getData();
                double b = (Double) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.min(a, b)));
            }
        }
    }

    @Function("Max")
    public void max(VM vm, Reference resultVar, Val<?> num1, Val<?> num2) {
        DataType t1 = num1.getType();
        DataType t2 = num2.getType();
        if(!t1.isNumber() || !t2.isNumber()) return;
        if(t1 == t2) {
            switch (t1) {
                case INT: {
                    int a = (Integer) num1.getData();
                    int b = (Integer) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new IntVal(Math.max(a, b)));
                    break;
                }
                case DOUBLE: {
                    double a = (Double) num1.getData();
                    double b = (Double) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.max(a, b)));
                    break;
                }
                case LONG: {
                    long a = (Long) num1.getData();
                    long b = (Long) num2.getData();
                    vm.setVariable(resultVar.getTarget(), new LongVal(Math.max(a, b)));
                    break;
                }
            }
        } else if(t1 == DataType.INT) {
            if(t2 == DataType.DOUBLE) {
                int a = (Integer) num1.getData();
                double b = (Double) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.max(a, b)));
            }
            else if(t2 == DataType.LONG) {
                int a = (Integer) num1.getData();
                long b = (Long) num2.getData();
                vm.setVariable(resultVar.getTarget(), new LongVal(Math.max(a, b)));
            }
        } else if(t1 == DataType.DOUBLE) {
            if(t2 == DataType.INT) {
                double a = (Double) num1.getData();
                int b = (Integer) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.max(a, b)));
            }
            else if(t2 == DataType.LONG) {
                double a = (Double) num1.getData();
                long b = (Long) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.max(a, b)));
            }
        } else if(t1 == DataType.LONG) {
            if(t2 == DataType.INT) {
                long a = (Long) num1.getData();
                int b = (Integer) num2.getData();
                vm.setVariable(resultVar.getTarget(), new LongVal(Math.max(a, b)));
            }
            else if(t2 == DataType.DOUBLE) {
                long a = (Long) num1.getData();
                double b = (Double) num2.getData();
                vm.setVariable(resultVar.getTarget(), new DoubleVal(Math.max(a, b)));
            }
        }
    }
}
