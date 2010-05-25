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

import java.util.Iterator;

/**
 * @author Lazaro
 */
public class NodeCollectionIterator<N extends Node> implements Iterator<N> {
    private Integer[] indexes;
    private NodeCollection<N> nodeCollection;
    private int offset = 0;

    public NodeCollectionIterator(NodeCollection<N> nodeList) {
        this.nodeCollection = nodeList;
        synchronized (nodeList) {
            this.indexes = nodeList.getIndexes().toArray(new Integer[0]);
        }
    }

    public boolean hasNext() {
        return offset != indexes.length;
    }

    public N next() {
        if (offset < indexes.length) {
            return nodeCollection.get(indexes[offset++]);
        }
        return null;
    }

    public void remove() {
        nodeCollection.remove(indexes[offset - 1]);
    }
}