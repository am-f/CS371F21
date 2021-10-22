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
    boolean merge(Block a, Block b) {
        try { //is this how we want to implement "return false if merge failed" ?
            a.setSize(a.getSize() + b.getSize());
            a.setRight(b.getRight());
            b.getRight().setLeft(a);
            b.setLeft(null);
            b.setRight(null);
        } catch (Exception e) {
            System.err.println("ERROR: merge failed"); //I'm not sure if I did this right. I've
            // only ever used System.out, never System.err
            return false;
        }
        return true;

    }
    //TODO:
    boolean shrink(Block b, int newSize) {
        try { //is this how we want to implement "return false if shrink failed" ?
            b.setSize(newSize);
        } catch (Exception e) {
            System.err.println("ERROR: shrink failed"); //I'm not sure if I did this right. I've
            // only ever used System.out, never System.err
            return false;
        }
        return true;
    }
}
