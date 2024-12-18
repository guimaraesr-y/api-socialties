package br.com.socialties.helpers.controllers.utils;

import java.lang.reflect.Field;

public class ModelMapperUtil {

    /**
     * Maps properties from a source object to a target object.
     *
     * @param <T>       The type of the target object.
     * @param source    The source object with values to be copied.
     * @param target    The target object to be filled.
     * @return The target object filled.
     */
    public static <T> T mapNonNullProperties(Object source, T target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target objects cannot be null");
        }

        for (Field field : target.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Field sourceField = getField(source.getClass(), field.getName());

                if (sourceField != null) {
                    sourceField.setAccessible(true);
                    Object value = sourceField.get(source);

                    if (value != null) {
                        field.set(target, value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error mapping properties", e);
            }
        }

        return target;
    }

    /**
     * Gets a field from a class, including inherited fields.
     *
     * @param clazz     The class to search for the field.
     * @param fieldName The name of the field.
     * @return The corresponding field, or null if not found.
     */
    private static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}