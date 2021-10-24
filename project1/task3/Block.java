
public class Block implements Comparable<Block> {
    //Note from Allison: I'm not sure if putting private in front of these attributes is the right/standard way to do this, but if we
    // keep the class public and don't modify variable access at all then the attributes will be public, which we don't want (I don't
    // think).  We also need to do something similar to any class currently declared public if we want private attributes in it

    //Marty: Professor Jun said that it is correct to decalre them private, we need to have a specific justification to declare them as public
    //JYM: yes. unless you define Block as protected/package level access, by default, always private attributes
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
    //Allison: I added this second constructor so we can initialize all the values in a block if
    // we already know them, and we can use the getters and setters later if we need to change
    // values
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
    public Block getLeft() {
        return left;
    }
    public void setLeft(Block left) { this.left = left; } //added this (not in uml) --> now in uml
    
    public Block getRight() {
        return right;
    }
    public void setRight(Block right) {
        this.right = right;
    } //added this (not in uml) --> now in uml

    //TODO:
    //Question: what are we comparing by?

    //Answer (Marty): The ordering of the list, which I believe we agreed was by offset

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
        return "Block: offset=" + offset + ", size=" + size;
    }
}
