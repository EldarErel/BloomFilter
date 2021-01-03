import java.util.BitSet;
/**
 * HashStructure -  Fast searching data structure
 * With 32-bit murmurHash3 implementation
 * Time complexity:
 * Insert - O(k)
 * Search - O(k)
 * -
 * 0 - the chance of declaring that a given key is not in the structure while it is
 * (1-(1-1/m)^k*n)^n - the chance of declaring that a given key is in the structure while it isn't
 * @author - Eldar Erel
 * @version - 20.12.20
 */
public class HashStructure {
    private final BitSet table; // bits array
    private final int k; // number of hash function allowed

    /**
     * Creates an empty hash structure
     * @param m - size of the structure
     * @param k - number of hash functions
     */
    public HashStructure (int m, int k) throws IllegalArgumentException {
        if (m < 1 || k < 1)
            throw new IllegalArgumentException("must be bigger than 1");
        table = new BitSet(m); // creates a size m bits array with unset bits
        this.k = k; // num of hash function
    }

    /**
     * Hash function
     * Generates index number for a key (number between 0-(m-1))
     * Time complexity O(1)
     * @param key  - the key
     * @param i - the function number
     * @return - the structure's index to add the key to (number between 0-(m-1))
     */
    private int h(Object key, int i) {
        byte[] arr = getBytes(key);
        if (i < 1 || i > k || arr == null) // hash function must in range,  arr is null when the key is not an integer or a string so returns -1
            return -1;
        return Math.abs(MurmurHash3.hash32x86(arr, i) % table.size()); // returns the index. number between 0-(m-1) , time complexity: O(1)
    }

    /**
     * Adds new key to the structure
     * Time complexity O(k)
     * @param key - the key to add
     */
    public void add(Object key) {
        if (!(key instanceof String) && !(key instanceof Integer))
            throw new IllegalArgumentException();
        for (int i = 1; i <= k; i++) {  // running k times
            int index = h(key, i); // calculating index
            table.set(index); // setting the bit
        }
    }

    /**
     * Look up a key in the structure
     * Time complexity O(k)
     * @param key - the key
     * @return - true if the key is in the structure, false otherwise
     */
    public boolean lookUp(Object key) {
        if (!(key instanceof String) && !(key instanceof Integer))
            return false;
        for (int i = 1; i <= k; i++) {  // running k times
            if (!table.get(h(key, i))) // if bit is not set return false
                return false;
        }
        return true; // if all bits are set returns true
    }

    /**
     * Converts a string or an integers into bytes arr
     * Time complexity O(1)
     * @param obj - the object to convert
     * @return - the object as a byte arr, or null if the object is not type of string or integer
     */
    private byte [] getBytes (Object obj) {
        // getting the bytes
        if (obj instanceof String) {
            return ((String) obj).getBytes(); // O(1)
        }
        /*
        Integers are 32 bits , byte is 8 bits, so we need 4 bytes to store an integer
        we uses a size of 4 byte array
        then we shift the bits of the integer to the left in order to get the 8 bits part
        Time complexity O(1)
         */
        else if (obj instanceof Integer) {
            int i = (Integer)obj;
            byte[] bArr = new byte[4];
            bArr[0] = (byte) (i >> 24); // last 8-bits
            bArr[1] = (byte) (i >> 16); // intermediate 8-bits
            bArr[2] = (byte) (i >> 8);
            bArr[3] = (byte) (i); // first 8-bits
            return bArr;
        }
        return null; // if obj is not integer or string then return null
    }
}
