
public class FreeList extends BlockList {

    public FreeList(int initialSize) {
        head = new Block(1, initialSize - 1);
        tail = head;
        maxSize = initialSize - 1;
        totalSize = initialSize - 1;
        blockCount = 1;
        memSize = initialSize;
    }

    boolean merge(Block a, Block b) {
        Block l;
        Block r;
        if(a.getOffset() < b.getOffset()) { //so it still works if blocks are entered out of order
            l = a;
            r = b;
        } else {
            l = b;
            r = a;
        }
        try { //is this how we want to implement "return false if merge failed" ?
            l.setSize(l.getSize() + r.getSize());
            l.setRight(r.getRight());
            r.getRight().setLeft(l);
            r.setLeft(null);
            r.setRight(null);
            if(tail == r) {
                tail = l;
            }
        } catch (Exception e) {
            System.err.println("ERROR: merge failed"); //I'm not sure if I did this right. I've
            // only ever used System.out, never System.err
            return false;
        }
        return true;

    }
    boolean shrinkBy(Block b, int shrinkByVal) { //I changed the method so the argument is how
        // much memory we want to remove. This effectively splits the memory into two blocks and
        // removes the first one
        try { //is this how we want to implement "return false if shrink failed" ?
            b.setOffset(b.getOffset()+shrinkByVal);
            b.setSize(b.getSize()-shrinkByVal);
        } catch (Exception e) {
            System.err.println("ERROR: shrink failed"); //I'm not sure if I did this right. I've
            // only ever used System.out, never System.err
            return false;
        }
        return true;
    }
}
