package vm;
import java.util.Iterator;

public class MyPageTable {

    private static int INITIAL_SIZE;
    private PageTableEntry[] buckets;
    private int numPTEs;


    public MyPageTable(int physMemSize) {
        INITIAL_SIZE = physMemSize / 64;
        buckets = new PageTableEntry[INITIAL_SIZE];
        numPTEs = 0;

    }


    private int hashCode(int pn) {
        int hash = (pn / INITIAL_SIZE);
        return hash;
    }


    protected PageTableEntry getPTEbyPFN(int pfn) {
        int index = hashCode(pfn) % INITIAL_SIZE;
        PageTableEntry finger = buckets[index];
        try {
            while (finger != null) {
                if (finger.pfn == pfn) {
                    return finger;
                }
                finger = finger.next;
            }
            if (finger == null) {
                throw new NoPFNException();
            }
        } catch (NoPFNException e) {
            System.err.print("there's nothing in this frame");
        }
        return null;
    }

    protected int getPFN(int vpn) throws PageFaultException {
        try {
            PageTableEntry pte = getPTEbyVPN(vpn);
            return pte.pfn;

        } catch (PageFaultException e) {
            throw e;
        }
        //return pte.vpn;
    }

    protected int getVPN(int pfn) throws NoPFNException {
        PageTableEntry pte = getPTEbyPFN(pfn);
        return pte.vpn;
    }

    protected PageTableEntry getPTEbyVPN(int vpn) throws PageFaultException { //returns pfn for vpn
        try {
            int index = hashCode(vpn) % INITIAL_SIZE;
            PageTableEntry finger = buckets[index];
            while (finger != null) {
                if (finger.pfn == vpn) {
                    return finger;
                }
                finger = finger.next;
            }
            if (finger == null) {
                throw new PageFaultException();
            }
        } catch (PageFaultException e) {
            throw e;
        }
        return null;
    }

    /*
    protected void addPTE(int vpn, int pfn) {
        PageTableEntry newPTE = new PageTableEntry(vpn, pfn);
        addPTE(newPTE);
    }
    */
    protected PageTableEntry addNewPTE(int vpn, int pfn) {
        PageTableEntry pte = new PageTableEntry(vpn, pfn);
        int index = hashCode(pte.vpn);
        PageTableEntry finger = buckets[index];
        pte.next = finger;
        buckets[index] = pte;
        numPTEs++;
        return pte;
    }

    protected PageTableEntry addPTE(PageTableEntry pte) {
        int index = hashCode(pte.pfn);
        PageTableEntry finger = buckets[index];
        pte.next = finger;
        buckets[index] = pte;
        numPTEs++;
        return pte;
    }


    protected void removePTE(PageTableEntry pte) {
        int index = hashCode(pte.vpn);
        PageTableEntry finger = buckets[index];
        if (finger == pte) {
            buckets[index] = pte.next;
            pte.next = null;
        }
        while (finger.next != pte) {
            finger = finger.next;
        }
        PageTableEntry del = finger.next;
        finger.next = del.next;
        del.next = null;
        numPTEs--;
    }

/*
    protected Object[] parsedPTE(Object pte) {
        Object[] parsed = new Object[3];
        parsed[0] = ((PageTableEntry)pte).vpn;
        parsed[1] = ((PageTableEntry)pte).pfn;
        parsed[2] = ((PageTableEntry)pte).dirty;
        return parsed;
    }

 */
    /*
    protected PageTableEntry newPTE(int vpn, int pfn) {
        return new PageTableEntry(vpn, pfn);
    }

     */


    protected static class PageTableEntry { //Protected so VirtMemory can reference PTEs
        // directly so VirtMemory.sync_to_disk() can store only dirty PTEs
        int vpn;
        int pfn;
        boolean dirty;
        PageTableEntry next;

        PageTableEntry(int vpn, int pfn) {
            this.vpn = vpn;
            this.pfn = pfn;
            dirty = false;
        }

    }

    protected PageTableIterator iterator() {
        return new PageTableIterator();
    }


    PageTableEntry getFirst() {
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] == null) {
                continue;
            }
            return buckets[i];
        }
        return null;
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
            for ( ; currBucket < buckets.length; currBucket++) {
                if (buckets[currBucket] == null) {
                    continue;
                }
                current = buckets[currBucket];
                break;
            }

        }
        PageTableEntry getNext() {
            if(current.next == null) {
                for ( ; currBucket < buckets.length; currBucket++) {
                    if (buckets[currBucket] == null) {
                        continue;
                    }
                    PageTableEntry next = buckets[currBucket];
                    return next;
                }
            }
                return current.next;

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
