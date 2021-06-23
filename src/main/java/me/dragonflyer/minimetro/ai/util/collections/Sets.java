package me.dragonflyer.minimetro.ai.util.collections;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;

import java.math.BigInteger;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class Sets {

    /**
     * Returns the set of all possible subsets of {@code set}. For example, {@code
     * powerSet(ImmutableSet.of(1, 2))} returns the set {@code {{}, {1}, {2}, {1, 2}}}.
     *
     * <p>Elements appear in these subsets in the same iteration order as they appeared in the input
     * set. The order in which these subsets appear in the outer set is undefined. Note that the power
     * set of the empty set is not the empty set, but a one-element set containing the empty set.
     *
     * <p>The returned set and its constituent sets use {@code equals} to decide whether two elements
     * are identical, even if the input set uses a different concept of equivalence.
     *
     * <p><i>Performance notes:</i> while the power set of a set with size {@code n} is of size {@code
     * 2^n}, its memory usage is only {@code O(n)}. When the power set is constructed, the input set
     * is merely copied. Only as the power set is iterated are the individual subsets created, and
     * these subsets themselves occupy only a small constant amount of memory.
     *
     * @param set the set of elements to construct a power set from
     * @return the power set, as an immutable set of immutable sets
     * @throws IllegalArgumentException if {@code set} has more than 30 unique elements (causing the
     *     power set size to exceed the {@code int} range)
     * @throws NullPointerException if {@code set} is or contains {@code null}
     * @see <a href="http://en.wikipedia.org/wiki/Power_set">Power set article at Wikipedia</a>
     */
    public static <E> Set<Set<E>> powerSet(Set<E> set) {
        return new PowerSet<E>(set, 0, set.size());
    }

    public static <E> Set<Set<E>> powerSet(Set<E> set, int minSubsetCardinality, int maxSubsetCardinality) {
        return new PowerSet<E>(set, minSubsetCardinality, maxSubsetCardinality);
    }

    private static final class SubSet<E> extends AbstractSet<E> {
        private final Map<E, Integer> inputSet;
        private final int mask;

        SubSet(Map<E, Integer> inputSet, int mask) {
            this.inputSet = inputSet;
            this.mask = mask;
        }

        @Override
        public Iterator<E> iterator() {
            return new UnmodifiableIterator<E>() {
                final List<E> elements = new ArrayList<>(inputSet.keySet());
                int remainingSetBits = mask;

                @Override
                public boolean hasNext() {
                    return remainingSetBits != 0;
                }

                @Override
                public E next() {
                    int index = Integer.numberOfTrailingZeros(remainingSetBits);
                    if (index == 32) {
                        throw new NoSuchElementException();
                    }
                    remainingSetBits &= ~(1 << index);
                    return elements.get(index);
                }
            };
        }

        @Override
        public int size() {
            return Integer.bitCount(mask);
        }

        @Override
        public boolean contains(Object o) {
            Integer index = inputSet.get(o);
            return index != null && (mask & (1 << index)) != 0;
        }
    }

    private static final class PowerSet<E> extends AbstractSet<Set<E>> {
        final Map<E, Integer> inputSet;
        final int minSubsetCardinality, maxSubsetCardinality;

        PowerSet(Set<E> input, int minSubsetCardinality, int maxSubsetCardinality) {
            int n = input.size();
            if (n > 30) {
                throw new IllegalArgumentException("Too many elements to create power set: " + input.size() + " > 30");
            }
            int greatestSubsetCardinality = binomial(n, n / 2).intValueExact();
            if (minSubsetCardinality < 0 || minSubsetCardinality > greatestSubsetCardinality
                    || maxSubsetCardinality < 0 || maxSubsetCardinality > greatestSubsetCardinality) {
                throw new IndexOutOfBoundsException();
            }

            this.inputSet = Maps.indexMap(input);
            this.minSubsetCardinality = minSubsetCardinality;
            this.maxSubsetCardinality = maxSubsetCardinality;
        }

        @Override
        public int size() {
            int n = inputSet.size();
            int lo = 0, hi = 0;
            // calculate number of subsets with a cardinality smaller than minSubsetCardinality
            if (minSubsetCardinality > 0) {
                for (int k = 0; k < minSubsetCardinality; k++) {
                    lo += binomial(n, k).intValueExact();
                }
            }
            // calculate number of subsets with a cardinality greater than maxSubsetCardinality
            if (maxSubsetCardinality < n) {
                for (int k = n; k > maxSubsetCardinality; k--) {
                    hi += binomial(n, k).intValueExact();
                }
            }
            return (1 << inputSet.size()) - lo - hi;
        }

        private static BigInteger binomial(final int N, final int K) {
            if (N < 0 || K < 0) {
                throw new IllegalArgumentException();
            }

            BigInteger ret = BigInteger.ONE;
            for (int k = 0; k < Math.min(K, N-K); k++) {
                ret = ret.multiply(BigInteger.valueOf(N-k))
                        .divide(BigInteger.valueOf(k+1));
            }
            return ret;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Iterator<Set<E>> iterator() {
            return new AbstractPowerSetIterator<Set<E>>(size(), minSubsetCardinality, maxSubsetCardinality) {
                @Override
                protected Set<E> get(final int mask) {
                    return new SubSet<E>(inputSet, mask);
                }
            };
        }

        @Override
        public boolean contains(Object obj) {
            if (obj instanceof Set) {
                Set<?> set = (Set<?>) obj;
                return inputSet.keySet().containsAll(set);
            }
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PowerSet) {
                PowerSet<?> that = (PowerSet<?>) obj;
                return inputSet.keySet().equals(that.inputSet.keySet());
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            /*
             * The sum of the sums of the hash codes in each subset is just the sum of
             * each input element's hash code times the number of sets that element
             * appears in. Each element appears in exactly half of the 2^n sets, so:
             */
            return inputSet.keySet().hashCode() << (inputSet.size() - 1);
        }

        @Override
        public String toString() {
            return "powerSet(" + inputSet + ")";
        }
    }
    
}
