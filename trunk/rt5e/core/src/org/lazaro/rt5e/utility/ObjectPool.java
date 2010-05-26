/**
 * Copyright (C) 2010 Lazaro Brito
 *
 * This file is part of RT5E.
 *
 * RT5E is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RT5E is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RT5E.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.lazaro.rt5e.utility;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * This is an object pool used for caching recyclable objects and of course reusing them.
 * <p/>
 * For a class to be abled to be pooled,
 * it must have a constructor with no arguments and it must implement <code>Pooled</code>.
 * <p/>
 * This implementation is NOT thread safe.
 *
 * @author Lazaro
 */
public class ObjectPool<T extends Pooled> {
    private Class<?> parent;
    private Queue<T> objectPool;
    private Queue<T> aliveObjectPool;
    private Initiator initiator;

    /**
     * Initiates a new object pool.
     *
     * @param parent    The class of the objects being pooled.
     * @param initiator The initiator to setup the pooled objects.
     * @param size      The initial count of objects cached.
     */
    @SuppressWarnings("unchecked")
    public ObjectPool(Class<?> parent, Initiator initiator, int size) {
        this.parent = parent;
        this.objectPool = new ArrayDeque<T>(size);
        this.aliveObjectPool = new ArrayDeque<T>(size);
        this.initiator = initiator;
        try {
            for (int i = size; i != 0; i--) {
                T object = (T) parent.newInstance();
                initiator.init(object);
                objectPool.offer(object);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a new object that isn't being used.
     * <p/>
     * If the pool is currently empty, it will create a new object and add it to the pool.
     *
     * @return An object that isn't being used.
     */
    @SuppressWarnings("unchecked")
    public T acquire() {
        T object = objectPool.poll();
        if (object == null) {
            try {
                object = (T) parent.newInstance();
                initiator.init(object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        aliveObjectPool.add(object);
        return object;
    }

    /**
     * Releases an object and prepares it for reuse.
     *
     * @param object The object to be recycled.
     */
    public void release(T object) {
        object.recycle();
        aliveObjectPool.remove(object);
        objectPool.add(object);
    }

    /**
     * Gets the number of available cached objects.
     *
     * @return The number of available cached objects.
     */
    public int available() {
        return objectPool.size();
    }

    /**
     * Gets the total number of objects cached by the pool.
     *
     * @return The total number of objects cached by the pool.
     */
    public int size() {
        return objectPool.size() + aliveObjectPool.size();
    }

    /**
     * Gets the number of active cached objects.
     *
     * @return The number of active cached objects.
     */
    public int active() {
        return aliveObjectPool.size();
    }
}
