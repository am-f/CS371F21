package vm;
import storage.PhyMemory;

public class VirtMemory extends Memory {

    protected int writeCount; //if writecount > 32, sync_to_disk
    protected MyPageTable vpnPT;
    protected MyPageTable pfnPT;
    //protected int pfnVpnMapping[];
    protected int PAGE_SIZE = 64;
    protected Policy frameTracking;
    protected int virtMemSize; //TODO: invalid address exception if va>=virtMemSize
    protected int physMemSize;

    //default constructor, should create instance of VirtMemory w/ 64KB virtual memory and 16kb
    // physical memory
    public VirtMemory() {
        this(64 * 1024, 16 * 1024);
    }
    //other constructor, should create instance of VirtMemory with specified size and instance of
    // PhysMem with specified size
    public VirtMemory(int virtSize, int phySize) {
        super(new PhyMemory(phySize));
        physMemSize = phySize;
        virtMemSize = virtSize;
        writeCount = 0;
        vpnPT = new MyPageTable(physMemSize);
        pfnPT = new MyPageTable(physMemSize);
        frameTracking = new Policy(virtMemSize);


    }

/*
    protected int virtBlock(int va) {
        return va / PAGE_SIZE;
    }

 */

    protected int[] parseVA(int va) { //{vpn, offset}
        int[] arr = new int[2];
        arr[0] = va / PAGE_SIZE;
        arr[1] = va % PAGE_SIZE;
        return arr;
    }
    protected int[] parsePA(int pa) {
        int[] arr = new int[2];
        arr[0] = pa / PAGE_SIZE;
        arr[1] = pa % PAGE_SIZE;
        return arr;
    }

    protected int vaToPaAddressTranslation(int va) { //address translation
        int vpn = va / PAGE_SIZE;
        int offset = va % PAGE_SIZE;
        int pfn = vpnPT.getPFN(vpn);
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
    protected int handlePageFault(int va) { //returns pfn
        /*
        int[] parsedVA = parseVA(va);
        if(frameTracking.numFramesAvailable > 0) {
            Object pte = vpnPT.addPTE(vpnPT.newPTE(parsedVA[0], (int)frameTracking.availFrame()));
            frameTracking.useFrame(pte);
            int availFrame = (int)frameTracking.availFrame();
            Object[] parsedPTE = vpnPT.parsedPTE(pte);
            ram.load((int)parsedVA[0],(int)parsedVA[1] * PAGE_SIZE);
            return (int)(parsedPTE[1]);
        }
        else {
            return -1;
        }
        */
         return -1;
        //evict first frame in usedFrames
        //load page containing va into ram
        //add to page table
    }
}
