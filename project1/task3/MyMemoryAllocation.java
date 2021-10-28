public class MyMemoryAllocation extends MemoryAllocation {

    String operatingMode;
    UsedList used;
    FreeList free;
    int maxMemSize;
    BlockList.BlockListIterator nfIter;
    //TODO:
    public MyMemoryAllocation(int mem_size, String algorithm) {
        super(mem_size, algorithm); //I'm not sure what this actually does since the constructor in super is empty,
        // but it gives me an error unless I put that there
        this.free = new FreeList(mem_size);
        this.used = new UsedList(mem_size);
        this.operatingMode = algorithm;

        this.maxMemSize = mem_size;
        nfIter = free.iterator();
    }
    
    //Partially completed by Marty
    //Partially completed by Allison
    //TODO: needs testing, definitely does not work as currently implemented
    public int alloc(int size) {
        BlockList.BlockListIterator iter = free.iterator();



        if(this.operatingMode.equals("BF")) {
            Block current;
            int[] maxBlock = free.getMaxBlock();
            if(maxBlock[1] < size) {
                //TODO: system error print
                return 0;
            }
            while(iter.hasNext()) {
                current = iter.next();
                int curSize = current.getSize();
                int curOffset = current.getOffset();
                if(curSize - size < maxBlock[1] - size) {
                    maxBlock[0] = curOffset;
                    maxBlock[1] = curSize;
                }
                else if(curSize - size == maxBlock[1] - size) {
                    if(curOffset < maxBlock[0]) {
                        maxBlock[0] = curOffset;
                        maxBlock[1] = curSize;
                    }
                }
            }
            used.insert(maxBlock[0], size);//Works assuming that the shrinking moves the left
            // boundary
            // of the block further to the right
            //new method either shrinks or deletes node depending on how much space needs allocating
            free.shrinkOrDelete(maxBlock[0], size);


            return maxBlock[0];//return address of allocated block


        }
        if(this.operatingMode.equals("FF")){

            Block current;

            while(iter.hasNext()){
                current = iter.next();
                //System.out.println("Considering the following: " + current.toString());

                if(current.getSize() >= size){
                    int blockOffset = current.getOffset();
                    used.insert(current.getOffset(), size);//Works assuming that the shrinking moves the left boundary of the block further to the right
                    //new method either shrinks or deletes node depending on how much space needs allocating
                    free.shrinkOrDelete(current.getOffset(), size);


                    return blockOffset;//return address of allocated block
                }
            }
        }

        if(this.operatingMode.equals("NF")) {
            Block current = nfIter.next();
            int blockCount = free.getBlockCount();
            int numBlocksChecked = 0;
            boolean done = false;
            if(blockCount == 1) {
                int blockOffset = current.getOffset();
                used.insert(current.getOffset(), size);//Works assuming that the shrinking moves the left boundary of the block further to the right
                //new method either shrinks or deletes node depending on how much space needs allocating
                free.shrinkOrDelete(current.getOffset(), size);
                return blockOffset;//return address of allocated block
            }
            if(nfIter.hasNext()) {
                do {
                    System.out.println("blockCount: " + blockCount + "\t numChecked: " + numBlocksChecked);
                    //System.out.println("Considering the following: " + current.toString());
                    numBlocksChecked++;
                    if (current.getSize() >= size) {
                        int blockOffset = current.getOffset();
                        used.insert(current.getOffset(), size);//Works assuming that the shrinking moves the left boundary of the block further to the right
                        //new method either shrinks or deletes node depending on how much space needs allocating
                        free.shrinkOrDelete(current.getOffset(), size);


                        return blockOffset;//return address of allocated block
                    }

                    if(!nfIter.hasNext() && numBlocksChecked != blockCount) {
                        nfIter = free.iterator();
                    }
                    current = nfIter.next();
                    if(blockCount == numBlocksChecked) {
                        done = true;
                    }
                } while (!done);

            }





        }
        
        return 0; //allocation failed
    }
    //TODO:
    public void free(int offset) {
        BlockList.BlockListIterator iter = used.iterator(); 
        Block usedCurrent;

        while(iter.hasNext()){
           usedCurrent = iter.next();
           //System.out.println("Considering the following: " + usedCurrent.toString());
           if(usedCurrent.getOffset() == offset){
               free.insert(offset, usedCurrent.getSize());
               used.delete(offset);

           }
        }

    }
    public int size() {
        return free.getTotalSize();
    }
    public int max_size() {
        return free.getMaxBlock()[1];
    }
    //TODO: later
    public void print() {

    }
}
