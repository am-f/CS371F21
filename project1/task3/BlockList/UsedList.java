package BlockList;

public class UsedList extends BlockList {
    public UsedList(int sizeOfMemory) {
        head = null;
        tail = null;
        blockCount = 0;
        memSize = sizeOfMemory;
    }
}
