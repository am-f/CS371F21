package BlockList;

public class BlockList /*implements BlockContainer*/ {

//Begin Fields

    protected Block head;
    protected Block tail;
    //protected BlockList.Block maxBlock;
    //protected int totalSize;
    protected int blockCount;
    protected int memSize;

//End Fields


//Begin Constructors

    public BlockList() {
        
        this.head = null;
        this.tail = null;
        //this.maxBlock = null;
        //this.totalSize = 0;
        this.blockCount = 0;
        this.memSize = 0;
    }

    public BlockList(int sizeOfMemory) { //I don't think we want this constructor here, since
        // initializing with sizeOfMemory applies to BlockList.FreeList only
        this.head = null;
        this.tail = null;
        //this.maxBlock = null;
        //this.totalSize = 0;
        this.blockCount = 0;
        this.memSize = sizeOfMemory;
    }

//End Constructors

//Begin Methods


    //Insert the block in the correct order
    //TODO: Enforce checking against max mem size for the case where the block is inserted between two blocks
    //inre this ^ TODO: I think that's automatically taken care of because we check the
    // boundaries, and if it doesn't go beyond the boundaries then it can't go beyong max mem size
    //Partially completed by Marty

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
                    return true;
                }
                current = current.getRight();
            }
            
            //Handle the case where the new block is inserted at the very end, as the new tail
            int curRight = current.getRightBoundary();
            int bRight = b.getRightBoundary();
            if(current.getRightBoundary() < b.getOffset() && b.getRightBoundary() <= this.memSize - 1){
                current.setRight(b);
                b.setLeft(current);
                
                this.tail = b;
                this.blockCount++;
                return true;
            }
            //If no previous case inserted the block
            return false;
            
        }
        
    }



    //Deletes the block at the given offset
    //Completed by Marty
    public boolean delete(int offset) {
        return delete(searchByOffset(offset));
    }
    protected boolean delete(Block b) {
        if(b == null) {
            return false;
        }
        if(b.getLeft() != null) {
            b.getLeft().setRight(b.getRight());//Make the block on the left point to the block
            // on the right
        }
        if(b.getRight() != null) {
            b.getRight().setLeft(b.getLeft());//Make the block on the right point to the block
            // on the left
        }
        this.blockCount--;
        if(blockCount == 1) {
            return true;
        }
        return true;
    }



    //Return block at given offset
    //Completed by Marty
    protected Block searchByOffset(int offset) {
        Block current = this.head;

        while(current != null){
            if (current.getOffset() == offset){
                return current;
            }
            current = current.getRight();
        }
        System.err.print("ERROR: block with offset " + offset + " does not exist");
        return null;
    }



    //Return all blocks with size greater than or equal to the given size
    //Completed by Marty
    protected BlockList searchBySize(int size) {
        BlockList returnList = new BlockList(/*this.memSize*/);

        Block current = this.head;

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
            System.out.println(total);
            finger = finger.getRight();
        }
        return total;
    };



    //Return the size of the single greatest block in the container
    public int getMaxSize() {
        int max = 0;
        Block finger = head;
        while(finger != null) {
            if(finger.getSize() > max) {
                max = finger.getSize();
            }
            finger = finger.getRight();
        }
        return max;
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
    };



    //Return true if there are no nodes, false if there is at least one node
    //Completed by Marty
    public boolean isEmpty(){
        if(this.head == null){
            return true;
        }
        else{
            return false;
        }
    }

    public BlockListIterator iterator() {
        return new BlockListIterator(this);
    }

    public class BlockListIterator implements BlockIterator {
        Block current;

        //initializes pointer to head of Blocklist for iteration
        public BlockListIterator(BlockList list){
            current = list.head;
        }

        //returns false if next element doesn't exist
        public boolean hasNext() {
            return current != null;
        }

        //Returns current block and updates pointer
        public Block next() {
            Block temp = new Block(current.getOffset(), current.getSize());
            current = current.getRight();
            return temp;
        }
    }

    /*
    @Override
    //returns instance of an iterator
    public Iterator<BlockList.Block> iterator() {
        
        return new BlockList.BlockIterator(this);
    };
    */


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
