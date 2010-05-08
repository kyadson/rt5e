package org.lazaro.rt5e.network.encryption;

/**
 * <p>
 * This is an implmentation of the ISAAC algorithm. See <a
 * href="http://en.wikipedia.org/wiki/ISAAC_(cipher)"
 * >http://en.wikipedia.org/wiki/ISAAC_(cipher)</a> for more information.
 * <p/>
 * This implementation is based on the one created by Bob Jenkins converted to
 * Java. To see his original code, see <a
 * href="http://burtleburtle.net/bob/rand/isaacafa.html"
 * >http://burtleburtle.net/bob/rand/isaacafa.html</a>.
 * </p>
 *
 * @author Bob Jenkins
 */
public class ISAACCipher {
    private static final int LOG_SIZE = 8; // Log of size of results[] and memory[]
    private static final int SIZE = 1 << LOG_SIZE; // Size of results[] and
    private static final int MASK = (SIZE - 1) << 2; // For pseudorandom lookup
    private static final int RATIO = 0x9e3779b9; // The golden ratio memory[]

    private int accumulator; // Accumulator
    private int count; // Count through the results in results[]
    private int counter; // Counter, guarantees cycle is at least 2^40
    private int last; // The last result
    private int memory[]; // The internal state
    private int results[]; // The results given to the user

    /**
     * This constructor creates and initializes an new instance without using a
     * seed.<br>
     */
    public ISAACCipher() {
        memory = new int[SIZE];
        results = new int[SIZE];

        init(false);
    }

    /**
     * This constructor creates and initializes an new instance using a
     * user-provided seed.<br>
     *
     * @param seed The seed.
     */
    public ISAACCipher(int[] seed) {
        memory = new int[SIZE];
        results = new int[SIZE];

        System.arraycopy(seed, 0, results, 0,
                (seed.length <= results.length) ? seed.length : results.length);

        init(true);
    }

    /**
     * Initialize or reinitialize this instance.
     *
     * @param flag If <code>true</code> then use the seed (which the constructor
     *             placed in <code>results[]</code>) for initialization.
     */
    private final void init(boolean flag) {
        int i;
        int a, b, c, d, e, f, g, h;
        a = b = c = d = e = f = g = h = RATIO;

        for (i = 0; i < 4; ++i) {
            a ^= b << 11;
            d += a;
            b += c;
            b ^= c >>> 2;
            e += b;
            c += d;
            c ^= d << 8;
            f += c;
            d += e;
            d ^= e >>> 16;
            g += d;
            e += f;
            e ^= f << 10;
            h += e;
            f += g;
            f ^= g >>> 4;
            a += f;
            g += h;
            g ^= h << 8;
            b += g;
            h += a;
            h ^= a >>> 9;
            c += h;
            a += b;
        }

        for (i = 0; i < SIZE; i += 8) { // Fill in memory[] with messy stuff
            if (flag) {
                a += results[i];
                b += results[i + 1];
                c += results[i + 2];
                d += results[i + 3];
                e += results[i + 4];
                f += results[i + 5];
                g += results[i + 6];
                h += results[i + 7];
            }
            a ^= b << 11;
            d += a;
            b += c;
            b ^= c >>> 2;
            e += b;
            c += d;
            c ^= d << 8;
            f += c;
            d += e;
            d ^= e >>> 16;
            g += d;
            e += f;
            e ^= f << 10;
            h += e;
            f += g;
            f ^= g >>> 4;
            a += f;
            g += h;
            g ^= h << 8;
            b += g;
            h += a;
            h ^= a >>> 9;
            c += h;
            a += b;
            memory[i] = a;
            memory[i + 1] = b;
            memory[i + 2] = c;
            memory[i + 3] = d;
            memory[i + 4] = e;
            memory[i + 5] = f;
            memory[i + 6] = g;
            memory[i + 7] = h;
        }

        if (flag) { // Second pass: makes all of seed affect all of memory[]
            for (i = 0; i < SIZE; i += 8) {
                a += memory[i];
                b += memory[i + 1];
                c += memory[i + 2];
                d += memory[i + 3];
                e += memory[i + 4];
                f += memory[i + 5];
                g += memory[i + 6];
                h += memory[i + 7];
                a ^= b << 11;
                d += a;
                b += c;
                b ^= c >>> 2;
                e += b;
                c += d;
                c ^= d << 8;
                f += c;
                d += e;
                d ^= e >>> 16;
                g += d;
                e += f;
                e ^= f << 10;
                h += e;
                f += g;
                f ^= g >>> 4;
                a += f;
                g += h;
                g ^= h << 8;
                b += g;
                h += a;
                h ^= a >>> 9;
                c += h;
                a += b;
                memory[i] = a;
                memory[i + 1] = b;
                memory[i + 2] = c;
                memory[i + 3] = d;
                memory[i + 4] = e;
                memory[i + 5] = f;
                memory[i + 6] = g;
                memory[i + 7] = h;
            }
        }

        isaac();
        count = SIZE;
    }

    /**
     * Generate 256 results.<br>
     * This is a small (not fast) implementation.
     */
    private void isaac() {
        int i, x, y;

        last += ++counter;
        for (i = 0; i < SIZE; ++i) {
            x = memory[i];
            switch (i & 3) {
                case 0:
                    accumulator ^= accumulator << 13;
                    break;
                case 1:
                    accumulator ^= accumulator >>> 6;
                    break;
                case 2:
                    accumulator ^= accumulator << 2;
                    break;
                case 3:
                    accumulator ^= accumulator >>> 16;
                    break;
            }
            accumulator += memory[(i + SIZE / 2) & (SIZE - 1)];
            memory[i] = y = memory[((x) & MASK) >> 2] + accumulator + last;
            results[i] = last = memory[((y >> LOG_SIZE) & MASK) >> 2] + x;
        }
    }

    /**
     * Get a random integer value.
     */
    public int nextInt() {
        if (0 == count--) {
            isaac();
            count = SIZE - 1;
        }

        return (results[count]);
    }

    /**
     * Reseeds this random object.<br>
     * The given seed supplements (using bitwise xor), rather than replaces, the
     * existing seed.
     *
     * @param seed An integer array containing the seed.
     */
    public void supplementSeed(int[] seed) {
        for (int i = 0; i < seed.length; i++)
            memory[i % memory.length] ^= seed[i];
    }
}

