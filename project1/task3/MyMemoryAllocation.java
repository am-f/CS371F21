public class MyMemoryAllocation extends MemoryAllocation {

   private String operatingMode;
   private UsedList used;
   private FreeList free;
   private int maxMemSize;
   private BlockList.BlockListIterator iter;
    
    public MyMemoryAllocation(int mem_size, String algorithm) {
        super(mem_size, algorithm); 

        this.free = new FreeList(mem_size);
        this.used = new UsedList(mem_size);
        this.operatingMode = algorithm;

        this.maxMemSize = mem_size;
        if(algorithm.equals("NF")) {
            iter = free.iterator(true);
           
        }
        
    }
    
    public int alloc(int size) {
        if(size <= 0 || size >= maxMemSize) {
            System.err.println("invalid alloc");
            return 0;
        }
        
        if(this.operatingMode.equals("BF")) {

            iter = free.iterator();
            Block current;
            int[] maxBlock = free.getMaxBlock();
            if(maxBlock[1] < size) {
                System.err.println("large enough block not available");
               
                return 0;
            }
            while(iter.hasNext()) {
                current = iter.next();
                int curSize = current.getSize();
                int curOffset = current.getOffset();

                if(curSize < size) {
                    continue;
                }

                if(curSize - size == maxBlock[1] - size) {
                    if(curOffset < maxBlock[0]) {
                        maxBlock[0] = curOffset;
                        maxBlock[1] = curSize;
                    }
                }
                else if(curSize - size < maxBlock[1] - size) {
                    maxBlock[0] = curOffset;
                    maxBlock[1] = curSize;
                }
            }
            used.insert(maxBlock[0], size);
           
            free.shrinkOrDelete(maxBlock[0], size);
           
            return maxBlock[0];//return address of allocated block


        }
        if(this.operatingMode.equals("FF")){

            iter = free.iterator();
            Block current;

            while(iter.hasNext()){
                current = iter.next();
               
                if(current.getSize() >= size){
                    int blockOffset = current.getOffset();
                    used.insert(current.getOffset(), size);
                    free.shrinkOrDelete(current.getOffset(), size);

                    return blockOffset;//return address of allocated block
                }
            }
           
            System.err.println("error in FF");
        }
        if(this.operatingMode.equals("NF")) {
            Block nfCurrent;
            int blockCount = free.getBlockCount();
            int numBlocksChecked = 0;
            boolean done = false;
            
            if(blockCount == 0) {
                System.out.println("bc=0, line 90");
            }

            if(blockCount == 1) {
                System.out.println("bc=1, line 94");
                nfCurrent = iter.next();
               
                int blockOffset = nfCurrent.getOffset();
                used.insert(nfCurrent.getOffset(), size);
                free.shrinkOrDelete(nfCurrent.getOffset(), size);
               
                return blockOffset;//return address of allocated block
            }
            if(iter.hasNext()) {
                System.out.println("nfIter.hasNext(), line 106");
                do {
                    System.out.println("inside do while, line 108");
                    nfCurrent = iter.next();
                    System.out.println("blockCount: " + blockCount + "\t numChecked: " + numBlocksChecked);
                    
                    numBlocksChecked++;
                    if (nfCurrent.getSize() >= size) {
                        System.out.println("found spot, line 114");
                        int blockOffset = nfCurrent.getOffset();
                        used.insert(nfCurrent.getOffset(), size);
                        free.shrinkOrDelete(nfCurrent.getOffset(), size);


                        return blockOffset;//return address of allocated block
                    }
                    System.out.println("line 124");
                    
                    if(blockCount == numBlocksChecked) {
                        System.out.println("done=true, line 131");
                        done = true;
                    }
                    System.out.println("right before while ends, lie 134");
                } while (!done);
                System.out.println("line 136");

            }
            System.out.println("line 139");

        }
        System.err.println("fail");
        return 0; //allocation failed
    }

    public void free(int offset) {
        if(offset <= 0 || offset >= maxMemSize) {
            System.err.println("invalid offset");
            return;
        }
        BlockList.BlockListIterator usedIter = used.iterator();
        Block usedCurrent;
        
        while(usedIter.hasNext()){
           usedCurrent = usedIter.next();
          
           if(usedCurrent.getOffset() == offset){
               free.insert(offset, usedCurrent.getSize());
               used.delete(offset);
               usedIter.restart();
              
               return;

           }
        }
        System.err.println("invalid offset");

    }
    public int size() {
        return free.getTotalSize();
    }
    public int max_size() {
        return free.getMaxBlock()[1];
    }

    public void print() {
        System.out.print("free: ");
        free.print();
        System.out.print("used: ");
        used.print();

    }
}
