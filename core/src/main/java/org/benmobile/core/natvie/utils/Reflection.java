/**
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/*
 * 
	 * @ClassName: Reflection<BR>
     * @Describe：Reflection auxiliary tools<BR>
     * @Author: Jekshow
     * @Version:1.0
     * @date:2016-10-20 下午3:36:53
 */
public class Reflection {
	
	private static Method findDeclaredMethod(Class<?> clazz, String name)  throws  Exception{
		Method[] methods = clazz.getDeclaredMethods();
        Method method = null;
        for (Method m : methods) {
        	if (name.equals(m.getName())) {
        		method = m;
        		break;
        	}
        }
        
        if (method == null) {
        	if (clazz.equals(Object.class)) {
				return null;
			}
        	return findDeclaredMethod(clazz.getSuperclass(), name);
        }
        return method;
	}
	
	private static Method findMethod(Class<?> clazz, String name)  throws  Exception{
		Method[] methods = clazz.getMethods();
        Method method = null;
        for (Method m : methods) {
        	if (name.equals(m.getName())) {
        		method = m;
        		break;
        	}
        }
        
        if (method == null) {
        	findDeclaredMethod(clazz, name);
        }
        return method;
	}
	
	
	private static Field findField(Class<?> clazz, String name)  throws Exception{
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			if (clazz.equals(Object.class)) {
				e.printStackTrace();
				return null;
			}
			Class<?> base = clazz.getSuperclass();
			return findField(base, name);
		}
	}
	
	public static Object setField(Object obj, String name, Object value)  throws  Exception{
		try {
			Field mBase;
			mBase = findField(obj.getClass(), name);
			mBase.setAccessible(true);
			Object old = mBase.get(obj);
			mBase.set(obj, value);
			return old;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object getField(Object obj, String name)  throws  Exception{
		try {
			Field mBase;
			mBase = findField(obj.getClass(), name);
			mBase.setAccessible(true);
			return mBase.get(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object callMethod(Object obj, String name, Object... arg)  throws  Exception{
        try {
			Method method = null;
			try {
				method = findMethod(obj.getClass(), name);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return method.invoke(obj, arg);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        
        return null;
	}
}
