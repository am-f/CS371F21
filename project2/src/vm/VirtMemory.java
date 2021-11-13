package vm;
import storage.PhyMemory;
import java.util.Iterator;

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

    protected int vaToPaAddressTranslation(int va) throws PageFaultException, NoPFNException { //address translation
        int vpn = va / PAGE_SIZE;
        int offset = va % PAGE_SIZE;
        try {
            if(va >= virtMemSize) {
                throw new InvalidAddressException();
            }
            int pfn = vpnPT.getPTEbyVPN(vpn).pfn;
            int pa = pfn + offset;
            return pa;
        } catch (PageFaultException e) {
            handlePageFault(vpn);
            return vaToPaAddressTranslation(va);
        } catch (InvalidAddressException e) {
            System.err.print("invalid address");
            return -1;
        }

    }
    protected int paToVaAddressTranslation(int pa) {

        return -1;
    }


    //writes specified value into memory at specified virtual address
    public void write(int addr, byte value){
        int[] parsedVA = parseVA(addr);
        try {
            if(addr >= virtMemSize || addr < 0) {
                throw new InvalidAddressException();
            }
            
            int pfn = vpnPT.getPTEbyVPN(parsedVA[0]).pfn; 
            if(pfn == -1) {
                throw new PageFaultException();
            }
            else {
                ram.write((pfn * PAGE_SIZE) + parsedVA[1], value);
                pfnPT.getPTEbyPFN(pfn).dirty = true;
                writeCount++;
                return;
            }

        } catch(PageFaultException e) {
            int pfn = handlePageFault(parsedVA[0]);

            ram.write((pfn * PAGE_SIZE) + parsedVA[1], value);
            pfnPT.getPTEbyPFN(pfn).dirty = true;
            writeCount++;
            return;
        } catch (InvalidAddressException e) {
            System.err.println("invalid address");
            return;
        } finally {
            if(writeCount > 32) {
                sync_to_disk();
            }
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
    public byte read(int addr){
        int[] parsedVA = parseVA(addr);
        try {
            if(addr >= virtMemSize || addr < 0) {
                throw new InvalidAddressException();
            }
            int pfn = vpnPT.getPTEbyVPN(parsedVA[0]).pfn;
            if(pfn == -1) {
                throw new PageFaultException();

            }
            else {
                return ram.read((pfn * PAGE_SIZE) + parsedVA[1]);
            }
        } catch(PageFaultException e) {
            int pfn = handlePageFault(parsedVA[0]);
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
        Iterator<MyPageTable.PageTableEntry> ptIter = pfnPT.iterator();
        int index = 0;
        MyPageTable.PageTableEntry current;
        while(ptIter.hasNext()) {
            current = ptIter.next();
            if(current.dirty) {
                ram.store(current.vpn, current.pfn * PAGE_SIZE);
            }

        }


        //iterate through each PTE in buckets
        //for each PTE, if dirty==true, then get block number of page, ram.store(blockNum,
        // startAddress)
    }

    protected void loadPage(int vpn) throws PageFaultException, NoPFNException {
        try {
            if(vpn > (virtMemSize / PAGE_SIZE)) {
                throw new InvalidAddressException();
            }
            if(vpnPT.getPTEbyVPN(vpn).pfn != -1) {
                System.err.print("page already in ram");
            }
        }
        catch (InvalidAddressException e) {
            System.err.print("invalid address");
        }
        catch (PageFaultException e) {
            int pfn = handlePageFault(vpn);
            ram.load(vpn, pfn * PAGE_SIZE);
            vpnPT.addNewPTE(vpn, pfn);
            pfnPT.addNewPTE(vpn, pfn);
        }
        //load from disk to ram
        //add to page table
        //add to usedFrames

    }
    protected void evictPageByPFN(int pfn) {
        MyPageTable.PageTableEntry pte = pfnPT.getPTEbyPFN(pfn);
        //if()
        //if dirty==true, store to disk
        //remove from page table
        //remove from usedFrames
    }
    protected void evictPage(MyPageTable.PageTableEntry pte) {
        int vpn = pte.vpn;
        if (pte.dirty) {
            ram.store(pte.vpn, pte.pfn * PAGE_SIZE);
        }
        frameTracking.freeFrame(pte.pfn);
    }
    protected int handlePageFault(int vpn) { //returns pfn
        //int[] parsedVA = parseVA(va);
        try {
            if (frameTracking.numFramesAvailable == 0) {
                int pfn = frameTracking.usedPfnToEvict();
                MyPageTable.PageTableEntry pte = pfnPT.getPTEbyPFN(pfn);
                if (pte.dirty) {
                    int evictVPN = vpnPT.getPTEbyPFN(pfn).vpn;
                    ram.store(evictVPN, pfn * PAGE_SIZE);
                }
                frameTracking.freeFrame(pfn);
            }
            int pfn = frameTracking.useAvailFrame();
            ram.load(vpn, pfn * PAGE_SIZE);
            MyPageTable.PageTableEntry pte = vpnPT.addNewPTE(vpn, pfn);
            return pfn;
            //evict first frame in usedFrames
            //load page containing va into ram
            //add to page table
        } catch (Exception e) {
            return -1;
        }
    }

}
