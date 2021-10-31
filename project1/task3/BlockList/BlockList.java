package BlockList;

public class BlockList /*implements BlockContainer*/ {

//Begin Fields

//If this is the parent list class, these fields should be public or free/used list must have their own instance variables
    protected Block head;
    protected Block tail;
    protected int blockCount;
    protected int memSize;
    public BlockListIterator iterator;

//End Fields


//Begin Constructors

    public BlockList() {
        head = null;
        tail = null;
        blockCount = 0;
        memSize = 0;
    }

    public BlockList(int sizeOfMemory) { //I don't think we want this constructor here, since
        // initializing with sizeOfMemory applies to BlockList.FreeList only
        head = null;
        tail = null;
        blockCount = 0;
        memSize = sizeOfMemory;
    }

//End Constructors

//Begin Methods

    //Insert the block in the correct order
    public boolean insert(int offset, int size) {

        Block b = new Block(offset, size);
        if(size >= memSize || size <= 0) {
            System.err.println("invalid size");
            return false;
        }

        //If the list is empty, insert the block as both head and tail
        if(head == null){
        
            head = b;
            tail = b;

            head.setLeft(null);
            head.setRight(null);

            blockCount++;
            return true;
        }
        else {
            Block current;
            current = head;

            //Handle case where new block is inserted at the front, as the new head
            if(b.getRightBoundary() < current.getOffset()){
                b.setRight(current);
                current.setLeft(b);

                head = b;
                blockCount++;
                return true;
            }

            //Handle the case where the new block is inserted between two existing blocks
            while (current.getRight() != null){
                if(current.getOffset() == offset) {
                    System.err.println("invalid insert");
                    return false;
                }
                if (current.getRightBoundary() < b.getOffset() && b.getRightBoundary() < current.getRight().getOffset()){
                    b.setLeft(current);
                    current.getRight().setLeft(b);
                    b.setRight(current.getRight());
                    current.setRight(b);

                    blockCount++;
                    return true;
                }
                current = current.getRight();
            }
            
            //Handle the case where the new block is inserted at the very end, as the new tail
            if(current.getRightBoundary() < b.getOffset() && b.getRightBoundary() <= memSize - 1){
                current.setRight(b);
                b.setLeft(current);
                
                tail = b;
                blockCount++;
                return true;
            }
            //If no previous case inserted the block
            System.err.println("failed insert");
            return false;
            
        }
        
    }

    //Deletes the block at the given offset
    public boolean delete(int offset) {
        return delete(searchByOffset(offset));
    }

    private boolean delete(Block b) {
        if(b == null) {
            System.err.println("block does not exist");
            return false;
        }
        if(b.getLeft() != null) {
            b.getLeft().setRight(b.getRight());//Make the block on the left point to the block
            // on the right
        }
        else {
            head = b.getRight();
        }
        if(b.getRight() != null) {
            b.getRight().setLeft(b.getLeft());//Make the block on the right point to the block
            // on the left
        }
        else {
            tail = b.getLeft();
        }
        blockCount--;
      
        b.setLeft(null);
        b.setRight(null);
        return true;
    }



    //Return block at given offset
    protected Block searchByOffset(int offset) {
        Block current = head;

        while(current != null){
            if (current.getOffset() == offset){
                return current;
            }
            current = current.getRight();
        }
        System.err.println("ERROR: block with offset " + offset + " does not exist");
        return null;
    }


    //Return all blocks with size greater than or equal to the given size
    //Completed by Marty

    protected BlockList searchBySize(int size) {
        BlockList returnList = new BlockList(/*this.memSize*/);

        Block current = head;

        while(current != null){
            if (current.getSize() >= size){
                returnList.insert(current.getOffset(), current.getSize());
            }
            current = current.getRight();
        }
        return returnList;
    }



    //Return the total size of all blocks represented by the container
    public int getTotalSize() {
        int total = 0;
        Block finger = head;
        while(finger != null) {
            total = total + finger.getSize();
            finger = finger.getRight();
        }
        return total;
    }


    //Return the offset and size of the single greatest block in the container in int array format:
    // {offset, size}
    public int[] getMaxBlock() {
        Block finger = head;
        int[] maxBlock = new int[2];
        while(finger != null) {
            if(finger.getSize() > maxBlock[1]) {
                maxBlock[0] = finger.getOffset();
                maxBlock[1] = finger.getSize();
            }
            finger = finger.getRight();
        }
        return maxBlock;
    }

    public int getBlockCount() {
        return blockCount;
    }
    public int getMemSize() {
        return memSize;
    }


    //Determine if the two blocks are adjacent based on their size and offset
    //Completed by Marty
    //Blocks are adjacent if the left edge of one block touches the right edge of another block
    //since we are working with integers, touching means a difference of 1 between them
    public boolean calculateAdjacency(Block a, Block b) {
        if(b.getOffset() - a.getRightBoundary() == 1){//if the left edge of b is 1 greater than the right edge of a
            return true;
        }
        else if (a.getOffset() - b.getRightBoundary() == 1){//if the left edge of a is 1 greater than the right edge of b
            return true;
        }
        else{
            return false;
        }
    }



    //Return true if there are no nodes, false if there is at least one node
    //Completed by Marty
    public boolean isEmpty(){
        if(head == null){
            return true;
        }
        else{
            return false;
        }
    }

    public BlockListIterator iterator() {
        return new BlockListIterator(this);
    }
    public BlockListIterator iterator(boolean looping) {
        return new BlockListIterator(this, true);
    }
    public Block getNext(BlockListIterator iter) {
        return iter.next();
    }
    public boolean getHasNext(BlockListIterator iter) {
        return iter.hasNext();
    }
    public void doRestart(BlockListIterator iter) {
        iter.restart();
    }


   private class BlockListIterator {
        Block current;
        boolean looping = false;

        //initializes pointer to head of Blocklist for iteration
        BlockListIterator(BlockList list){
            current = list.head;
        }
        BlockListIterator(BlockList list, boolean looping) {
            current = list.head;
            this.looping = looping;
        }
        void restart() {
            current = head;
        }

        //returns false if next element doesn't exist
        boolean hasNext() {
            if(looping) {
                if(blockCount != 0) {
                    return true;
                }
            }

            return current != null;
        }

        //Returns current block and updates pointer
        Block next() {

            if(looping) {
                if(current == null && blockCount != 0) {
                    current = head;
                }
            }
            else if(current == null) {
                System.err.println("end of list, no next block");
                return null;
            }

            Block temp = new Block(current.getOffset(), current.getSize());
            current = current.getRight();
            return temp;

        }
    }
   
    //Prints out every block in the list
    //Completed by Marty
    public void print(){

        Block current = head;

        System.out.print("{");

        while(current != null){
            System.out.print("[" + current.toString() + "]");

            current = current.getRight();

            if(current != null){
                System.out.print(", ");
            }
        }

        System.out.println("}");
    }
    
//End Methods
}
