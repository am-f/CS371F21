package vm;

public class VirtMemory extends Memory {

    //so far this is basically just copied and pasted from Memory.java just so I don't have
    // red squiggles everywhere saying that VirtMemory doesn't implement the methods in Memory
    public VirtMemory() {
        super(null);
    }

    public void write(int addr, byte value){}

    /*
     * returns value that was stored at address addr
     */
    public byte read(int addr){
        return 0;
    }
    /*
     * flush back dirty pages to disk
     */
    protected void sync_to_disk(){}
}
