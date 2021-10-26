public class OurMemoryAllocator extends MemoryAllocation {

    String operatingMode;
    UsedList used;
    FreeList free;
    int maxMemSize;
    //TODO:
    public OurMemoryAllocator(int mem_size, String algorithm) {
        super(mem_size, algorithm); //I'm not sure what this actually does since the constructor in super is empty,
        // but it gives me an error unless I put that there
        this.free = new FreeList(mem_size);
        this.used = new UsedList();
        this.operatingMode = algorithm;

        this.maxMemSize = mem_size;
    }
    
    //Partially completed by Marty
    //Partially completed by Allison
    //TODO: needs testing, definitely does not work as currently implemented
    public int alloc(int size) {
        BlockList.BlockListIterator iter = free.iterator(); 
        
        if(this.operatingMode.equals("FF")){

            // BlockList available = (BlockList) free.searchBySize(size);//Downcasting the BlockContainer to a BlockList

            // BlockList.BlockListIterator freeIter = (BlockList.BlockListIterator) available.iterator();
            //iterate
            // through the blocks with size >= passed size argument

            Block current;

            while(iter.hasNext()){
                current = iter.next();
                System.out.println("Considering the following: " + current.toString());

                if(current.getSize() >= size){
                    //new method either shrinks or deletes node depending on how much space needs allocating
                    free.shrinkOrDelete(current.getSize());//TODO: Does not currently work because the "current" returned by the iterator is a shallow copy, and we would only be shrinking the copy

                    used.insert(current.getOffset(), size);//Works assuming that the shrinking moves the left boundary of the block further to the right

                    return current.getOffset();//return address of allocated block
                }
            }
        }
        
        return 0; //allocation failed
    }
    //TODO:
    public void free(int addr) {
        BlockList.BlockListIterator iter = used.iterator(); 
        Block usedCurrent;

        while(iter.hasNext()){
           usedCurrent = iter.next();
           System.out.println("Considering the following: " + usedCurrent.toString());

           if(usedCurrent.getOffset() == addr){
                used.delete(addr);
                free.insert(addr, usedCurrent.getSize());
           }
        }

    }
    public int size() {
        return free.getTotalSize();
    }
    public int max_size() {
        return free.getMaxSize();
    }
    //TODO: later
    public void print() {

    }
}
