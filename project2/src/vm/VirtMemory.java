package vm;
import storage.PhyMemory;
import java.util.Iterator;

public class VirtMemory extends Memory {

    protected int writeCount; //if writecount > 32, sync_to_disk
    protected MyPageTable pt;
    protected int PAGE_SIZE = 64;
    protected Policy frameTracking;
    //protected PolicyLRU frameTracking;
    protected int virtMemSize;
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
        pt = new MyPageTable(physMemSize);
        frameTracking = new Policy(physMemSize);
        //frameTracking = new PolicyLRU(physMemSize);

    }




    protected int[] parseVA(int va) { //{vpn, offset}
        int[] arr = new int[2];
        arr[0] = va / PAGE_SIZE;
        arr[1] = va % PAGE_SIZE;
        return arr;
    }


    //writes specified value into memory at specified virtual address
    public void write(int addr, byte value){
        int[] parsedVA = parseVA(addr);
        try {
            if(addr >= virtMemSize || addr < 0) {
                throw new InvalidAddressException();
            }
            MyPageTable.PageTableEntry pte = pt.getPTEbyVPN(parsedVA[0]);
            int pfn = pte.pfn;

            if(pfn == -1) {
                throw new PageFaultException();
            }
            else {
                ram.write((pfn * PAGE_SIZE) + parsedVA[1], value);
                pte.dirty = true;
                frameTracking.updatePage(pfn);
                writeCount++;
                //return;
            }

        } catch(PageFaultException e) {
            //int pfn = loadPage(parsedVA[0]);
            int pfn = handlePageFault(parsedVA[0]);
            ram.write((pfn * PAGE_SIZE) + parsedVA[1], value);
            pt.getPTEbyPFN(pfn).dirty = true; //old
            frameTracking.updatePage(pfn);
            writeCount++;
            //return;
        } catch (InvalidAddressException e) {
            System.err.println("invalid address");
        } finally {
            if(writeCount >= 32) {
                sync_to_disk();
                writeCount = 0;
            }
            return;
        }
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
    public byte read(int addr) {
        int[] parsedVA = parseVA(addr);
        try {
            if(addr >= virtMemSize || addr < 0) {
                throw new InvalidAddressException();
            }
            MyPageTable.PageTableEntry pte = pt.getPTEbyVPN(parsedVA[0]);
            int pfn = pte.pfn;
            if(pfn == -1) {
                throw new PageFaultException();
            }
            else {
                frameTracking.updatePage(pfn);
                return ram.read((pfn * PAGE_SIZE) + parsedVA[1]);

            }
        } catch(PageFaultException e) {
            int pfn = handlePageFault(parsedVA[0]);
            //int pfn = loadPage(parsedVA[0]);
            frameTracking.updatePage(pfn);
            return ram.read((pfn * PAGE_SIZE) + parsedVA[1]);
        } catch (InvalidAddressException e) {
            System.err.println("invalid address");
            //return
        }
        //find hash for address
        //search bucket[hash] for correct vpn (address)
        //if PTE is found: convert vpn to pfn, return ram.read(pfn)
        //if PTE is not found: Page fault! Upon return from handling page fault, convert vpn to
        // pfn, return ram.read(pfn)

        return 0;
    }

    //flush back dirty pages to disk
    protected void sync_to_disk(){
        Iterator<MyPageTable.PageTableEntry> ptIter = pt.iterator();
        MyPageTable.PageTableEntry current;
        while(ptIter.hasNext()) {
            current = ptIter.next();
            if(current.dirty) {
                ram.store(current.vpn, current.pfn * PAGE_SIZE);
                current.dirty = false;
            }

        }

        //iterate through each PTE in buckets
        //for each PTE, if dirty==true, then get block number of page, ram.store(blockNum,
        // startAddress)
    }


    protected void evictPage(MyPageTable.PageTableEntry pte) {
        if (pte.dirty) {
            ram.store(pte.vpn, pte.pfn * PAGE_SIZE);
            pte.dirty = false;
        }
        frameTracking.freeFrame(pte.pfn);
        pt.removePTE(pte);

    }
    protected int handlePageFault(int vpn) { //returns pfn
        if (frameTracking.numFramesAvailable == 0) {
            int pfn = frameTracking.usedPfnToEvict();
            MyPageTable.PageTableEntry pte = pt.getPTEbyPFN(pfn);
            evictPage(pte);
        }
        int pfn = frameTracking.useAvailFrame();
        ram.load(vpn, pfn * PAGE_SIZE);
        pt.addNewPTE(vpn, pfn);
        return pfn;
        //evict first frame in usedFrames
        //load page containing va into ram
        //add to page table
    }
}
