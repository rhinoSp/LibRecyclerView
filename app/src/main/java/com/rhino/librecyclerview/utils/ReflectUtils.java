package com.rhino.librecyclerview.utils;

import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * <p>The utils of reflect.</p>
 *
 * @author LuoLin
 * @since Create on 2016/10/31.
 **/
public class ReflectUtils {

    /**
     * Get the field.
     *
     * @param sourceClass         the class
     * @param fieldName           the field name
     * @param isFindDeclaredField whether find declared field
     * @param isSuperFind         whether find super
     * @return Field
     */
    public static Field getField(Class<?> sourceClass, String fieldName, boolean isFindDeclaredField,
            boolean isSuperFind) {
        Field field = null;
        try {
            field = isFindDeclaredField ? sourceClass.getDeclaredField(fieldName) : sourceClass.getField(fieldName);
        } catch (NoSuchFieldException e1) {
            if (isSuperFind) {
                Class<?> cls = sourceClass.getSuperclass();
                while (field == null && cls != null) {
                    try {
                        field = isFindDeclaredField ? cls.getDeclaredField(fieldName) : cls.getField(fieldName);
                    } catch (NoSuchFieldException e11) {
                        cls = cls.getSuperclass();
                    }
                }
            }
        }
        return field;
    }

    /**
     * Get object by field name.
     *
     * @param object    the object
     * @param fieldName the field name
     * @param cls       T Class
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObjectByFieldName(Object object, String fieldName, Class<T> cls) {
        if (object != null && !TextUtils.isEmpty(fieldName) && cls != null) {
            try {
                Field field = getField(object.getClass(), fieldName, true, true);
                if (field != null) {
                    field.setAccessible(true);
                    return (T) field.get(object);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
