package org.darklol9.labs.utils.helper;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ReflectionHelper {

    private final Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<>();
    private final Map<Class<?>, Map<String, Method>> methodCache = new HashMap<>();

    public Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Map<String, Field> classFieldCache = fieldCache.computeIfAbsent(clazz, k -> new HashMap<>());

        Field field = classFieldCache.get(fieldName);
        if (field == null) {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            classFieldCache.put(fieldName, field);
        }

        return field;
    }

    public Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Map<String, Method> classMethodCache = methodCache.computeIfAbsent(clazz, k -> new HashMap<>());

        String methodKey = getMethodKey(methodName, parameterTypes);
        Method method = classMethodCache.get(methodKey);
        if (method == null) {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            classMethodCache.put(methodKey, method);
        }

        return method;
    }

    private String getMethodKey(String methodName, Class<?>... parameterTypes) {
        StringBuilder builder = new StringBuilder();
        builder.append(methodName);
        for (Class<?> paramType : parameterTypes) {
            builder.append("_");
            builder.append(paramType.getName());
        }
        return builder.toString();
    }

}
