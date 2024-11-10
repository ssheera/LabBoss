package org.darklol9.labs.utils.helper;

import java.util.Collections;
import java.util.LinkedList;

public class WeightedList<E> extends LinkedList<E> {

    public void add(E element, int weight) {
        for (int i = 0; i < weight; i++)
            this.add(element);
    }

    public E getRandom() {
        return this.get((int) (Math.random() * this.size()));
    }

    public void shuffle() {
        Collections.shuffle(this);
    }
}
