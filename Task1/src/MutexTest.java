import java.util.ConcurrentModificationException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class MutexTest {
    public Mutex mutex = new Mutex();
    public int amount = 10;
    public int max = 10;

    @org.junit.jupiter.api.Test
    void lockTest() {
        mutex.lock();
        assertEquals(mutex.getCurrentThread().get(), Thread.currentThread());
    }

    @org.junit.jupiter.api.Test
    void unlockTest() {
        mutex.lock();
        mutex.unlock();
        assertEquals(mutex.getCurrentThread().get(), null);
    }

    @org.junit.jupiter.api.Test
    void casWait_NotLockedBefore_Exception() {
        assertThrows(ConcurrentModificationException.class,()->{
            mutex.casWait();
        } );
    }

    @org.junit.jupiter.api.Test
    void casNotifyTest() throws InterruptedException {
        amount = 10;
        Thread thread1 = new Thread(() ->{
            try {
                mutex.lock();
                while (amount >= max){
                    mutex.casWait();
                }
                amount++;
                mutex.casNotifyAll();
                mutex.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2= new Thread(() ->{
            try {
                mutex.lock();
                while (amount == 0){
                    mutex.casWait();
                }
                amount--;
                mutex.casNotifyAll();
                mutex.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        assertEquals(mutex.getWaitedThreads().contains(thread1), false);
    }

    @org.junit.jupiter.api.Test
    void casNotifyAll() throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 9; i++) {
            threads[i] = new Thread(() ->{
                try {
                    increment(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        threads[9] = new Thread(() ->{
            try {
                mutex.lock();
                mutex.casNotifyAll();
                mutex.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }
        assertEquals(9, count.intValue());
    }
    public void increment(AtomicInteger count) throws InterruptedException {
        mutex.lock();
        mutex.casWait();
        count.incrementAndGet();
        mutex.unlock();

    }
}