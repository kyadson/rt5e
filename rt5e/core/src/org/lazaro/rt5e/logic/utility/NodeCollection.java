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
package org.lazaro.rt5e.logic.utility;

import org.lazaro.rt5e.logic.Node;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Lazaro
 */
public class NodeCollection<N extends Node> extends AbstractCollection<N> {
    private int currentIndex;
    private List<Integer> indexes = new LinkedList<Integer>();
    private boolean indexOptimization = true;
    private int minimumIndex, maximumIndex;
    private Object[] nodes;

    public NodeCollection() {
        this(0, 1024);
    }

    public NodeCollection(int maximumIndex) {
        this(0, maximumIndex);
    }

    public NodeCollection(int minimumIndex, int maximumIndex) {
        this.minimumIndex = minimumIndex;
        this.maximumIndex = maximumIndex;
        nodes = new Object[maximumIndex + 1];
        currentIndex = minimumIndex;
    }

    @Override
    public boolean add(N node) {
        return add(node, nextIndex());
    }

    /**
     * Adds an index node onto the list at the specified position.
     *
     * @param node  The object node.
     * @param index The position.
     * @return If the index node was added.
     */
    public boolean add(N node, int index) {
        if (index == -1) {
            return false;
        }
        if (index > maximumIndex || index < minimumIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds : "
                    + index);
        }
        if (nodes[index] != null) {
            return false;
        }
        nodes[index] = node;
        indexes.add(index);
        node.setIndex(index);
        return true;
    }

    public int capacity() {
        return maximumIndex - minimumIndex;
    }

    @Override
    public boolean contains(Object object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            if (node.getIndex() != -1 && node.getIndex() >= minimumIndex
                    && node.getIndex() <= maximumIndex) {
                return nodes[node.getIndex()].equals(node);
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public N get(int index) {
        return (N) nodes[index];
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public int getMaximumIndex() {
        return maximumIndex;
    }

    public int getMinimumIndex() {
        return minimumIndex;
    }

    public int indexOf(Object object) {
        if (contains(object)) {
            return ((Node) object).getIndex();
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return indexes.isEmpty();
    }

    public boolean isFull() {
        return indexes.size() >= maximumIndex;
    }

    @Override
    public Iterator<N> iterator() {
        return new NodeCollectionIterator<N>(this);
    }

    protected int nextIndex() {
        if (indexOptimization) {
            currentIndex++;
            if (currentIndex > maximumIndex) {
                currentIndex = minimumIndex;
            }
        }
        if (!indexOptimization || nodes[currentIndex] != null) {
            currentIndex = -1;
            for (int i = minimumIndex; i <= maximumIndex; i++) {
                if (nodes[i] == null) {
                    currentIndex = i;
                    break;
                }
            }
        }
        return currentIndex;
    }

    @SuppressWarnings("unchecked")
    public N remove(int index) {
        if (index > maximumIndex || index < minimumIndex) {
            throw new IndexOutOfBoundsException("Index out of bounds : "
                    + index);
        }
        N node = (N) nodes[index];
        nodes[index] = null;
        indexes.remove((Integer) index);
        node.setIndex(-1);
        return node;
    }

    public boolean remove(N node) {
        if (contains(node)) {
            return remove(node.getIndex()) != null;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object object) {
        if (object instanceof Node) {
            return remove((N) object);
        }
        return false;
    }

    /**
     * If true, this collection will optimize indexing object nodes to save
     * time. However, when enabled, the list will not be ordered properly.
     *
     * @param indexOptimization
     */
    public void setIndexOptimization(boolean indexOptimization) {
        this.indexOptimization = indexOptimization;
    }

    @Override
    public int size() {
        return indexes.size();
    }

    @Override
    public Object[] toArray() {
        return nodes;
    }

    @SuppressWarnings({"unchecked", "hiding"})
    @Override
    public <N> N[] toArray(N[] a) {
        N[] r = a;
        if (nodes.length > r.length) {
            r = (N[]) Array.newInstance(a.getClass().getComponentType(),
                    nodes.length);
        }
        for (int i = 0; i < nodes.length; i++) {
            r[i] = (N) nodes[i];
        }
        return r;
    }
}