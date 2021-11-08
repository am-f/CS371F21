package vm;
import java.util.LinkedList;

public class Policy {

    protected LinkedList<Integer> usedFramesByVPN = new LinkedList();
    protected LinkedList<Integer> availableFramesByPFN = new LinkedList();
    protected Policy(int phySize) {
        int pfNum = phySize / 64;
    }
   protected int pfnOfAvailPage() {
        return availableFramesByPFN.peek();
    }
    protected int vpnOfPageToEvict() {
        return usedFramesByVPN.peek();
    }
    /*
    protected void addToEnd(int vpn) {

    }
    protected void removeFromFront(int vpn) {

    }

     */









}
