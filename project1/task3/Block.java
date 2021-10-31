public class Block implements Comparable<Block> {
   
    private int offset;
    private int size;
    private Block left;
    private Block right;

    public Block(int offset, int size) {
        this.offset = offset;
        this.size = size;
        this.left = null;
        this.right = null;
    }
   
    public Block(int offset, int size, Block left, Block right) {
        this.offset = offset;
        this.size = size;
        this.left = left;
        this.right = left;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getOffset() {
        return offset;
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }

    //Marty:
    //The left boundary (first occupied memory slot) of a block is always its offset
    //The right boundary of a block (the final memory slot it occupies) will be (size - 1) blocks after the offset
   
    public int getRightBoundary(){
        return this.offset + this.size - 1;
    }

    public Block getLeft() {
        return left;
    }
    public void setLeft(Block left) { this.left = left; } 
    
    public Block getRight() {
        return right;
    }
    public void setRight(Block right) {
        this.right = right;
    } 

    
    //Marty:
    //Returns -1 if this block is a lower offset than the parameter block
    //Returns 0 if the blocks have the same offset
    //Returns 1 if this block has a greater offset than the passed block
    public int compareTo(Block b) {
        
        if(this.offset < b.getOffset()){
            return -1;
        }
        else if (this.offset > b.getOffset()){
            return 1;
        }
        else{
            return 0;
        }
        
    }
    public String toString() {
        return "[" + offset + "," + size + "]";
    }
}
