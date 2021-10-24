public class UsedList extends BlockList {
    public UsedList() {
        head = null;
        tail = null;
        maxSize = 0;
        totalSize = 0;
        blockCount = 0;
        //does used need memSize? Or just free?
        //also do we actually need maxSize and totalSize for usedlist? would it be the largest block of used memory
        // and amount of total memory used??
    }
    
}
