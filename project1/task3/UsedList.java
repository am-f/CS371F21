public class UsedList extends BlockList {

    //private Block head;
    //private Block tail;
    //protected Block maxBlock;
    //protected int totalSize;
    //protected int blockCount;
    //protected int memSize;
    public UsedList(int sizeOfMemory) {
        head = null;
        tail = null;
        //totalSize = initialSize - 1;
        blockCount = 0;
        memSize = sizeOfMemory;
        //super(sizeOfMemory);
        //head = null;
        //tail = null;
        //maxSize = 0;
        //totalSize = 0;
        //blockCount = 0;
        //does used need memSize? Or just free?
        //also do we actually need maxSize and totalSize for usedlist? would it be the largest block of used memory
        // and amount of total memory used??
        //JYM: Imaging a scenario you just initialize your MemoryAllocation. You have a big contiguous space of 1 to 100 bytes. 
        // If you are using Block [1,100] to represent it, then it is essentially constructing a node, and inserting it into
        // list. That is it. For list it is just a block node. Why does list have to know totalSize or maxSize?
    }

    public boolean insert(int offset, int size) {
        return super.insert(offset, size);
    }
    public boolean delete(int offset) {
        return super.delete(offset);
    }


}
