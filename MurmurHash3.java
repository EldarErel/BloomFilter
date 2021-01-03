/**
 * 32-bit MurmurHash3 implementation
 * Returns 32-bit hash code represents the object
 */

public abstract class MurmurHash3 {
    // Constants
    private static final int C1 = 0xcc9e2d51;
    private static final int C2 = 0x1b873593;
    private static final int R1 = 15;
    private static final int R2 = 13;
    private static final int M = 5;
    private static final int N = 0xe6546b64;

    /**
     *  Generates 32-bit hash from the byte array with a given seed.
     * @param data - the byte data
     * @param seed - seed
     * @return - 32-bit hash
     */
    public static int hash32x86(final byte[] data, final int seed) {
        int length = data.length;
        int offset = 0;
        int hash = seed;
        final int nblocks = length >> 2;
        // body
        for (int i = 0; i < nblocks; i++) {
            final int index = offset + (i << 2);
            final int k = getLittleEndianInt(data, index);
            hash = mix32(k, hash);
        }
        // tail
        final int index = offset + (nblocks << 2);
        int k1 = 0;
        switch (offset + length - index) {
            case 3:
                k1 ^= (data[index + 2] & 0xff) << 16;
            case 2:
                k1 ^= (data[index + 1] & 0xff) << 8;
            case 1:
                k1 ^= (data[index] & 0xff);
                // mix functions
                k1 *= C1;
                k1 = Integer.rotateLeft(k1, R1);
                k1 *= C2;
                hash ^= k1;
        }
        hash ^= length;
        return fmix32(hash);
    }

    /**
     * Gets the little-endian int from 4 bytes starting at the specified index.
     * @param data - data as bytes
     * @param index - start index
     * @return the endian inr
     */
    private static int getLittleEndianInt(final byte[] data, final int index) {
        return ((data[index] & 0xff)) |
                ((data[index + 1] & 0xff) << 8) |
                ((data[index + 2] & 0xff) << 16) |
                ((data[index + 3] & 0xff) << 24);
    }

    /**
     *  Performs the intermediate mix step of the 32-bit hash function.
     * @param k - key
     * @param hash - current hash code
     * @return - mixed hash code
     */
    private static int mix32(int k, int hash) {
        k *= C1;
        k = Integer.rotateLeft(k, R1);
        k *= C2;
        hash ^= k;
        return Integer.rotateLeft(hash, R2) * M + N;
    }

    /**
     * Performs the final avalanche mix step of the 32-bit hash function.
     * @param hash - current hash code
     * @return - final mixed hash code
     */
    private static int fmix32(int hash) {
        hash ^= (hash >>> 16);
        hash *= 0x85ebca6b;
        hash ^= (hash >>> 13);
        hash *= 0xc2b2ae35;
        hash ^= (hash >>> 16);
        return hash;
    }
}