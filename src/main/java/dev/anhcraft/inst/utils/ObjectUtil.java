package dev.anhcraft.inst.utils;

public class ObjectUtil {
    public static int toInt(Object object){
        if(object instanceof Number) {
            return ((Number) object).intValue();
        } else if(object instanceof String) {
            return Integer.parseInt((String) object);
        } else {
            return 0;
        }
    }

    public static long toLong(Object object){
        if(object instanceof Number) {
            return ((Number) object).longValue();
        } else if(object instanceof String) {
            return Long.parseLong((String) object);
        } else {
            return 0;
        }
    }

    public static double toDouble(Object object){
        if(object instanceof Number) {
            return ((Number) object).doubleValue();
        } else if(object instanceof String) {
            return Double.parseDouble((String) object);
        } else {
            return 0;
        }
    }

    public static boolean toBool(Object object){
        if(object instanceof Byte) {
            return (Byte) object != 0;
        } else if(object instanceof Short) {
            return (Short) object != 0;
        } else if(object instanceof Integer) {
            return (Integer) object != 0;
        } else if(object instanceof Long) {
            return (Long) object != 0;
        } else if(object instanceof Float) {
            return (Float) object != 0;
        } else if(object instanceof Double) {
            return (Double) object != 0;
        } else if(object instanceof Boolean) {
            return (Boolean) object;
        } else if(object instanceof String) {
            return ((String) object).equalsIgnoreCase("true");
        } else {
            return false;
        }
    }

    public static String toString(Object object){
        return String.valueOf(object);
    }
}
