import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HarrisListTest {

    @org.junit.jupiter.api.Test
    void insert_RightOrder() {
        HarrisList<Integer> l = new HarrisList<>();
        List<Integer> values = Arrays.asList(15,27,3,14,5,19,64);
        values.parallelStream().forEach(l::insert);
       assertEquals(3, l.head.next.getReference().key);

    }

    @org.junit.jupiter.api.Test
    void insert_ExistingValue_False() {
        HarrisList<Integer> l = new HarrisList<>();
        List<Integer> values = Arrays.asList(15,27,3,14,5,19,64);
        values.parallelStream().forEach(l::insert);
        assertEquals(false, l.insert(19));

    }

    @org.junit.jupiter.api.Test
    void remove_ExistingValue_True() {
        HarrisList<Integer> l = new HarrisList<>();
        List<Integer> values = Arrays.asList(15,27,3,14,5,19,64);
        values.parallelStream().forEach(l::insert);
        assertEquals(true, l.remove(19));

    }

    @org.junit.jupiter.api.Test
    void remove_NotExistingValue_False() {
        HarrisList<Integer> l = new HarrisList<>();
        List<Integer> values = Arrays.asList(15,27,3,14,5,19,64);
        values.parallelStream().forEach(l::insert);
        assertEquals(false, l.remove(23));
    }

    @org.junit.jupiter.api.Test
    void HarrisListTest() throws InterruptedException {
        HarrisList<Integer> hl = new HarrisList<>();
        Thread[] threads = new Thread[100];
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread (()->{
                int key = random.nextInt();
                hl.insert(key);
                if(Thread.currentThread().getId()%2==1)
                    hl.remove(key);
            });
        }

        for (int i = 0; i < 100; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 100; i++) {
            threads[i].join();
        }
        assertEquals(50, hl.print().size());
    }
}