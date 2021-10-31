public class UsedList extends BlockList {
    //private Block head;
     //private Block tail;
    // private int blockCount;
    // private int memSize;

    public UsedList(int sizeOfMemory) {
        setHead(null);
        setTail(null);
        setblockCount(0);
        setmemSize(sizeOfMemory);
        //head = null;
       // tail = null;
        //blockCount = 0;
       // memSize = sizeOfMemory;
        
    }

    public boolean insert(int offset, int size) {
        return super.insert(offset, size);
    }
    public boolean delete(int offset) {
        return super.delete(offset);
    }


}
