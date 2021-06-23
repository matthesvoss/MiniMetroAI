package me.dragonflyer.minimetro.ai.util.collections;

import com.google.common.collect.UnmodifiableListIterator;

import java.util.NoSuchElementException;

/**
 * This class provides an implementation of the {@link UnmodifiableListIterator} interface across a
 * fixed number of subsets of a power set that may be retrieved by position. It does not support
 * {@link #remove}, {@link #set}, or {@link #add}.
 *
 * @author Matthes Voß
 */
abstract class AbstractPowerSetIterator<E> extends UnmodifiableListIterator<E> {
    private final int size;
    private final int minSubsetCardinality, maxSubsetCardinality;
    private int position = 0;
    private int mask = 0;

    /** Returns the subset created with the specified bit mask. This method is called by {@link #next()}. */
    protected abstract E get(int setBits);

    /**
     * Constructs an iterator across a sequence of the given size whose initial position is 0. That
     * is, the first call to {@link #next()} will return the first element (or throw {@link
     * NoSuchElementException} if {@code size} is zero).
     *
     * @throws IllegalArgumentException if {@code size} is negative
     */
    protected AbstractPowerSetIterator(int size) {
        this(size, 0, size);
    }

    protected AbstractPowerSetIterator(int size, int minSubsetCardinality, int maxSubsetCardinality) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        this.size = size;
        this.minSubsetCardinality = minSubsetCardinality;
        this.maxSubsetCardinality = maxSubsetCardinality;
    }

    @Override
    public final boolean hasNext() {
        return position < size;
    }

    @Override
    public final E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        int subsetCardinality = Integer.bitCount(mask);
        while (subsetCardinality < minSubsetCardinality || subsetCardinality > maxSubsetCardinality) {
            subsetCardinality = Integer.bitCount(++mask);
        }
        position++;
        return get(mask++);
    }

    @Override
    public final int nextIndex() {
        return position;
    }

    @Override
    public final boolean hasPrevious() {
        return position > 0;
    }

    @Override
    public final E previous() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        position--;
        mask--;
        int subsetCardinality = Integer.bitCount(mask);
        while (subsetCardinality < minSubsetCardinality || subsetCardinality > maxSubsetCardinality) {
            mask--;
            if (mask < 0) {
                throw new NoSuchElementException();
            }
            subsetCardinality = Integer.bitCount(mask);
        }
        return get(mask);
    }

    @Override
    public final int previousIndex() {
        return position - 1;
    }
}
