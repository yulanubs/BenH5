/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.benmobile.protocol.observer;


import org.benmobile.protocol.utlis.ValueUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * MyObservable is used to notify a group of Observer objects when a change
 * occurs. On creation, the set of observers is empty. After a change occurred,
 * the application can call the {@link #notifyObservers()} method. This will
 * cause the invocation of the {@code update()} method of all registered
 * Observers. The order of invocation is not specified. This implementation will
 * call the Observers in the order they registered. Subclasses are completely
 * free in what order they call the update methods.
 *
 * @see MyObserver
 */
public class MyObservable {

    List<MyObserver> observers = new ArrayList<MyObserver>();

    boolean changed = false;

    /**
     * Constructs a new {@code MyObservable} object.
     */
    public MyObservable() {
    }

    /**
     * Adds the specified observer to the list of observers. If it is already
     * registered, it is not added a second time.
     *
     * @param observer
     *            the Observer to add.
     */
    public MyObserver addObserver(MyObserver observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        System.out.println("======ID"+observer.getPluginId());
        synchronized (this) {
            boolean add = true;
            MyObserver myObserver=null;
            for (MyObserver o : observers
                    ) {
                if(o.getPluginId().equals(observer.getPluginId())){
                    myObserver=o;
                    add = false;
                    break;
                }
            }
            if (add) {
                observers.add(observer);
                return null;
            }else{
                return myObserver;
            }
        }
    }

    /**
     * Clears the changed flag for this {@code MyObservable}. After calling
     * {@code clearChanged()}, {@code hasChanged()} will return {@code false}.
     */
    protected void clearChanged() {
        changed = false;
    }

    /**
     * Returns the number of observers registered to this {@code MyObservable}.
     *
     * @return the number of observers.
     */
    public int countObservers() {
        return observers.size();
    }

    /**
     * Removes the specified observer from the list of observers. Passing null
     * won't do anything.
     *
     * @param observer
     *            the observer to remove.
     */
    public synchronized void deleteObserver(MyObserver observer) {
        observers.remove(observer);
    }

    /**
     * Removes all observers from the list of observers.
     */
    public synchronized void deleteObservers() {
        observers.clear();
    }

    /**
     * Returns the changed flag for this {@code MyObservable}.
     *
     * @return {@code true} when the changed flag for this {@code MyObservable} is
     *         set, {@code false} otherwise.
     */
    public boolean hasChanged() {
        return changed;
    }

    /**
     * If {@code hasChanged()} returns {@code true}, calls the {@code update()}
     * method for every observer in the list of observers using null as the
     * argument. Afterwards, calls {@code clearChanged()}.
     * <p>
     * Equivalent to calling {@code notifyObservers(null)}.
     */
    public void notifyObservers() {
        notifyObservers(null,null);
    }

    /**
     * If {@code hasChanged()} returns {@code true}, calls the {@code update()}
     * method for every Observer in the list of observers using the specified
     * argument. Afterwards calls {@code clearChanged()}.
     *
     * @param data
     *            the argument passed to {@code update()}.
     */
    @SuppressWarnings("unchecked")
    public void notifyObservers(String pluginId,Object data) {
        int size = 0;
        MyObserver[] arrays = null;
        synchronized (this) {
            if (hasChanged()) {
                clearChanged();
                size = observers.size();
                arrays = new MyObserver[size];
                observers.toArray(arrays);
            }
        }
        if (ValueUtils.isNotEmpty(arrays)&& ValueUtils.isNotEmpty(pluginId)) {

                for (MyObserver observer : arrays) {
//                observer.update(this, data);
                    //data是数据源更新数据不知道什么途径存入，现在的要求是data中必须包含pluginid也就是observer创建时传入的tag
                    if(observer.getPluginId().equals(pluginId)){
                        observer.update(this,data);
                        break;
                    }
                }
            }


    }

    /**
     * Sets the changed flag for this {@code MyObservable}. After calling
     * {@code setChanged()}, {@code hasChanged()} will return {@code true}.
     */
    protected void setChanged() {
        changed = true;
    }
}
