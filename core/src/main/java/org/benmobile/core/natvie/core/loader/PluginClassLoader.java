/*
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.core.loader;

import org.benmobile.core.natvie.core.bean.PluginInfo;

import dalvik.system.DexClassLoader;

public class PluginClassLoader extends DexClassLoader {
	/*
	 * When the host is the same as the plug-in includes classes: 0 indicates preferred host, 1 said plug-in is preferred
	 */
	static int classLoaderType = 0;

	public static void setClassLoaderType(int type) {
		classLoaderType = type;
	}

	public PluginInfo plugin;
	ClassLoader appClassLoader;

	/*
	 * the method name: make < BR >
	 * this method described is: create the plug-in loader instance
	 * @param dexPath
	 * @param optimizedDirectory
	 * @param libraryPath
	 * @param parent
	 * @return
	 */
	public static PluginClassLoader make(String dexPath,
			String optimizedDirectory, String libraryPath, ClassLoader parent) {
		if (parent instanceof SyknetAppClassLoader) {
			parent = parent.getParent();
		}
		
		if (classLoaderType == 0) {
			return new PluginClassLoader(dexPath, optimizedDirectory,
					libraryPath, parent);
		} else {
			PluginClassLoader cl = new PluginClassLoader(dexPath,
					optimizedDirectory, libraryPath,
					ClassLoader.getSystemClassLoader());
			cl.appClassLoader = parent;
			return cl;
		}
	}

	private PluginClassLoader(String dexPath, String optimizedDirectory,
			String libraryPath, ClassLoader parent) {
		super(dexPath, optimizedDirectory, libraryPath, parent);
	}

	@Override
	protected Class<?> loadClass(String className, boolean resolve)
			throws ClassNotFoundException {
		if (classLoaderType == 0) {
			return super.loadClass(className, resolve);
		} else {
			try {
				return super.loadClass(className, resolve);
			} catch (ClassNotFoundException e) {
				try {
					return appClassLoader.loadClass(className);
				} catch (ClassNotFoundException e2) {
					throw e;
				}
			}
		}
	}

}
