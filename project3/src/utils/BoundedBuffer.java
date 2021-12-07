package utils;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**Modeled after in-class demo code for BoundedBuffer (https://jyuan2pace.github.io/CS371F21/ostep-code/BoundedBuffer.java)**/
public class BoundedBuffer <T> {
//Please note the biggest difference between this BoundBuffer
//and the one we demoed in class is <T>
//implement member functions: deposit() and fetch()

     private T[] buff;
     private int capacity;
     private int front;
     private int rear;
     private int count;
     private Lock lock = new ReentrantLock();
     private Condition notFull = lock.newCondition();
     private Condition notEmpty = lock.newCondition();


     public BoundedBuffer(int capacity) {

     }
     void deposit(T data) {

     }
     T fetch() {
         return null;
     }
}
