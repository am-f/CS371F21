package framework;
import utils.BoundedBuffer;
import java.util.LinkedList;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
public class MyMapReduce extends MapReduce {
	//TODO: your code here. Define all attributes 
	//What is in a running instance of MapReduce?
	private PartitionTable pTable;
	private ConcurrentKVStore kvStore;
	private MapperReducerClientAPI client;
	private int numMappers;
	private int numReducers;
	private Lock lock = new ReentrantLock(); //currently very coarse granularity
	private boolean allMappersDone = false;
	private int numMappersDone = 0;
	private Thread[] mappers;
	private Thread[] reducers;



	public void MREmit(Object key, Object value) //KV from File-->PartitionTable
	{
		//TODO: your code here. Delete UnsupportedOperationException after your implementation is done.

		int pNum = (int)client.Partitioner(key, numMappers);
		//pTable.partitions[pNum].deposit(key, value);
		

		/*From assignment doc:
		The MREmit() function is thus another key part of your class; it needs to take key/value
		pairs from the many different mappers and store them in a way that later reducers can
		access them, given constraints described below. Designing and implementing this data
		structure is thus a central challenge of the project: we will use  bounded buffer we
		learned in class to transfer data between mappers and reducers.
		 */
		throw new UnsupportedOperationException();
	}
	private LinkedList intermediateReduce(PartitionTable.Partition partition) {
		LinkedList<Object> uniqueKeys = new LinkedList();
		/*
		PartitionTable.Partition.kvPair kv;
		while(((kv = partition.fetch()) && kv.key != null)
			kvStore.add(kvPair);
			if(uniqueKeys.contains(kv.key) {
				uniqueKeys.add(kv.key);
		}
		*/
		return uniqueKeys;

	}

	public Object MRGetNext(Object key, int partition_number) {
		//TODO: your code here. Delete UnsupportedOperationException after your implementation is done.
		/*From assignment doc:
		After the mappers are finished, your class should have stored the key/value pairs in such a
		way that the Reduce() function can be called.Reduce() is invoked once per key and is passed
		the key along with a function that enables iteration over all of the values that produced
		that same key. To iterate, the code just calls MRGetNext() repeatedly until a NULL value is
		returned;  MRGetNext() returns a value object passed in by the MREmit() function above, or
		NULL when the key's values have been processed.
		 */
		throw new UnsupportedOperationException();
	}
	@Override
	protected void MRRunHelper(String inputFileName,
		    		  MapperReducerClientAPI mapperReducerObj,
		    		  int num_mappers, 
		    		  int num_reducers)
	{
		//TODO: your code here. Delete UnsupportedOperationException after your implementation is done.

		//our control flow?
		//instantiates stuff
		pTable = new PartitionTable(num_mappers);
		kvStore = new ConcurrentKVStore();
		client = mapperReducerObj;
		client.Map(inputFileName);
		numMappers = num_mappers;
		numReducers = num_reducers;
		// creates and calls threads
		//steps:
			//create i mapper threads which do function mapperReducerObj.Map(pw[0...i])
				//after a mapper thread calls Map(pw[i]); call MREmit(null, null);
			//create i reducer threads which do internal function reduce(pt[0...i]) that moves
				// KVs from pt[i]-->ConcurrentStore then calls user-defined Reduce(key, i) for each
				// key in pt[i]


		/*
			Map(file)-->{while (token=file.nextLine)!=null->MREmit(token, "1")}

			Reduce(key, pNum)-->count=0-->{while MRGetNext(key, pNum) != null)
			-->count++-->stats[pNum]++}-->MRPostProcess(key, count, pNum)
		 */
		throw new UnsupportedOperationException();
	}


	private class Mapper implements Runnable {
		Mapper(String inputSource) {
			/**/
			run();
		}
		@Override
		public void run() {
			while(true/*   */) {
				//Map(whatever);
				//then:
				lock.lock();
				numMappersDone++;
				if(numMappersDone==numMappers) {
					//for each pTable: pTable[0.....i].deposit(null, null)
					allMappersDone = true;
				}
				lock.unlock();
			}
		}
	}
	private class Reducer implements Runnable {
		private PartitionTable.Partition partition;

		Reducer(PartitionTable.Partition p) {
			this.partition = p;
			run();
		}
		@Override
		public void run() {
			while(true/*   */) {
				//do intermediate reduce:
				//LinkedList<Object> uniqueKeys = intermediateReduce(partition);
				//intermediateReduce will not return until all mappers are done
				//do user reduce for each unique key in pTable

			}

		}
	}
}
