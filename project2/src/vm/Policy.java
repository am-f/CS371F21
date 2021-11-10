package vm;
import java.util.LinkedList;

public class Policy {

    protected int numFrames;
    protected int numFramesAvailable;
    protected int numFramesUsed;
    //protected LinkedList<Object> usedFrames = new LinkedList();
    protected LinkedList<Integer> usedPFNs = new LinkedList();
    protected LinkedList<Integer> availablePFNs = new LinkedList();
    protected Policy(int phySize) {
        numFrames = phySize / 64;
        numFramesAvailable = numFrames;
        numFramesUsed = 0;
        for(int i = 0; i < numFrames; i++) {
            availablePFNs.add(i);
        }

    }
    protected int firstAvailPFN() {return availablePFNs.peek();}
    protected int usedPfnToEvict() {
        return usedPFNs.peek();
    }
    //protected Object availFrame() {return availablePFNs.peek();}
    //protected Object pageToEvict() { return usedFrames.peek(); }
    protected int useAvailFrame() {
        int pfn = firstAvailPFN();
        useFrame(pfn);
        return pfn;
    }
    protected void useFrame(int pfn) { //uses first frame
    //protected void useFrame(Object pte) {
        //usedFrames.add(pte);
        usedPFNs.add(pfn);
        numFramesUsed++;
        availablePFNs.removeFirst();
        numFramesAvailable--;

    }

    protected void freeFrame(int pfn) {
    //protected void evictFrame(Object pte, int pfn) {
        usedPFNs.removeFirst();
        numFramesUsed--;
        availablePFNs.add(pfn);
        numFramesAvailable++;

    }













}
