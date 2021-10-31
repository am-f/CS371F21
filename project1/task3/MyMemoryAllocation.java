import BlockList.*;

public class MyMemoryAllocation extends MemoryAllocation {

   String operatingMode;
   UsedList usedList;
   FreeList freeList;
   int maxMemSize;

    
    public MyMemoryAllocation(int mem_size, String algorithm) {
        super(mem_size, algorithm); 

        freeList = new FreeList(mem_size);
        usedList = new UsedList(mem_size);
        operatingMode = algorithm;

        maxMemSize = mem_size;
        if(algorithm.equals("NF")) {
            freeList.iterator = freeList.iterator(true);
           
        }
        
    }
    
    public int alloc(int size) {
        if(size <= 0 || size >= maxMemSize) {
            System.err.println("invalid alloc");
            return 0;
        }
        
        if(this.operatingMode.equals("BF")) {
            freeList.iterator = freeList.iterator();
            Block current;
            int[] maxBlock = freeList.getMaxBlock();
            if(maxBlock[1] < size) {
                System.err.println("large enough block not available");
                return 0;
            }
            while(freeList.getHasNext(freeList.iterator)) {
                current = freeList.getNext(freeList.iterator);
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
            usedList.insert(maxBlock[0], size);
           
            freeList.shrinkOrDelete(maxBlock[0], size);
           
            return maxBlock[0];//return address of allocated block


        }
        if(this.operatingMode.equals("FF")){

            freeList.iterator = freeList.iterator();
            Block current;

            while(freeList.getHasNext(freeList.iterator)){
                current = freeList.getNext(freeList.iterator);
               
                if(current.getSize() >= size){
                    int blockOffset = current.getOffset();
                    usedList.insert(current.getOffset(), size);
                    freeList.shrinkOrDelete(current.getOffset(), size);

                    return blockOffset;//return address of allocated block
                }
            }
           
            System.err.println("error in FF");
            return 0;
        }
        if(this.operatingMode.equals("NF")) {
            Block nfCurrent;
            int blockCount = freeList.getBlockCount();
            int numBlocksChecked = 0;
            boolean done = false;

            if(blockCount == 1) {
                nfCurrent = freeList.getNext(freeList.iterator);
               
                int blockOffset = nfCurrent.getOffset();
                usedList.insert(nfCurrent.getOffset(), size);
                freeList.shrinkOrDelete(nfCurrent.getOffset(), size);
               
                return blockOffset;//return address of allocated block
            }
            if(freeList.getHasNext(freeList.iterator)) {
                do {
                    nfCurrent = freeList.getNext(freeList.iterator);
                    numBlocksChecked++;
                    if (nfCurrent.getSize() >= size) {
                        int blockOffset = nfCurrent.getOffset();
                        usedList.insert(nfCurrent.getOffset(), size);
                        freeList.shrinkOrDelete(nfCurrent.getOffset(), size);

                        return blockOffset;//return address of allocated block
                    }
                    
                    if(blockCount == numBlocksChecked) {
                        done = true;
                    }
                } while (!done);

            }

        }
        System.err.println("fail");
        return 0; //allocation failed
    }

    public void free(int offset) {
        if(offset <= 0 || offset >= maxMemSize) {
            System.err.println("invalid offset");
            return;
        }
        usedList.iterator = usedList.iterator();
        Block usedCurrent;
        
        while(usedList.getHasNext(usedList.iterator)){
           usedCurrent = usedList.getNext(usedList.iterator);
          
           if(usedCurrent.getOffset() == offset){
               freeList.insert(offset, usedCurrent.getSize());
               usedList.delete(offset);
               usedList.doRestart(usedList.iterator);
              
               return;

           }
        }
        System.err.println("invalid offset");

    }
    public int size() {
        return freeList.getTotalSize();
    }
    public int max_size() {
        return freeList.getMaxBlock()[1];
    }

    public void print() {
        System.out.print("free: ");
        freeList.print();
        System.out.print("used: ");
        usedList.print();

    }
}
