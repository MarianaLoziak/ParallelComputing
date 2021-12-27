import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class HarrisList<T extends Comparable<? super T>> {

    Node<T> tail = new Node<>(null, new AtomicMarkableReference<>(null, false));
    Node<T> head = new Node<>(null, new AtomicMarkableReference<>(null, false));

    public HarrisList(){
        head.next.compareAndSet(null, tail,false,false);
    }

    public boolean insert(T key){
        Node<T> newNode = new Node<>(key, new AtomicMarkableReference(null, false) );
        Neighbour<T> neighbour;
        while(true){
            neighbour = search(key);
            if(neighbour.right != tail && neighbour.right.key.compareTo(key)==0){
                return false;
            }
            newNode.next = new AtomicMarkableReference<>(neighbour.right, false);
            if(neighbour.left.next.compareAndSet(neighbour.right, newNode, false, false)){
                return true;
            }
        }
    }

    public boolean remove(T key){
        Node<T> rightNextNode;// = new AtomicMarkableReference(null, false);
        Neighbour<T> neighbour;

        while(true){
            neighbour = search(key);
            if(neighbour.right == tail || !(neighbour.right.key.compareTo(key)==0)){//ключ не знайдено або вже видалено
                return false;
            }

            rightNextNode = neighbour.right.next.getReference();

            if(!neighbour.right.next.isMarked()){
                if(neighbour.right.next.compareAndSet(rightNextNode,rightNextNode,false,true)){
                    break;
                }
            }
        }
        if(!neighbour.left.next.compareAndSet(neighbour.right,rightNextNode,false, false)){//!!!!!!
            neighbour = search(neighbour.right.key);
        }
        return true;
    }

    public List<T> print(){
        Node<T> node = head;
        List<T> nodes = new ArrayList<T>();
        while (node!=null && node.next.getReference() != tail){
            node = node.next.getReference();
            //System.out.println(node.key);
            nodes.add(node.key);
        }
        return nodes;
    }

    public Neighbour<T> search (T key){

        Node<T> leftNode = null;
        Node<T> leftNextNode = null;
        Node<T> rightNode = null;
        while(true){
            Node<T> node = head;
            Node<T> nodeNext = head.next.getReference();


            do{
                boolean[] markHolder;
                if(!node.next.isMarked()){
                    leftNode = node;
                    leftNextNode = node.next.getReference();
                }

                node = nodeNext;
                if (node == tail) break;
                nodeNext = node.next.getReference();
            }while(node.next.isMarked()||(node.key.compareTo(key)<0));

            rightNode = node;
            if(leftNextNode == rightNode){
                if(rightNode!=tail && rightNode.next.isMarked()){
                    continue;
                } else {
                    return new Neighbour<T>(leftNode, rightNode);
                }
            }
            if(leftNode.next.compareAndSet(leftNextNode,rightNode,false, false)){
                if (rightNode !=tail && rightNode.next.isMarked()){
                    continue;
                }
                else{
                    return new Neighbour<T>(leftNode, rightNode);
                }
            }
        }
    }

    class Neighbour<T>{
        Node<T> left;
        Node<T> right;

        public Neighbour(Node<T> left, Node<T> right){
            this.left = left;
            this.right = right;
        }
    }

}
