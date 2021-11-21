package vm;
import java.util.Iterator;

public class MyPageTable {

    private static int initialSize;
    private static int HASH_SIZE;
    private PageTableEntry[] vpnBuckets;
    private PageTableEntry[] pfnBuckets;
    private int numPTEs;
    private final double DEFAULT_LOAD_FACTOR = 0.75;

    public MyPageTable(int physMemSize) {
        initialSize = physMemSize / 64;
        HASH_SIZE = physMemSize / 64;
        vpnBuckets = new PageTableEntry[initialSize];
        pfnBuckets = new PageTableEntry[initialSize];
        numPTEs = 0;

    }


    private int hashCode(int pn) {
        int hash = (pn / HASH_SIZE);
        return hash;
    }


    protected PageTableEntry getPTEbyPFN(int pfn) {
        int index = hashCode(pfn) % initialSize;
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

    protected PageTableEntry getPTEbyVPN(int vpn) throws PageFaultException { //returns pfn for vpn
        try {
            int index = hashCode(vpn) % initialSize;
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

    protected PageTableEntry addPTE(PageTableEntry pte, int initialSize) {
        int vpnKey = hashCode(pte.vpn) % initialSize;
        int pfnKey = hashCode(pte.pfn) % initialSize;
        PageTableEntry finger = vpnBuckets[vpnKey];
        pte.vpnNext = finger;
        vpnBuckets[vpnKey] = pte;
        finger = pfnBuckets[pfnKey];
        pte.pfnNext = finger;
        pfnBuckets[pfnKey] = pte;
        numPTEs++;

        return pte;
    }
    protected PageTableEntry addNewPTE(int vpn, int pfn) {
        PageTableEntry pte = new PageTableEntry(vpn, pfn);
        addPTE(pte, initialSize);
        
        //after new element is added load factor is recalculated
        double loadFactor = (1.0 * numPTEs) / initialSize;
        //if load factor > 0.75 we need to rehash
        if(loadFactor > DEFAULT_LOAD_FACTOR ){
            rehash();
        }
        return pte;
    }

    protected void rehash(){
        //We'd need to rehash the vpn bucket list AND the pfn bucket list
            //Current buckets are made into temps
            PageTableEntry oldVpn[] = vpnBuckets;
            PageTableEntry oldPfn[] = pfnBuckets;

            //Old lists are made to be twice the size
            this.vpnBuckets = new PageTableEntry[2 * initialSize];
            this.pfnBuckets = new PageTableEntry[2 * initialSize];
             //numPTEs = 0;
             initialSize *= 2;


            //loop through original vpn bucket list and insert it into the new list
            for(int i = 0; i < oldVpn.length; i++){
                //head of chain at index
                PageTableEntry head = oldVpn[i];
                PageTableEntry next;
                if(head == null) {
                    continue;
                }
                else {
                //TODO: Infinite Loop, does not update head within loop so it will never be null
                    while( head != null) {
                        //remove pte from old
                        //add pte to new
                        next = head.vpnNext;
                        removePTE(head, oldVpn, oldPfn, initialSize);
                        addPTE(head, initialSize * 2);
                        //head = next;
                        head = oldVpn[i];
                    }

                }
            }

    }

    protected void removePTE(PageTableEntry pte) {
        removePTE(pte, vpnBuckets, pfnBuckets, initialSize);
    }

    protected void removePTE(PageTableEntry pte, PageTableEntry[] vpnBuckets,
                             PageTableEntry[] pfnBuckets, int initialSize ) {
        int vpnKey = hashCode(pte.vpn) % initialSize;
        int pfnKey = hashCode(pte.pfn) % initialSize;
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


    private class PageTableIterator implements Iterator<Integer> {
        PageTableEntry current;
        int numSeen;
        int numLeft;
        int currBucket;
        //initializes pointer to head of PageTable for iteration
        private PageTableIterator() {
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
        private PageTableEntry getNext() {
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
        public Integer next() {
            PageTableEntry temp = current;
            current = getNext();
            numSeen++;
            return temp.pfn;
        }


    }
}
