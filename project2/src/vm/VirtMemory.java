package vm;

public class VirtMemory extends Memory {

    protected int writeCount; //if writecount > 32, sync_to_disk
    protected MyPageTable pt;
    protected int PAGE_SIZE = 64;
    protected Policy frameTracking;


    //default constructor, should create instance of VirtMemory w/ 64KB virtual memory and 16kb
    // physical memory
    public VirtMemory() {

        super(null);
    }
    //other constructor, should create instance of VirtMemory with specified size and instance of
    // PhysMem with specified size
    public VirtMemory(int virtSize, int phySize) {


        super(null);
    }

/*
    protected int virtBlock(int va) {
        return va / PAGE_SIZE;
    }

 */

    protected int vaToPaAddressTranslation(int va) { //address translation
        int vpn = va / PAGE_SIZE;
        int offset = va % PAGE_SIZE;
        int pfn = pt.getPFN(vpn);
        int pa = pfn+offset;

        return pa;
    }
    protected int paToVaAddressTranslation(int pa) {

        return -1;
    }


    //writes specified value into memory at specified virtual address
    public void write(int addr, byte value){
        //find hash for address
        //search bucket[hash] for correct vpn (address)
        //if PTE is found: convert vpn to pfn, ram.write(pfn, value)
        //if PTE is not found: Page Fault! Upon return from handling page fault, convert vpn to
        // pfn, ram.write(pfn, value)
        //pte.dirty = true
        //writeCount++
        //if writeCount > 32, sync_to_disk();

    }

    //returns value that was stored at address addr
    public byte read(int addr){
        //find hash for address
        //search bucket[hash] for correct vpn (address)
        //if PTE is found: convert vpn to pfn, return ram.read(pfn)
        //if PTE is not found: Page fault! Upon return from handling page fault, convert vpn to
        // pfn, return ram.read(pfn)

        return 0;
    }
    //flush back dirty pages to disk
    protected void sync_to_disk(){
        //iterate through each PTE in buckets
        //for each PTE, if dirty==true, then get block number of page, ram.store(blockNum,
        // startAddress)
    }

    protected void loadPage(int vpn) {
        //load from disk to ram
        //add to page table
        //add to usedFrames

    }
    protected void evictPage(int vpn) {
        //if dirty==true, store to disk
        //remove from page table
        //remove from usedFrames
    }
    protected void handlePageFault(int va) {
        //evict first frame in usedFrames
        //load page containing va into ram
        //add to page table
    }
}
