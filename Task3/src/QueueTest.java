import org.junit.Test;
import java.util.NoSuchElementException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

    @org.junit.jupiter.api.Test
    void pop_EmptyQueue_Exception(){
        Queue<Integer> queue = new Queue();
        assertThrows(NullPointerException.class, queue::pop);
    }


    @org.junit.jupiter.api.Test
    void QueueTest() throws InterruptedException {

        Queue<Integer> queue = new Queue();
        List<Integer> data = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            data.add(r.nextInt());
        }
        data.parallelStream().forEach(queue::push);

        for (int i = 0; i < 10; i++) {
            int res = queue.pop();
            result.add(res);
        }
        Collections.sort(data);
        Collections.sort(result);
        assertEquals(data, result);
    }


}