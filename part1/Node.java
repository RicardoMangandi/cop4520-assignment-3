import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {

    public int uniqueNum;
    public Node next;
    public volatile boolean marked;
    public volatile boolean tagged;
    public Lock lock;

    Node(int uniqueNum)
    {
        this.uniqueNum = uniqueNum;
        this.next = null;
        this.marked = false;
        this.tagged = false;
        this.lock = new ReentrantLock();
    }
    
    public void lock()
    {
        lock.lock();
    }

    public void unlock()
    {
        lock.unlock();
    }
}
