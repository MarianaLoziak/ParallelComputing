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
    void remove() {
    }

    @org.junit.jupiter.api.Test
    void contains() {
    }

    @org.junit.jupiter.api.Test
    void insert() {
        SkipList sk = new SkipList();
        sk.insert(15);
        Node n = sk.head.next[0].getReference();
        assertEquals(15,n.data);
    }
}