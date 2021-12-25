import java.util.concurrent.atomic.AtomicReference;

public class Node<T>{
    private T data;
    private AtomicReference<Node<T>> next;

    public Node(T data, AtomicReference<Node<T>> next){
        this.data = data;
        this.next = next;
    }

    public T getData(){
        return data;
    }

    public AtomicReference<Node<T>> getNext(){
        return next;
    }
}
