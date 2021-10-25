import BlockList.*;

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
        BlockList.BlockListIterator iter = free.iterator(); //WHY ERROR?

        if(this.operatingMode.equals("FF")){

            Block freeFFBlock;
            int FFSize = 0;
            while(FFSize < size) {
                //NOT DONE
            }



            //BlockList.BlockList available = (BlockList.BlockList) free.searchBySize(size);//Downcasting the
            // BlockList.BlockContainer to a BlockList.BlockList

            //BlockList.BlockList.BlockListIterator iter = (BlockList.BlockList.BlockListIterator) available.iterator();
            //iterate
            // through the blocks with size >= passed size argument

            Block current;

            while(iter.hasNext()){
                current = iter.next();
                System.out.println("Considering the following: " + current.toString());

                if(current.getSize() >= size){
                    free.shrinkBy(current, size);//TODO: Does not currently work because the "current" returned by the iterator is a shallow copy, and we would only be shrinking the copy

                    used.insert(current.getOffset(), size);//Works assuming that the shrinking moves the left boundary of the block further to the right

                    return current.getOffset();//return address of allocated block
                }
            }
        }
        
        return 0; //allocation failed
    }
    //TODO:
    public void free(int addr) {

    }
    public int size() {
        return free.getTotalSize();
    }
    public int max_size() {
        return free.getMaxSize();
    }
    //TODO:
    public void print() {

    }
}
