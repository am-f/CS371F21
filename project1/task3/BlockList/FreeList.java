package BlockList;

public class FreeList extends BlockList {

    public FreeList(int initialSize) {
        head = new Block(1, initialSize - 1);
        tail = head;
        //totalSize = initialSize - 1;
        blockCount = 1;
        memSize = initialSize;
    }

    public boolean merge(Block a, Block b) {
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
            if(r.getRight() != null) {
                r.getRight().setLeft(l);
            }
            r.setLeft(null);
            r.setRight(null);
            if(tail == r) {
                tail = l;
            }
            blockCount--;
        } catch (Exception e) {
            System.err.println("ERROR: merge failed"); //I'm not sure if I did this right. I've
            // only ever used System.out, never System.err
            return false;
        }
        return true;

    }

    //TODO: Will not work with iterator, as we would be shrinking iterator's shallow copy of the block rather than the list's copy of the block
    public boolean shrinkBy(Block b, int shrinkByVal) { //I changed the method so the argument is how
        // much memory we want to remove. This effectively splits the memory into two blocks and
        // removes the first one
        try { //is this how we want to implement "return false if shrink failed" ?
            Block a = searchByOffset(b.getOffset());
            a.setOffset(a.getOffset()+shrinkByVal);
            a.setSize(a.getSize()-shrinkByVal);
        } catch (Exception e) {
            System.err.println("ERROR: shrink failed"); //I'm not sure if I did this right. I've
            // only ever used System.out, never System.err
            return false;
        }
        return true;
    }

    @Override
    public boolean insert(int offset, int size) {

        Block b = new Block(offset, size);

        //If the list is empty, insert the block as both head and tail
        if(this.head == null){

            this.head = b;
            this.tail = b;

            this.head.setLeft(null);
            this.head.setRight(null);

            this.blockCount++;
            return true;
        }
        else {
            Block current;
            current = this.head;

            //Handle case where new block is inserted at the front, as the new head
            if(b.getRightBoundary() < current.getOffset()){
                b.setRight(current);
                current.setLeft(b);

                this.head = b;
                this.blockCount++;
                if(calculateAdjacency(b, current)) {
                    merge(b, current);
                }
                return true;
            }

            //Handle the case where the new block is inserted between two existing blocks
            while (current.getRight() != null){

                if (current.getRightBoundary() < b.getOffset() && b.getRightBoundary() < current.getRight().getOffset()){
                    b.setLeft(current);
                    current.getRight().setLeft(b);
                    b.setRight(current.getRight());
                    current.setRight(b);

                    this.blockCount++;
                    if(calculateAdjacency(b, b.getRight())) {
                        merge(b, b.getRight());
                    }
                    if(calculateAdjacency(b, b.getLeft())) {
                        merge(b, b.getLeft());
                    }
                    return true;
                }
                current = current.getRight();
            }

            //Handle the case where the new block is inserted at the very end, as the new tail
            if(current.getRightBoundary() < b.getOffset() && b.getRightBoundary() < this.memSize - 1){
                current.setRight(b);
                b.setLeft(current);

                this.tail = b;
                this.blockCount++;
                if(calculateAdjacency(b, b.getLeft())) {
                    merge(b, b.getLeft());
                }
                return true;
            }
            //If no previous case inserted the block
            return false;

        }

    }


    @Override
    public boolean delete(int offset) {
        Block current = this.head;

        while(current != null){ //Allison: we could implement this using searchByOffset instead
            // of this loop if we wanted
            if(current.getOffset() == offset){
                current.getLeft().setRight(current.getRight());//Make the block on the left point to the block on the right
                current.getRight().setLeft(current.getLeft());//Make the block on the right point to the block on the left
                this.blockCount--;
                if(blockCount == 1) {
                    return true;
                }
                if(current.getLeft() != null && current.getRight() != null) {
                    if(calculateAdjacency(current.getLeft(), current.getRight())) {
                        merge(current.getLeft(), current.getRight());
                    }
                }
                return true;
            }
            current = current.getRight();
        }
        return false; //if no block was found during the while loop
    }

}
