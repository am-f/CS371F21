package vm;
import java.util.LinkedList;

public class Policy {

    protected int numFrames;
    protected int numFramesAvailable;
    protected int numFramesUsed;
    protected LinkedList<Object> usedFrames = new LinkedList();
    protected LinkedList<Integer> availablePFNs = new LinkedList();
    protected Policy(int phySize) {
        numFrames = phySize / 64;
        numFramesAvailable = numFrames;
        numFramesUsed = 0;
        for(int i = 0; i < numFrames; i++) {
            availablePFNs.add(i);
        }

    }
   protected Object availPage() {
        return availablePFNs.peek();
    }
    protected Object pageToEvict() {
        return usedFrames.peek();
    }

    protected void useFrame(Object pte) {
        usedFrames.add(pte);
        numFramesUsed++;
        availablePFNs.removeFirst();
        numFramesAvailable--;

    }
    protected void evictFrame(Object pte, int pfn) {
        usedFrames.removeFirst();
        numFramesUsed--;
        availablePFNs.add(pfn);
        numFramesAvailable++;


    }











}
