package util;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Jana Vadillo
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V>[] pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  /**
   * creates a new assosciative array with a larger size.
   *
   * @param customSize size of contents to be stored in array
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray(int customSize) {
    // Creating new arrays is sometimes a PITN.
    int capacity = 16;
    while (capacity < customSize) {
      capacity *= 2;
    } // grlw the capacity until it fits the size
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        capacity);

    this.size = customSize;

  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> newArray = new AssociativeArray<>(this.size);
    newArray.size = this.size;
    for (int i = 0; i < this.size; i++) {
      KVPair<K, V> currentPair = this.pairs[i].clone();
      newArray.pairs[i] = currentPair;
    } // loop adding clines of the current pair to the new array
    return newArray;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    String returnStr = "{";
    for (int i = 0; i < this.size; i++) {
      KVPair<K, V> currentPair = this.pairs[i];
      if (i == 0) {
        returnStr += currentPair.toString();
        continue;
      } // if loop to check if there is a value at the start to skip
      returnStr += ", " + currentPair.toString();
    } // loop adding lines of the current pair to the new array

    return returnStr + "}";
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   *
   * @param key
   *              The key whose value we are seeting.
   * @param value
   *              The value of that key.
   *
   * @throws NullKeyException
   *                          If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException("null key was used");
    } // raise exception in the case of NullKey

    KVPair<K, V> newpair = new KVPair<>(key, value);
    int keyAddress;

    try {
      keyAddress = find(key);
    } catch (KeyNotFoundException e) {
      this.size++;
      if (pairs.length >= this.size) {
        this.expand();
      } // Expand the array if needed
      this.pairs[this.size - 1] = newpair;
      return;

    } // if the key wasnt found add a new value
    this.pairs[keyAddress] = newpair;
    return;
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key
   *            A key
   *
   * @return
   *         The corresponding value
   *
   * @throws KeyNotFoundException
   *                              when the key is null or does not appear in the
   *                              associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    int n;
    try {
      n = find(key);
    } catch (KeyNotFoundException e) {
      throw new KeyNotFoundException("you searched for a key which does not exist");
    } // what to do if key was not found
    return this.pairs[n].val;

  } // get(K)


    /**
   * Get the key accociated with an index.
   *
   * @param key
   *            A key
   *
   * @return
   *         The corresponding value
   *
   * @throws KeyNotFoundException
   *                              when the key is null or does not appear in the
   *                              associative array.
   */
  public K getKey(int index) throws Exception {
    K thisKey;
    try {
      thisKey = this.pairs[index].key;
    } catch (Exception e) {
      throw new Exception(" thisdoes not exist");
    } // what to do if key was not found
    return thisKey;
  } // get(K)


  /**
   * Determine if key appears in the associative array. Should
   * return false for the null key, since it cannot appear.
   *
   * @param key
   *            The key we're looking for.
   *
   * @return true if the key appears and false otherwise.
   */
  public boolean hasKey(K key) {
    try {
      find(key);
    } catch (KeyNotFoundException e) {
      return false;
    } // catch if key was not found
    return true;
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   *
   * @param key
   *            The key to remove.
   */
  public void remove(K key) {

    int keyAddress;

    try {
      keyAddress = find(key);
    } catch (KeyNotFoundException e) {
      return;
    } // if the key wasnt found do nothing
    int cursor;

    for (cursor = keyAddress + 1; cursor < this.size; cursor++) {
      KVPair<K, V> currentPair = this.pairs[cursor];
      this.pairs[cursor - 1] = currentPair;
    } // if key is found note that value was found so that going forward things will
    this.pairs[cursor - 1] = null;
    this.size = this.size - 1;
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return The number of key/value pairs in the array.
   */
  public int size() {

    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   *
   * @param key
   *            The key of the entry.
   *
   * @return
   *         The index of the key, if found.
   *
   * @throws KeyNotFoundException
   *                              If the key does not appear in the associative
   *                              array.
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      KVPair<K, V> currentPair = this.pairs[i];
      if (currentPair.key.equals(key)) {
        return i;
      } // if key is found return the index
    } // loop looking for the value
    throw new KeyNotFoundException();
  } // find(K)

} // class AssociativeArray
