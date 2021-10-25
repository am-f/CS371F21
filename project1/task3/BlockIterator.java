//import java.util.Iterator;
public interface BlockIterator {
    public boolean hasNext();
    public Object next();



}
/*
class BlockIterator implements Iterator<Block>{
     Block current; 

    //initializes pointer to head of Blocklist for iteration 
    public BlockIterator(BlockList list){
         current = list.head;
    } 

    @Override
    //returns false if next element doesn't exist
    public boolean hasNext() {
        return current != null;
    }

    @Override
    //Returns current block and updates pointer
    public Block next() {
        Block temp = new Block(current.getOffset(), current.getSize());
        current = current.getRight();
        return temp;
    }
}
*/
