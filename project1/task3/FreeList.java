package task3;

public class FreeList extends BlockList {

    public FreeList(int initialSize) {
        head = new Block(1, initialSize - 1);
        tail = null;
        maxSize = initialSize - 1;
        totalSize = initialSize - 1;
        blockCount = 1;
        memSize = initialSize;
    }
    //TODO:
    boolean merge(Block a, Block b) {
        return false;
    }
    //TODO:
    boolean shrink(Block b, int newSize) {
        return false;
    }
}
