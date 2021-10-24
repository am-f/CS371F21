
public class BlockList implements BlockContainer {

//Begin Fields

    Block head;
    Block tail;
    int maxSize;
    int totalSize;
    int blockCount;
    int memSize;

//End Fields



//Begin Constructors

    public BlockList() {
        
        this.head = null;
        this.tail = null;
        this.maxSize = 0;
        this.totalSize = 0;
        this.blockCount = 0;
        this.memSize = 0;
    }



    public BlockList(int sizeOfMemory) { //I don't think we want this constructor here, since
        // initializing with sizeOfMemory applies to FreeList only
        this.head = null;
        this.tail = null;
        this.maxSize = 0;
        this.totalSize = 0;
        this.blockCount = 0;
        this.memSize = sizeOfMemory;
    }
    
//End Constructors



//Begin Methods

    //Insert the block in the correct order
    //TODO: Enforce checking against max mem size
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
            if(current.getRightBoundary() < b.getOffset() && b.getRightBoundary() < this.memSize - 1){
                current.setRight(b);
                b.setLeft(current);
                
                this.tail = b;
                this.blockCount++;
                return true;
            }
            
            //If no previous case inserted the block
            return false;
            
        }
        
    };



    //Delete the block at the given address
    //TODO: Complete method
    public boolean delete(int address) {
        return true;
    };



    //Return all blocks with the given address
    //TODO: Complete method
    public BlockContainer searchByAddress(int address) {
        return new BlockList();
    };



    //Return all blocks with size greater than or equal to the given size
    //TODO: Complete method
    public BlockContainer searchBySize(int size) {
        return new BlockList();
    };



    //Return the total size of all blocks represented by the container
    public int getTotalSize() {
        return this.totalSize;
    };



    //Return the size of the single greatest block in the container
    public int getMaxSize() {
        return this.maxSize;
    };



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



    //Return true if there is at least one node
    //TODO: Complete method
    public boolean isEmpty(){
        if(this.head == null){
            return true;
        }
        else{
            return false;
        }
    };

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
