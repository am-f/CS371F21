package framework;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// All reducers also have concurrent access to a kv store which maps
// a key to all the value associated to the key, for instace
// if reducer_i has fetched ("foo", 1) ("bar",1) ("foo",1)
// ("foo",1) and ("bar",1) from partition_i,
// then after injecting them, reducer_i should have updated the KV
// store to contain ("foo", {1,1,1}) and ("bar", {1,1}). You can use a
// concurrent hashmap/tree to implement the concurrent KV store


/**Modeled after provided single-threaded SimpleHashMap (https://replit.com/join/ezbeltcxsd-junyuan2**/

public class ConcurrentKVStore<K, V> {
    int capacity;
    int size;
    double loadfactor;
    Entry<K,V>[] table;
    Lock[] tableLocks;
    Lock miscAttributeLock;
    ReentrantLock rehashLock = new ReentrantLock();
    Condition rehashDone = rehashLock.newCondition();
    Condition rehashCV = rehashLock.newCondition();
    boolean rehash = false;

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
        tableLocks = new ReentrantLock[capacity];
        
        for(int i = 0; i < capacity; i++){
            this.tableLocks[i] = new ReentrantLock();
        }

        this.miscAttributeLock = new ReentrantLock();
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
        if(rehashLock.isHeldByCurrentThread()) {
            //keep moving
        }
        else {
            rehashLock.lock();
            while (rehash == true) {
                try {
                    rehashDone.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            rehashLock.unlock();
        }

        K key = (K) arg0;
        Entry<K,V> e = find(index(key), key);
        if(e != null) return e.getValue();
        return null;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private int index(K key) {
        miscAttributeLock.lock();
        int ret = Math.abs(key.hashCode()) % capacity;
        miscAttributeLock.unlock();
        return ret;
    }

    private Entry<K,V> find(int index, K key) {
        if(rehashLock.isHeldByCurrentThread()) {
            //keep moving
        }
        else {
            rehashLock.lock();
            while (rehash == true) {
                try {
                    rehashDone.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            rehashLock.unlock();
        }

            tableLocks[index].lock();//turn on lock for tableLocks[index]
            Entry<K, V> e = table[index];
        try {
            while (e != null) {
                if (e.getKey().equals(key)) break;
                e = e.next;
            }
        } catch (Exception a) { }
        finally {
            tableLocks[index].unlock();//turn off lock for tableLocks[index]
            return e;
        }


    }

    public V put(K arg0, V arg1) {
        if(rehashLock.isHeldByCurrentThread()) {
            //keep moving
        }
        else {
            rehashLock.lock();
            while (rehash == true) {
                try {
                    rehashDone.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            rehashLock.unlock();
        }

        int i = index(arg0);
        
        tableLocks[i].lock();//turn on locks for tableLocks[i]
        if(table[i] == null) {
            table[i] = new Entry<K,V>(arg0, arg1);
        } else {
            Entry<K,V> e = find(i, arg0);
            if(e != null) {
                Entry<K,V> a = new Entry<K,V>(arg0, arg1);
                a.next = e.next;
                e.next = a;
                //V old_val = e.getValue();
                //e.setValue(arg1);
                tableLocks[i].unlock();
                return arg1;
            }
            e = table[i];
            while(e.next != null) {
                e = e.next;
            }
            e.next = new Entry<K,V>(arg0, arg1);
        }
        
        tableLocks[i].unlock();//turn off locks for tableLocks[i]

        miscAttributeLock.lock();//turn on locks for main table attributes, this is unlocked
        // in rehash or in else statement
        size++;
        if(size > capacity*loadfactor) {
            miscAttributeLock.unlock();
            rehash();
        }
        else {
            miscAttributeLock.unlock();
        }

        return null;
    }

    private void rehash() {
        //turn on locks for main table attributes
        //lock it down
        rehashLock.lock();
        rehash = true;
        int oldCapacity = capacity;
        int oldSize = size;
        int newCapacity = capacity * 2;
        int newSize = 0;
        Entry<K,V>[] oldTable = table;
        Lock[] oldLocks = tableLocks;
        Entry<K,V>[] newTable = (Entry<K,V>[]) new Entry[newCapacity];
        Lock[] newLocks = new ReentrantLock[newCapacity];
        for(int i = 0; i < newCapacity; i++){
            newLocks[i] = new ReentrantLock();
        }

        miscAttributeLock.lock();
        table = newTable;
        tableLocks = newLocks;
        capacity = newCapacity;
        size = newSize;
        miscAttributeLock.unlock();

        for(int i = 0; i < oldCapacity; i++) {
            Entry<K, V> e = oldTable[i];
            while(e != null) {
                put(e.getKey(), e.getValue());
            }
        }
        rehash = false;
        rehashDone.notifyAll();
        rehashLock.unlock();


       /*

        for(int i = 0; i < oldCapacity; i++){
            tableLocks[i].lock();
        }
        miscAttributeLock.lock();
        capacity *= 2;
        size = 0;

        Entry<K,V>[] old_table = table;
        table = (Entry<K,V>[]) new Entry[capacity];
        Lock[] oldLocks = tableLocks;
        tableLocks = new ReentrantLock[capacity];
        for(int i = 0; i < capacity; i++){
            tableLocks[i] = new ReentrantLock();
        }
        miscAttributeLock.unlock();
        for(int i = (oldCapacity - 1); i >= 0; i--){
            oldLocks[i].unlock();
        }

        //unlock it down

        for(int i = 0; i < oldCapacity; i++) {
            Entry<K, V> e = old_table[i];
            while(e != null) {
                put(e.getKey(), e.getValue());

            }
        }
        for(Entry<K,V> e : old_table) {
            while(e != null) {
                put(e.getKey(), e.getValue());
                e = e.next;
            }
        }

        */
    }

    public V remove(Object arg0) {
        if(rehashLock.isHeldByCurrentThread()) {
            //keep moving
        }
        else {
            rehashLock.lock();
            while (rehash == true) {
                try {
                    rehashDone.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            rehashLock.unlock();
        }
        K key = (K)arg0;
        int i = index(key);
        tableLocks[i].lock();
        if(table[i] == null) {
            tableLocks[i].unlock();
            return null;
        }
        else if(table[i].getKey().equals(key)) {
            Entry<K,V> e = table[i];
            table[i] = e.next;
            miscAttributeLock.lock();
            size--;
            miscAttributeLock.unlock();
            tableLocks[i].unlock();
            return e.getValue();
        } else {
            Entry<K,V> e_prev = table[i];
            Entry<K,V> e_curr = table[i].next;
            while(e_curr != null) {
                if(e_curr.getKey().equals(key)) {
                    e_prev.next = e_curr.next;
                    miscAttributeLock.lock();
                    size--;
                    miscAttributeLock.unlock();
                    tableLocks[i].unlock();
                    return e_curr.getValue();
                }
                e_prev = e_curr;
                e_curr = e_curr.next;
            }
            tableLocks[i].unlock();
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
            this.next = null;
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
