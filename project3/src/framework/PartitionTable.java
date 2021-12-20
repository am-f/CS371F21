package framework;
import utils.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import framework.PartitionTable.Partition.kvPair;

public class PartitionTable {

    Partition[] partitions;

    PartitionTable(int numMappers) {
        partitions = new Partition[numMappers];
        for(int i = 0; i < numMappers; i++) {
            partitions[i] = new Partition();
        }
    }


  class Partition<kvPair> extends BoundedBuffer {
        //TODO: your code here
       class kvPair {
            Object key;
            Object value;
        }
        Partition() {
            super(50);
        }
        //Notes:
        // (1) each partition works like a bounded buffer between
        // mappers and a reducer. (you can assume size = 10 or 50)
        // (2) if reducer_i wants to fetch a KV pair it can
        // only fetches from partition_i, but mapper_i can drop messages
        // into different partitions.

        //Create overriden deposit and fetch methods from buffer
    }

}
