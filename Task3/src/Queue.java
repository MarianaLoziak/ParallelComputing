import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public class Queue<T> {

    private Node<T> dummy = new Node(null, new AtomicReference<Node>(null));
    private AtomicReference<Node<T>> head = new AtomicReference<>(dummy);
    private AtomicReference<Node<T>> tail = new AtomicReference<>(dummy);
    public Queue(){

    }

    //adding
    public void push(T data){
        Node<T> newTail = new Node(data, new AtomicReference<Node<T>>(null));
        while(true){
            Node<T> currTail = tail.get();
            if(currTail.getNext().compareAndSet(null, newTail )){
                tail.compareAndSet(currTail, newTail);
                return;
            }
            else{
                tail.compareAndSet(currTail, currTail.getNext().get());
            }
        }
    }

    public T pop(){
        while(true){
            Node<T> currHead = head.get();
            Node<T> currTail = tail.get();
            Node<T> nextHead = currHead.getNext().get();
            if(currTail == currHead){
                if(nextHead == null){
                    throw new NullPointerException("Queue is empty");
                } else{
                    tail.compareAndSet(currTail,nextHead);
                }
            } else{
                T result = nextHead.getData();
                if (head.compareAndSet(currHead, nextHead)){
                    return result;
                }
            }
        }
    }

    public void print(){
        Node<T> node = head.get();
        while(node != null){
            System.out.println(node.getData());
            node = node.getNext().get();
        }
    }
}
