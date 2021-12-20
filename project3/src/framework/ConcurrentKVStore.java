package framework;

// All reducers also have concurrent access to a kv store which maps
// a key to all the value associated to the key, for instace
// if reducer_i has fetched ("foo", 1) ("bar",1) ("foo",1)
// ("foo",1) and ("bar",1) from partition_i,
// then after injecting them, reducer_i should have updated the KV
// store to contain ("foo", {1,1,1}) and ("bar", {1,1}). You can use a
// concurrent hashmap/tree to implement the concurrent KV store


/**
 * If you choose to implement a concurrent hashmap to hold key-value pairs
 *  inserted concurrently by reducers,  you are required to support high
 *  concurrency by locking at the bucket level rather than the entire hashmap.  
 * Please note that high concurrency contributes to better performance, 
 * and it will be evaluated in tests.
 */

/**Modeled after provided single-threaded SimpleHashMap (https://replit.com/join/ezbeltcxsd-junyuan2**/

public class ConcurrentKVStore<K, V> {
    int capacity;
    int size;
    double loadfactor;
    Entry<K,V>[] table;

    public ConcurrentKVStore() {
        this(16);
    }

    public ConcurrentKVStore(int capacity) {
        this(capacity, 0.75);
    }

    public ConcurrentKVStore(int capacity, double loadfactor) {
        this.capacity = capacity;
        this.size = 0;
        this.loadfactor = loadfactor;
        this.table = (Entry<K,V>[]) new Entry[capacity];
    }
/*
    public String show() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(Entry<K,V> e : table) {
            sb.append(Integer.toString(i));
            while(e != null) {
                sb.append("\t");
                sb.append(e.toString());
                e = e.next;
            }
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }
*/
    public V get(Object arg0) {
        K key = (K)arg0;
        Entry<K,V> e = find(index(key), key);
        if(e != null) return e.getValue();
        return null;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private int index(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    private Entry<K,V> find(int index, K key) {
        Entry<K,V> e = table[index];
        while(e != null) {
            if (e.getKey().equals(key)) break;
            e = e.next;
        }
        return e;
    }
   
    //make this concurrent
    public V put(K arg0, V arg1) {
        int i = index(arg0);
        if(table[i] == null) {
            table[i] = new Entry<K,V>(arg0, arg1);
        } else {
            Entry<K,V> e = find(i, arg0);
            if(e != null) {
                V old_val = e.getValue();
                e.setValue(arg1);
                return old_val;
            }
            e = table[i];
            while(e.next != null) {
                e = e.next;
            }
            e.next = new Entry<K,V>(arg0, arg1);
        }
        size++;
        if(size > capacity*loadfactor) {
            capacity *= 2;
            rehash();
        }
        return null;
    }

    private void rehash() {
        Entry<K,V>[] old_table = table;
        table = (Entry<K,V>[]) new Entry[capacity];
        size = 0;
        for(Entry<K,V> e : old_table) {
            while(e != null) {
                put(e.getKey(), e.getValue());
                e = e.next;
            }
        }
    }

    //make this concurrent
    public V remove(Object arg0) {
        K key = (K)arg0;
        int i = index(key);
        if(table[i] == null) return null;
        else if(table[i].getKey().equals(key)) {
            Entry<K,V> e = table[i];
            table[i] = e.next;
            size--;
            return e.getValue();
        } else {
            Entry<K,V> e_prev = table[i];
            Entry<K,V> e_curr = table[i].next;
            while(e_curr != null) {
                if(e_curr.getKey().equals(key)) {
                    e_prev.next = e_curr.next;
                    size--;
                    return e_curr.getValue();
                }
                e_prev = e_curr;
                e_curr = e_curr.next;
            }
            return null;
        }
    }

    public int size() {
        return this.size;
    }

    private static class Entry<K, V> {

        private K key;
        private V val;
        private Entry<K,V> next;

        public Entry(K key, V val) {
            this.key = key;
            this.val = val;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return val;
        }

        public V setValue(V value) {
            this.val = value;
            return val;
        }

        public String toString() {
            return key + "=" + val;
        }
    }
}
