public interface BlockContainer {
/*This interface is intended to represent the methods the OurMemoryAllocator will use,
 so that we can later upgrade the LinkedList implementation to a Tree-based implementation
 
 @author Marty Roger, Betsy Gorelik, and Allison Fisher
 */     

 //Begin Methods

    //Insert the block in the correct order
    boolean insert(Block b);

    //Delete the block at the given address
    boolean delete(int address);

    //Return all blocks with the given address
    BlockContainer searchByAddress(int address);

    //Return all blocks with size greater than or equal to the given size
    BlockContainer searchBySize(int size);

    //Return the total size of all blocks represented by the container
    int getTotalSize();

    //Return the size of the single greatest block in the container
    int getMaxSize();

    //Determine if the two blocks are adjacent based on their size and offset
    boolean calculateAdjacency(Block a, Block b);

    //Return true if there is at least one node
    boolean isEmpty();
    
//End Methods
}
