import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SkipListTest {

    @org.junit.jupiter.api.Test
    void find() {

        SkipList skipList = new SkipList();
        skipList.insert(15);
        skipList.insert(38);
        skipList.insert(25);
        Node[] preds = new Node[skipList.getMaxLevel() + 1];
        Node[] succs = new Node[skipList.getMaxLevel() + 1];
        assertEquals(true, skipList.find(25, preds, succs));
        assertEquals(preds[0].data, 15);
    }

    @org.junit.jupiter.api.Test
    void remove_NotExisting_False() {
        SkipList skipList = new SkipList();
        skipList.insert(15);
        skipList.insert(38);
        assertEquals(false, skipList.remove(20));

    }

    @org.junit.jupiter.api.Test
    void remove_Existing_True() {
        SkipList skipList = new SkipList();
        skipList.insert(15);
        skipList.insert(2);
        skipList.insert(38);
        assertEquals(true, skipList.remove(15));

    }
    @org.junit.jupiter.api.Test
    void insert_ExistingValue_False() {
        SkipList skipList = new SkipList();
        skipList.insert(15);
        skipList.insert(2);
        skipList.insert(38);
        assertEquals(false,skipList.insert(2));

    }
    @org.junit.jupiter.api.Test
    void insert() {
        SkipList sk = new SkipList();
        sk.insert(15);
        Node n = sk.head.next[0].getReference();
        assertEquals(15,n.data);
    }

    @org.junit.jupiter.api.Test
    void contains_NotExistingValue_False() {
        SkipList skipList = new SkipList();
        skipList.insert(15);
        skipList.insert(2);
        skipList.insert(38);
        assertEquals(false,skipList.contains(22));
    }

    @org.junit.jupiter.api.Test
    void skipList_Parallel() throws InterruptedException {
        SkipList skipList = new SkipList();
        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread (()->{

                int key = (int)Thread.currentThread().getId();
                skipList.insert(key);
                if(Thread.currentThread().getId()%2==1)
                    skipList.remove(key);
            });
        }

        for (int i = 0; i < 100; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 100; i++) {
            threads[i].join();
        }

        assertEquals(false, skipList.contains(99));
        assertEquals(true, skipList.contains(20));
        Node n = skipList.head;
        List<Integer> list = new ArrayList<>();

        while(n!=null && n.next[0].getReference()!=skipList.tail){
            n = n.next[0].getReference();
            list.add(n.data);
        }
        assertEquals(50, list.size());
    }


}