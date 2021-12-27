import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class Node<T> {
    public T key;
    public AtomicMarkableReference<Node<T>> next;

    public Node(T key, AtomicMarkableReference<Node<T>> next){
        this.key = key;
        this.next  = next;
    }
}
