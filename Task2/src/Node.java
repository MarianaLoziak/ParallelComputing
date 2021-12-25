import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class Node {
    int data;
    int height;
    AtomicMarkableReference<Node>[] next;

    public Node(int data, int height){
        this.data = data;
        this.height = height;
        this.next = new AtomicMarkableReference[height+1];
        for (int i = 0; i < height+1; i++) {
            next[i] = new AtomicMarkableReference<Node>(null,false);
        }
    }
}
