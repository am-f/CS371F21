package task3;

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
        this.memSize = -1;
    }



    public BlockList(int sizeOfMemory) {
        
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
    //TODO: Complete method
    public boolean insert(Block b) {
        return true;
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
    //TODO: Complete method
    public boolean calculateAdjacency(Block a, Block b) {
        return true;
    };



    //Return true if there is at least one node
    //TODO: Complete method
    public boolean isEmpty(){
        return true;
    };
    
//End Methods
}
