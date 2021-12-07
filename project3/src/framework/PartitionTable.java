package framework;
import utils.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PartitionTable<kvPair> extends BoundedBuffer {
	//TODO: your codde here
    private class kvPair {
        Object key;
        Object value;
    }
    public PartitionTable() {
        super(50);
    }
    public PartitionTable(int capacity) {
        super(capacity);
    }
    void deposit(Object key, Object value) {

    }
    Object fetch() {
        return null;
    }

	//Notes:
	// (1) each partition works like a bounded buffer between
	// mappers and a reducer. (you can assume size = 10 or 50)
	// (2) if reducer_i wants to fetch a KV pair it can
	// only fetches from partition_i, but mapper_i can drop messages
	// into different partitions.
    

}

