package me.dragonflyer.minimetro.ai.util;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PowerSet<E> implements Iterator<Set<E>>, Iterable<Set<E>> {

    private E[] arr;
    private BitSet bset;
    private int minCardinality, maxCardinality;

    @SuppressWarnings("unchecked")
    public PowerSet(Set<E> set) {
        arr = (E[]) set.toArray();
        bset = new BitSet(arr.length + 1);
    }

    public PowerSet(Set<E> set, int minCardinality, int maxCardinality) {
        this(set);

        if (minCardinality < 0 || maxCardinality < 0) {
            throw new IllegalArgumentException();
        }
        this.minCardinality = Math.min(minCardinality, set.size());
        this.maxCardinality = Math.min(maxCardinality, set.size());
    }

    @Override
    public boolean hasNext() {
        return !bset.get(arr.length);
    }

    @Override
    public Set<E> next() {
        Set<E> returnSet = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {
            if (bset.get(i)) returnSet.add(arr[i]);
        }

        int cardinality;
        do {
            // increment bitset
            for (int i = 0; i < bset.size(); i++) {
                if (!bset.get(i)) {
                    bset.set(i);
                    break;
                } else bset.clear(i);
            }

            cardinality = bset.cardinality();
        } while (hasNext() && (cardinality < minCardinality || cardinality > maxCardinality));

        return returnSet;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not Supported!");
    }

    @Override
    public Iterator<Set<E>> iterator() {
        return this;
    }

}
