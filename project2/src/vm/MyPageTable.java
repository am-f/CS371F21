package vm;
import java.util.Iterator;

public class MyPageTable {

    private static int INITIAL_SIZE;
    private PageTableEntry[] vpnBuckets;
    private PageTableEntry[] pfnBuckets;
    private int numPTEs;


    public MyPageTable(int physMemSize) {
        INITIAL_SIZE = physMemSize / 64;
        vpnBuckets = new PageTableEntry[INITIAL_SIZE];
        pfnBuckets = new PageTableEntry[INITIAL_SIZE];
        numPTEs = 0;

    }


    private int hashCode(int pn) {
        //int hash = (pn / INITIAL_SIZE);
        int hash = pn % 64;
        return hash;
    }


    protected PageTableEntry getPTEbyPFN(int pfn) {
        int index = hashCode(pfn) % INITIAL_SIZE;
        PageTableEntry finger = pfnBuckets[index];
        try {
            while (finger != null) {
                if (finger.pfn == pfn) {
                    return finger;
                }
                finger = finger.pfnNext;
            }
            if (finger == null) {
                throw new NoPFNException();
            }
        } catch (NoPFNException e) {
            System.err.print("there's nothing in this frame");
        }
        return null;
    }

    // protected int getPFN(int vpn) throws PageFaultException {
    //     try {
    //         PageTableEntry pte = getPTEbyVPN(vpn);
    //         return pte.pfn;

    //     } catch (PageFaultException e) {
    //         throw e;
    //     }
       
    // }

    // protected int getVPN(int pfn) throws NoPFNException {
    //     PageTableEntry pte = getPTEbyPFN(pfn);
    //     return pte.vpn;
    // }

    protected PageTableEntry getPTEbyVPN(int vpn) throws PageFaultException { //returns pfn for vpn
        try {
            int index = hashCode(vpn) % INITIAL_SIZE;
            PageTableEntry finger = vpnBuckets[index];
            while (finger != null) {
                if (finger.vpn == vpn) {
                    return finger;
                }
                finger = finger.vpnNext;
            }
            if (finger == null) {
                throw new PageFaultException();
            }
        } catch (PageFaultException e) {
            throw e;
        }
        return null;
    }


    protected PageTableEntry addNewPTE(int vpn, int pfn) {
        PageTableEntry pte = new PageTableEntry(vpn, pfn);
        int vpnKey = hashCode(pte.vpn) % INITIAL_SIZE;
        int pfnKey = hashCode(pte.pfn) % INITIAL_SIZE;
        PageTableEntry finger = vpnBuckets[vpnKey];
        pte.vpnNext = finger;
        vpnBuckets[vpnKey] = pte;
        finger = pfnBuckets[pfnKey];
        pte.pfnNext = finger;
        pfnBuckets[pfnKey] = pte;
        numPTEs++;
        return pte;
    }


    protected void removePTE(PageTableEntry pte) {
        int vpnKey = hashCode(pte.vpn) % INITIAL_SIZE;
        int pfnKey = hashCode(pte.pfn) % INITIAL_SIZE;
        //vpn
        PageTableEntry finger = vpnBuckets[vpnKey];
        if (finger == pte) {
            vpnBuckets[vpnKey] = pte.vpnNext;
            pte.vpnNext = null;
        }
        else {
            while (finger.vpnNext != pte) {
                finger = finger.vpnNext;
            }
            finger.vpnNext = pte.vpnNext;
        }
        pte.vpnNext = null;
        //pfn
        finger = pfnBuckets[pfnKey];
        if (finger == pte) {
            pfnBuckets[pfnKey] = pte.pfnNext;
            pte.pfnNext = null;
        }
        else {
            while (finger.pfnNext != pte) {
                finger = finger.pfnNext;
            }
            finger.pfnNext = pte.pfnNext;
        }
        pte.pfnNext = null;
        numPTEs--;

    }

    protected static class PageTableEntry { //Protected so VirtMemory can reference PTEs
        // directly so VirtMemory.sync_to_disk() can store only dirty PTEs
        int vpn;
        int pfn;
        boolean dirty;
        PageTableEntry vpnNext;
        PageTableEntry pfnNext;


        PageTableEntry(int vpn, int pfn) {
            this.vpn = vpn;
            this.pfn = pfn;
            dirty = false;
        }

    }

    protected PageTableIterator iterator() {
        return new PageTableIterator();
    }



    private class PageTableIterator implements Iterator<PageTableEntry> {
        PageTableEntry current;
        int numSeen;
        int numLeft;
        int currBucket;
        //initializes pointer to head of PageTable for iteration
        public PageTableIterator() {
            numSeen = 0;
            numLeft = numPTEs;
            currBucket = 0;
            for ( ; currBucket < vpnBuckets.length; currBucket++) {
                if (vpnBuckets[currBucket] == null) {
                    continue;
                }
                current = vpnBuckets[currBucket];
                break;
            }

        }
        PageTableEntry getNext() {
            if(current.vpnNext == null) {
                currBucket++;
                for ( ; currBucket < vpnBuckets.length; currBucket++) {
                    if (vpnBuckets[currBucket] == null) {
                        continue;
                    }
                    PageTableEntry next = vpnBuckets[currBucket];
                    return next;
                }
            }
            return current.vpnNext;

        }

        //returns false if next element doesn't exist
        public boolean hasNext() {
            if(numSeen == numPTEs) {
                return false;
            }
            else return true;
        }
        //Returns current block and updates pointer
        public PageTableEntry next() {
            PageTableEntry temp = current;
            current = getNext();
            numSeen++;
            return temp;

        }


    }
}
