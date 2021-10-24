
public interface BlockContainer {
/*This interface is intended to represent the methods the OurMemoryAllocator will use,
 so that we can later upgrade the LinkedList implementation to a Tree-based implementation
 
 @author Marty Roger, Betsy Gorelik, and Allison Fisher
 */     

 //Begin Methods

    //Insert the block in the correct order
    boolean insert(int offset, int size);

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
    //Question: do we mean adjacent like the two blocks touch each other in memory (i.e. block a
    // goes from address 1 to address 5, block b goes from address 5 to address 10),
    // or is it if the blocks are next to each other in the list regardless of if the memory
    // itself is adjacent (i.e. block a goes from address 1 to address 5, block b goes from
    // address 20 to address 30, but a.right=b and b.left=a)

    //Answer (Marty): I meant the former, but the list ordering should enforce the latter being true if the former is true
    // JYM: adjacency means contiguous address space, not location in the list.
    boolean calculateAdjacency(Block a, Block b);

    //Return false if there is at least one node
    //Question: wouldn't we return false if there's >=1 node? and return true if it's empty (0
    // nodes)?

    //Answer (Marty): Yep. Flubbed that one
    //JYM: what do you do with just 1 node?
    boolean isEmpty();
    
//End Methods
}
