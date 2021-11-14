package vm;

import java.util.LinkedList;

public class PolicyLRU {
    //lru
    // add "last time used" var to PTE
    // policy: double-linked list sorted by last time used
    //hash table keyed by pfn
    //linked list of available frames
    //every time page is accessed, time and spot in list must be updated
    //find and remove lru: O(1)
    //update page use: o(1)ish
    //add page: o(1)
    //our list elements are: page{pfn, lastTimeUsed}
    private class Page {
        int pfn;
        long lastTimeUsed;
        Page nextPFN;
        Page left;
        Page right;

        Page(int pfn) {
            this.pfn = pfn;
            lastTimeUsed = System.currentTimeMillis();
            nextPFN = null;
            left = null;
            right = null;
        }
        Page(int pfn, long lastTimeUsed) {
            this.pfn = pfn;
            this.lastTimeUsed = lastTimeUsed;
            nextPFN = null;
            left = null;
            right = null;
        }
    }
    protected int numFrames;
    protected int numFramesAvailable;
    protected int numFramesUsed;

    PageList timeList;
    Page[] buckets;
    LinkedList<Page> availableFrames;
    final int IDK = 64 * 1024;

    public PolicyLRU(int phySize) {
        timeList = new PageList();
        buckets = new Page[phySize / 64];
        availableFrames = new LinkedList<Page>();
        numFrames = phySize / 64;
        numFramesAvailable = numFrames;
        numFramesUsed = 0;
        for(int i = 0; i < numFrames; i++) {
            Page page = new Page(i);
            availableFrames.add(page);
            addPagetoHashTable(page);
            page.lastTimeUsed = 0;
        }
    }
    private void addPagetoHashTable(Page page) {
        int key = hashCode(page.pfn) % 64;
        if(buckets[key] == null) {
            buckets[key] = page;
        }
        else {
            Page finger = buckets[key];
            while(finger.nextPFN != null) {
                if(finger.nextPFN.pfn < page.pfn) {
                    finger = finger.nextPFN;
                }
                else {
                    break;
                }
            }
            page.nextPFN = finger.nextPFN;
            finger.nextPFN = page;
        }
    }
    private Page removePagefromHashTable(Page page) {
        int key = hashCode(page.pfn) % 64;
        Page finger = buckets[key];
        if(finger == page) {
            buckets[key] = page.nextPFN;
            page.nextPFN = null;
            return page;
        }
        while(finger.nextPFN != page) {
            finger = finger.nextPFN;
        }
        finger.nextPFN = page.nextPFN;
        page.nextPFN = null;
        return page;
    }
    private void addPageToUsed(Page page) {
        //addPagetoHashTable(page);
        timeList.add(page);
    }
    private void addPageToUsed(int pfn) {
        Page page = new Page(pfn);
        //addPagetoHashTable(page);
        timeList.add(page);
    }
    private Page findPage(int pfn) {
        int key = hashCode(pfn) % 64;
        Page finger = buckets[key];
        if(finger.pfn == pfn) {
            return finger;
        }
        while(finger.pfn != pfn) {
            finger = finger.nextPFN;
            if(finger == null) {
                return null;
            }
        }
        return finger;

    }
    private Page removePageFromUsed(Page page) {
         //Page del = removePagefromHashTable(page);
         timeList.remove(page);
         return page;
    }
    private Page removePageFromUsed(int pfn) {
        //Page page = removePagefromHashTable(pfn);
        Page page = findPage(pfn);
        timeList.remove(page);
        return page;
    }
    private Page removePagefromHashTable(int pfn) {
        int key = hashCode(pfn) % 64;
        Page finger = buckets[key];
        if(finger.pfn == pfn) {
            buckets[key] = finger.nextPFN;
            finger.nextPFN = null;
            return finger;
        }
        while(buckets[key].nextPFN.pfn != pfn) {
            finger = finger.nextPFN;
        }
        Page del = finger.nextPFN;
        finger.nextPFN = del.nextPFN;
        del.nextPFN = null;
        return del;
    }
    protected int useAvailFrame() {
        Page page = availableFrames.peek();
        useFrame(page);
        return page.pfn;
    }
    private int useFrame(Page page) {
        addPageToUsed(page);
        numFramesUsed++;
        availableFrames.removeFirstOccurrence(page);
        numFramesAvailable--;
        page.lastTimeUsed = System.currentTimeMillis();
        return page.pfn;
    }
    protected void freeFrame(int pfn) {
        Page page = removePageFromUsed(pfn);
        numFramesUsed--;
        availableFrames.add(page);
        numFramesAvailable++;
        page.lastTimeUsed = 0;
    }
    private void freeFrame(Page page) {
        removePageFromUsed(page);
        numFramesUsed--;
        availableFrames.add(page);
        numFramesAvailable++;
        page.lastTimeUsed = 0;
    }
    protected void updatePage(int pfn) {
        if(numFramesUsed == 1) {
            return;
        }
        int key = hashCode(pfn) % 64;
        Page finger = buckets[key];
        Page page;
        if(finger.pfn == pfn) {
            page = finger;
        }
        else {
            while (finger.nextPFN.pfn != pfn) {
                finger = finger.nextPFN;
            }
            page = finger.nextPFN;
        }
        timeList.remove(page);
        timeList.add(page);
        page.lastTimeUsed = System.currentTimeMillis();

    }
    private int hashCode(int pfn) {
        //int hash = (pn / INITIAL_SIZE);
        int hash = pfn % 64;
        return hash;
    }
    protected int firstAvailPFN() {return availableFrames.peek().pfn;}
    protected int usedPfnToEvict() {
        return timeList.peek().pfn;
    }
    //protected Object availFrame() {return availablePFNs.peek();}
    //protected Object pageToEvict() { return usedFrames.peek(); }

    private class PageList {
        Page head;
        Page tail;
        public PageList() {
            head = null;
            tail = null;

        }
        protected void add(Page page) {
            if(head == null) {
                head = page;
                tail = page;
            }
            else {
                tail.right = page;
                page.left = tail;
                tail = page;
            }
        }
        protected Page peek() {
            return head;
        }

        protected void remove(Page page) {
            Page left = page.left;
            Page right = page.right;
            if(head == tail) {
                head = null;
                tail = null;
            }
            else {
                if(head == page) {
                    head = right;
                    right.left = null;
                }
                else if(tail == page) {
                    tail = left;
                    left.right = null;
                }
                else {
                    left.right = right;
                    right.left = left;
                }
            }
            /*
            if(head == page && tail == page) {
                head = null;
                tail = null;
                page.left = null;
                page.right = null;
                return;
            }
            if(head == page && tail != page) {
                head = right;
                right.left = null;
            }
            else {
                left.right = right;
                right.left = left;
            }
            if(tail == page && head != page) {
                tail = left;
                left.right = null;
            }
            else {
                right.left = left;
                left.right = right;
            }
             */
                page.left = null;
                page.right = null;
        }
        protected void remove() {
            Page page = head;
            head = page.right;
            if(tail == page) {
                tail = null;
            }
            else {
                page.right.left = null;
            }
        }



    }
}
