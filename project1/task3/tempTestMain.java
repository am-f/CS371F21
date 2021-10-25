public class tempTestMain{

    public static void main(String[] args){
        

        BlockList myList = new BlockList(100);
        System.out.println("List created successfully");
        myList.print();

        System.out.println("should print true: " + myList.insert(20, 10));
        myList.print();
        System.out.println("should print false: " + myList.insert(15, 10));
        myList.print();
        System.out.println("should print true: " + myList.insert(3, 5));
        myList.print();
        System.out.println("should print true: " + myList.insert(10, 3));
        myList.print();
        System.out.println("should print true: " + myList.insert(15, 3));
        myList.print();
        System.out.println("should print true: " + myList.insert(40, 10));
        myList.print();

        System.out.println("Deleting Block: " + myList.delete(15));
        myList.print();

        ((BlockList) myList.searchBySize(10)).print();
    }
}