package BlockList;

//import java.util.Iterator;
public interface BlockIterator {
    public boolean hasNext();
    public Object next();



}
/*
class BlockList.BlockIterator implements Iterator<BlockList.Block>{
     BlockList.Block current;

    //initializes pointer to head of Blocklist for iteration 
    public BlockList.BlockIterator(BlockList.BlockList list){
         current = list.head;
    } 

    @Override
    //returns false if next element doesn't exist
    public boolean hasNext() {
        return current != null;
    }

    @Override
    //Returns current block and updates pointer
    public BlockList.Block next() {
        BlockList.Block temp = new BlockList.Block(current.getOffset(), current.getSize());
        current = current.getRight();
        return temp;
    }
}
*/
