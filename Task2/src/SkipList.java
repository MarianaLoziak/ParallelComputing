import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class SkipList {
    private static final int MaxLevel = 16;
    public int getMaxLevel(){
        return this.MaxLevel;
    }
    final Node head = new Node(Integer.MIN_VALUE, MaxLevel);
    final Node tail = new Node(Integer.MAX_VALUE, MaxLevel);
    private static final double p = 0.5;
    private static Random random = new Random();

    public SkipList(){
        for (int i = 0; i < head.next.length; i++) {
            head.next[i] = new AtomicMarkableReference<>(tail, false);
        }
    }

    public boolean find(int data, Node[] preds, Node[] succs){
        Node left = null;
        Node current = null;
        Node right = null;
        while(true){
            left = head;
            boolean found = true;
            for (int level = MaxLevel; level >=0 && found ; level--) {
                current = left.next[level].getReference();
                while(true){
                    boolean[] markHolder = {false};
                    right = current.next[level].get(markHolder);
                    while(markHolder[0]){
                        //left = current;
                        //current = left.next[level].getReference();
                        //right = current.next[level].get(markHolder);
                        found = left.next[level].compareAndSet(current, right, false, false);
                        if(!found){
                            break;
                        }
                        current = left.next[level].getReference();
                        right = current.next[level].get(markHolder);
                    }
                    if(current.data < data){
                        left = current;
                        current  = right;
                    } else break;

                }

                if (found){
                    preds[level] = left;
                    succs[level] = current;
                }
                else break;
            }
            if(found){
                return (data == current.data);
            }

        }
    }



    public boolean remove(int data){
        Node[]preds = new Node[MaxLevel+1];
        Node[]succs = new Node[MaxLevel+1];
        while(true){
            boolean found = find(data, preds, succs);
            if(found){
                Node node = succs[0];

                for (int level =node.height; level > 0 ; level--) {
                    boolean[] markHolder = {false};
                    Node next = node.next[level].get(markHolder);
                    while(markHolder[0]==false){
                        node.next[level].attemptMark(next, true);
                        next = node.next[level].get(markHolder);
                    }
                }

                boolean[] markHolder = {false};
                Node next = node.next[0].get(markHolder);
                while(true){
                    boolean marked = node.next[0].compareAndSet(next, next, false, true);

                    //метод пошуку допомагає фізично видалити вузол, що позначений
                    if(marked)
                    {
                        find(data,preds,succs);
                        return true;
                    }
                    else
                    {
                        next = node.next[0].get(markHolder);

                        //Node is already marked, returning false
                        if(markHolder[0])
                            return false;
                    }
                }
            } else{
                return false;
            }
        }

    }

    public boolean contains(int data){
        Node left = null;
        Node current = null;
        Node right = null;
        while(true){
            left = head;
            for (int level = MaxLevel; level >=0 ; level--) {
                current = left.next[level].getReference();
                while(true){
                    boolean[] markHolder = {false};
                    right = current.next[level].get(markHolder);
                    while(markHolder[0]){
                        left = current;
                        current = left.next[level].getReference();
                        right = current.next[level].get(markHolder);
                    }
                    if(current.data < data){
                        left = current;
                        current  = right;
                    } else break;

                }
            } return (data == current.data);

        }
    }

    public boolean insert(int data){
        int height = generateHeight();
        Node[] preds = new Node[MaxLevel + 1];
        Node[] succs = new Node[MaxLevel + 1];

        while(true){
            boolean found = find(data, preds, succs);
            if(found)
                return false;
            else{
                Node node = new Node(data, height);
                for (int level = 0; level <= height; level++) {
                    node.next[level].set(succs[level], false);
                }

                boolean inserted = preds[0].next[0].compareAndSet(succs[0],node, false, false);//додаємо референси на найнижчому рівні
                if (inserted==false) {
                    continue;
                }
                for (int level = 1; level <= height; level++) {
                    while (true){
                        boolean result = preds[level].next[level].compareAndSet(succs[level], node, false,false);
                        if(result)
                            break;
                        find(data, preds, succs);
                    }
                }
                return true;
            }
        }
    }

    public void print(){
        Node current = head;
        for (int i = MaxLevel; i >=0; i--) {
            System.out.println("Level" + i);
            boolean[] markHolder = {false};
            Node node = head;
            Node next = node.next[i].get(markHolder);
            while(next!=null){
                System.out.print(next.data + "\t");
                int flag = 0;
                while(markHolder[0] && next != null)
                {
                    flag = 1;
                    node = next;
                    next = next.next[i].get(markHolder);
                }
                if(flag == 0)
                {
                    next = next.next[i].get(markHolder);
                }
            }
            System.out.println();
        }
    }
    public static void main(String[] args){
        SkipList skipList = new SkipList();
        skipList.insert(15);
        skipList.insert(4);
        skipList.insert(16);
        skipList.insert(43);
        skipList.insert(38);
        skipList.insert(25);
        skipList.print();
    }
    private static int generateHeight() {
        int level = 0;
        while ((random.nextDouble() < p)&&(level < MaxLevel)){
            level++;
        }
        return level;
    }
}

