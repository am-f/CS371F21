public class OurMemoryAllocator extends MemoryAllocation {

    String operatingMode;
    UsedList used;
    FreeList free;
    //TODO:
    public OurMemoryAllocator(int mem_size, String algorithm) {
        super(mem_size, algorithm); //I'm not sure what this actually does since the constructor in super is empty,
        // but it gives me an error unless I put that there
        free = new FreeList(mem_size);
        used = new UsedList();
        operatingMode = algorithm;


    }
    //TODO:
    public int alloc(int size) {
        return -1;
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
