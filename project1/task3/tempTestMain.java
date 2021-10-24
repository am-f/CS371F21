public class tempTestMain{

    public static void main(String[] args){
        

        BlockList myList = new BlockList();
        System.out.println("List created successfully");
        myList.print();

        Block block1 = new Block(11, 9);

        Block block2 = new Block(1, 10);

        System.out.println("Blocks are adjacent: " + myList.calculateAdjacency(block1, block2));

        System.out.println("should print true: " + myList.insert(20, 10));
        myList.print();
        System.out.println("should print false: " + myList.insert(15, 10));
        myList.print();
        System.out.println("should print true: " + myList.insert(3, 5));
        myList.print();
        System.out.println("should print true: " + myList.insert(10, 3));
        myList.print();
    }
}