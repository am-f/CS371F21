package vm;
import java.util.LinkedList;

public class Policy {

    protected int numFrames;
    protected int numFramesAvailable;
    protected int numFramesUsed;
    protected LinkedList<Integer> usedPFNs = new LinkedList<Integer>();
    protected LinkedList<Integer> availablePFNs = new LinkedList<Integer>();

    protected Policy(int phySize) {
        numFrames = phySize / 64;
        numFramesAvailable = numFrames;
        numFramesUsed = 0;
        for (int i = 0; i < numFrames; i++) {
            availablePFNs.add(i);
        }

    }

    protected int firstAvailPFN() {
        return availablePFNs.peek();
    }

    protected int usedPfnToEvict() {
        return usedPFNs.peek();
    }

    protected int useAvailFrame() { //uses first frame
        int pfn = firstAvailPFN();
        useFrame(pfn);
        return pfn;
    }

    protected void useFrame(int pfn) {
        usedPFNs.add(pfn);
        numFramesUsed++;
        availablePFNs.removeFirst();
        numFramesAvailable--;

    }

    protected void freeFrame(int pfn) {
        usedPFNs.removeFirst();
        numFramesUsed--;
        availablePFNs.add(pfn);
        numFramesAvailable++;

    }
}
