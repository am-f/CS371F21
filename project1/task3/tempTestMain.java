public class tempTestMain{

    public static void main(String[] args){


        MyMemoryAllocation mal= new MyMemoryAllocation(14, "NF");
        //free->[1,13]
        //used->
        System.out.println("Start:");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        mal.alloc(1);
        //free->[2,12]
        //used->[1,1]
        System.out.println("alloc(1)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        mal.alloc(3);
        System.out.println("alloc(3)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->[5,9]
        //used->[1,1]->[2,3]
        mal.alloc(2);
        System.out.println("alloc(2)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->[7,7]
        //used->[1,1]->[2,3]->[5,2]
        mal.alloc(2);
        System.out.println("alloc(2)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->[9,5]
        //used->[1,1]->[2,3]->[5,2]->[7,2]
        mal.alloc(1);
        System.out.println("alloc(1)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->[10,4]
        //used->[1,1]->[2,3]->[5,2]->[7,2]->[9,1]
        mal.alloc(1);
        System.out.println("alloc(1)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->[11,3]
        //used->[1,1]->[2,3]->[5,2]->[7,2]->[9,1]->[10,1]
        mal.alloc(1);
        System.out.println("alloc(1)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->[12,2]
        //used->[1,1]->[2,3]->[5,2]->[7,2]->[9,1]->[10,1]->[11,1]
        mal.alloc(2);
        System.out.println("alloc(2)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->
        //used->[1,1]->[2,3]->[5,2]->[7,2]->[9,1]->[10,1]->[11,1]->[12,2]
        mal.free(2);
        System.out.println("free(2)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->[2,3]
        //used->[1,1]->[5,2]->[7,2]->[9,1]->[10,1]->[11,1]->[12,2]
        mal.free(7);
        System.out.println("free(7)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->[2,3]->[7,2]
        //used->[1,1]->[5,2]->[9,1]->[10,1]->[11,1]->[12,2]
        mal.free(10);
        System.out.println("free(10)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->[2,3]->[7,2]->[10,1]
        //used->[1,1]->[5,2]->[9,1]->[11,1]->[12,2]
        System.out.println();
        System.out.println("START TEST");
        System.out.println();
        mal.free(12);
        System.out.println("free(12)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println();
        //free->[2,3]->[7,2]->[10,1]->[12,2]
        //used->[1,1]->[5,2]->[9,1]->[11,1]
        //free->[2,3]->[7,2]->[10,1]->[12,2]
        //used->[1,1]->[5,2]->[9,1]->[11,1]
        mal.alloc(1);
        System.out.println("alloc(1)");
        System.out.println("size: " + mal.size());
        System.out.println("maxSize: " + mal.max_size());
        System.out.print("free: ");
        mal.free.print();
        System.out.print("used: ");
        mal.used.print();
        System.out.println("READY:::::");
        System.out.print(mal.alloc(1));

        //if ((mal.alloc(1) != 2)) throw new AssertionError();
        //int alloc1 = mal.alloc(1);
        //System.out.printf("%d\n\n", alloc1);
        //free->[3,2]->[7,2]->[10,1]->[12,2]
        //used->[1,1]->[2,1]->[5,2]->[9,1]->[11,1]
        assert(mal.alloc(2)==7);
        //int alloc1 = mal.alloc(1);
        //System.out.printf("%d\n\n", alloc1);
        //free->[3,2]->[10,1]->[12,2]
        //used->[1,1]->[2,1]->[5,2]->[7,2]->[9,1]->[11,1]
        assert(mal.alloc(2)==12);
        //free->[3,2]->[10,1]
        //used->[1,1]->[2,1]->[5,2]->[7,2]->[9,1]->[11,1]->[12,2]
        assert(mal.alloc(3)==0); //also failed case ! fragments!
        //free->[3,2]->[10,1]
        //used->[1,1]->[2,1]->[5,2]->[7,2]->[9,1]->[11,1]->[12,2]
        assert(mal.alloc(1)==3); //wrap around
        //free->[4,1]->[10,1]
        //used->[1,1]->[2,1]->[3,1]->[5,2]->[7,2]->[9,1]->[11,1]->[12,2]
        //assert(mal.size() == 8);
        //assert(mal.max_size() == 3);
        //return mal;





        /*

        BlockList myList = new FreeList(100);
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

        //((BlockList) myList.searchBySize(10)).print();

         */
    }
}
