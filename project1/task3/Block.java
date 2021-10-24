package task3;

public class Block implements Comparable<Block> {
    //Note from Allison: I'm not sure if putting private in front of these attributes is the right/standard way to do this, but if we
    // keep the class public and don't modify variable access at all then the attributes will be public, which we don't want (I don't
    // think).  We also need to do something similar to any class currently declared public if we want private attributes in it
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
    public int compareTo(Block b) {
        return -1;
    }
    //TODO:
    public String toString() {
        return "Block: offset=" + offset + ", size=" + size;
    }
}
