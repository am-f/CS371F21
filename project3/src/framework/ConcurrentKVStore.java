package framework;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    ReentrantLock[] tableLocks;
    Lock miscAttributeLock;

    protected Logger LOGGER = Logger.getLogger(MyMapReduce.class.getName());
    ReentrantLock rehashLock = new ReentrantLock();
    Condition rehashDone = rehashLock.newCondition();
    Condition rehashCV = rehashLock.newCondition();
    boolean rehash = false;

    //public ConcurrentKVStore() {this(16);}

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
            try {
                while (rehash == true) {
                    LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " await");
                    rehashDone.await();
                    LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " done await");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " line 88");
            } finally {

                rehashLock.unlock();
            }
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
        int ret = -1;
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString()
                + "line107");
        miscAttributeLock.lock();
        try {
            ret = Math.abs(key.hashCode()) % capacity;

        } catch (Exception e) {
            LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " line 112");
            e.printStackTrace();
        } finally {
            miscAttributeLock.unlock();
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString()
                            + "line120");
            return ret;
        }

    }

    private Entry<K,V> find(int index, K key) {

        if(rehashLock.isHeldByCurrentThread()) {
            //keep moving
        }
        else {
            rehashLock.lock();
            try {
                while (rehash == true) {
                    LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " await");
                    rehashDone.await();
                    LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " done await");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " line136");
            } finally {
                rehashLock.unlock();
            }
        }
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", tL[" + index + "]: " + tableLocks[index].toString()
                        + "line149");
            tableLocks[index].lock();//turn on lock for tableLocks[index]
            Entry<K, V> e = table[index];
        try {
            while (e != null) {
                if (e.getKey().equals(key)) break;
                e = e.next;
            }
        } catch (Exception a) {
            a.printStackTrace();
            LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " line151");
        }
        finally {
            LOGGER.log(Level.INFO,
                    "tableLocks[" + index + "] heldByCurrent " + Thread.currentThread().getName() +
                            ": " + tableLocks[index].isHeldByCurrentThread());
            tableLocks[index].unlock();//turn off lock for tableLocks[index]
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", tL[" + index + "]: " + tableLocks[index].toString() + "line166");
            return e;
        }


    }

    public V put(K arg0, V arg1) {

        if(rehashLock.isHeldByCurrentThread()) {
            //keep moving
        }
        else {
            rehashLock.lock();
            try {
                while (rehash == true) {
                    LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " await");
                    rehashDone.await();
                    //rehashLock.wait();
                    LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " done await");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " line180");
            } finally {
                rehashLock.unlock();
            }
        }

        int i = index(arg0);
        V ret = null;
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", tL[" + i + "]: " + tableLocks[i].toString() + "line198");
        tableLocks[i].lock();//turn on locks for tableLocks[i]
        try {
            if (table[i] == null) {
                table[i] = new Entry<K, V>(arg0, arg1);
            } else {
                Entry<K, V> e = find(i, arg0);
                if (e != null) {
                    Entry<K, V> a = new Entry<K, V>(arg0, arg1);
                    a.next = e.next;
                    e.next = a;
                    //V old_val = e.getValue();
                    //e.setValue(arg1);
                    //tableLocks[i].unlock();
                    ret = arg1;
                } else {
                    e = table[i];
                    while (e.next != null) {
                        e = e.next;
                    }
                    e.next = new Entry<K, V>(arg0, arg1);
                    ret = arg1;
                }
            }
        }
        finally {
            LOGGER.log(Level.INFO,
                    "tableLocks[" + i + "] heldByCurrent " + Thread.currentThread().getName() +
                            ": " + tableLocks[i].isHeldByCurrentThread());
            tableLocks[i].unlock();//turn off locks for tableLocks[i]
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", tL[" + i + "]: " + tableLocks[i].toString() + "line230");

        }
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() +
                        "line233");
        miscAttributeLock.lock();//turn on locks for main table attributes, this is unlocked
        // in rehash or in else statement
        size++;
        if(size > capacity*loadfactor && rehash == false) {
            miscAttributeLock.unlock();
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() + "line241");
            LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " TIME TO REHASH");
            rehash();
        }
        else {
            miscAttributeLock.unlock();
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() + "line249");
        }

        return arg1;
    }

    private void rehash() {
        //turn on locks for main table attributes
        //lock it down
        rehashLock.lock();
        //rehash = true;
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() +
                        "line260");
        miscAttributeLock.lock();
        rehash = true;
        if(size <= capacity*loadfactor) {
            LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " NO REHASH");
            rehash = false;
            rehashDone.signalAll();
            miscAttributeLock.unlock();
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() + "line271");
            rehashLock.unlock();
            return;
        }
        miscAttributeLock.unlock();
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() +
                        "line277");

        LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " START REHASH");
        /*for(int i = 0; i < capacity; i++) {
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", oldtL[" + i + "]: " + tableLocks[i].toString() + "line282");
            tableLocks[i].lock();
        } */
        boolean allLocked = true;
        for(int i = 0; i < capacity; i++) {
            boolean temp = tableLocks[i].tryLock();
            if(temp == false) { allLocked = false; }
            if(i == capacity - 1 && allLocked == false) {
                Thread tempThread = Thread.currentThread();
                LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " time to sleep");
                    //tempThread.sleep(1);
                try {
                    tempThread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " return from " +
                        "sleep");

                // Thread.currentThread().sleep(.01);
                i = 0;
            }
        }
        int oldCapacity = capacity;
        int oldSize = size;
        int newCapacity = capacity * 2;
        int newSize = 0;
        Entry<K,V>[] oldTable = table;
        ReentrantLock[] oldLocks = tableLocks;
        Entry<K,V>[] newTable = (Entry<K,V>[]) new Entry[newCapacity];
        ReentrantLock[] newLocks = new ReentrantLock[newCapacity];
        for(int i = 0; i < newCapacity; i++){
            newLocks[i] = new ReentrantLock();
        }
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() +
                        "line297");
        miscAttributeLock.lock();
        table = newTable;
        tableLocks = newLocks;
        capacity = newCapacity;
        size = newSize;
        miscAttributeLock.unlock();
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() +
                        "line306");

        for(int i = 0; i < oldCapacity; i++) {
            Entry<K, V> e = oldTable[i];
            Entry<K, V> mov;
            while(e != null) {
                put(e.getKey(), e.getValue());
                e = e.next;
                LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + "rehash size: " + size);
            }
        }
        for(int i = oldCapacity - 1; i >= 0; i--) {
            oldLocks[i].unlock();
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", oldtL[" + i + "]: " + tableLocks[i].toString() + "line322");
        }
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() +
                        "line324");
        miscAttributeLock.lock();
        rehash = false;
        miscAttributeLock.unlock();
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() +
                        "line330");
        LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + "END REHASH");
        oldLocks = null;
        oldTable = null;
        LOGGER.log(Level.INFO, rehashLock.toString());
        LOGGER.log(Level.INFO, "rehashLock heldByCurrent: " + rehashLock.isHeldByCurrentThread());
        LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + "rehashDone wait queue " +
                "length: " + rehashLock.getWaitQueueLength(rehashDone));
        rehashDone.signalAll();
        LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " rehashDone.signalAll()");
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
            try {
                while (rehash == true) {
                    LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " await");
                    rehashDone.await();
                    LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " done await");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.log(Level.INFO, "" + Thread.currentThread().getName() + " line358");
            } finally {
                rehashLock.unlock();
            }
        }

        K key = (K)arg0;
        int i = index(key);
        LOGGER.log(Level.INFO,
                Thread.currentThread().getName() + ", tL[" + i + "]: " + tableLocks[i].toString() + "line409");
        tableLocks[i].lock();
        if(table[i] == null) {
            LOGGER.log(Level.INFO,
                    "tableLocks[" + i + "] heldByCurrent " + Thread.currentThread().getName() +
                            ": " + tableLocks[i].isHeldByCurrentThread());
            tableLocks[i].unlock();
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", tL[" + i + "]: " + tableLocks[i].toString() + "line417");
            return null;
        }
        else if(table[i].getKey().equals(key)) {
            Entry<K,V> e = table[i];
            table[i] = e.next;
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() + "line424");
            miscAttributeLock.lock();
            size--;
            LOGGER.log(Level.INFO,
                    "tableLocks[" + i + "] heldByCurrent " + Thread.currentThread().getName() +
                            ": " + tableLocks[i].isHeldByCurrentThread());
            miscAttributeLock.unlock();
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() + "line432");
            tableLocks[i].unlock();
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", tL[" + i + "]: " + tableLocks[i].toString() + "line435");
            return e.getValue();
        } else {
            Entry<K,V> e_prev = table[i];
            Entry<K,V> e_curr = table[i].next;
            while(e_curr != null) {
                if(e_curr.getKey().equals(key)) {
                    e_prev.next = e_curr.next;
                    LOGGER.log(Level.INFO,
                            Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() + "line444");
                    miscAttributeLock.lock();
                    size--;
                    LOGGER.log(Level.INFO,
                            "tableLocks[" + i + "] heldByCurrent " + Thread.currentThread().getName() +
                                    ": " + tableLocks[i].isHeldByCurrentThread());
                    miscAttributeLock.unlock();
                    LOGGER.log(Level.INFO,
                            Thread.currentThread().getName() + ", Misc:" + miscAttributeLock.toString() + "line452");

                    tableLocks[i].unlock();
                    LOGGER.log(Level.INFO,
                            Thread.currentThread().getName() + ", tL[" + i + "]: " + tableLocks[i].toString() + "line456");
                    return e_curr.getValue();
                }
                e_prev = e_curr;
                e_curr = e_curr.next;
            }
            LOGGER.log(Level.INFO,
                    "tableLocks[" + i + "] heldByCurrent " + Thread.currentThread().getName() +
                            ": " + tableLocks[i].isHeldByCurrentThread());
            tableLocks[i].unlock();
            LOGGER.log(Level.INFO,
                    Thread.currentThread().getName() + ", tL[" + i + "]: " + tableLocks[i].toString() + "line467");
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
