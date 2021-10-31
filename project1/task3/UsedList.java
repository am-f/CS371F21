public class UsedList extends BlockList {
    
    public UsedList(int sizeOfMemory) {
        setHead(null);
        setTail(null);
        setblockCount(0);
        setmemSize(sizeOfMemory);
        
    }

    public boolean insert(int offset, int size) {
        return super.insert(offset, size);
    }
    public boolean delete(int offset) {
        return super.delete(offset);
    }


}
