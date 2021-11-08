package vm;
import java.util.LinkedList;
public class MyPageTable {

    private static int INITIAL_SIZE;
    private PageTableEntry[] buckets;
    private int numPTEs;


    public MyPageTable(int physMemSize) {
        INITIAL_SIZE = physMemSize/64;

        buckets = new PageTableEntry[INITIAL_SIZE];

    }


    private int hashCode(int vpn) {
        return vpn % INITIAL_SIZE;
    }


    protected int getPFN(int vpn){ //returns pfn for vpn
        int index = hashCode(vpn);
        PageTableEntry finger = buckets[index];



        return -1;
    }
    /*
    protected void addPTE(int vpn, int pfn) {
        PageTableEntry newPTE = new PageTableEntry(vpn, pfn);
        addPTE(newPTE);
    }
    */
    protected void addPTE(PageTableEntry pte) {
        int index = hashCode(pte.vpn);
        PageTableEntry finger = buckets[index];
        pte.next = finger;
        buckets[index] = pte;
        numPTEs++;
    }

    protected void removePTE(PageTableEntry pte) {
        int index = hashCode(pte.vpn);
        PageTableEntry finger = buckets[index];
        if(finger == pte) {
            buckets[index] = pte.next;
            pte.next = null;
        }
        while(finger.next != pte) {
            finger = finger.next;
        }
        PageTableEntry del = finger.next;
        finger.next = del.next;
        del.next = null;
        numPTEs--;
    }



    private static class PageTableEntry {
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





}
