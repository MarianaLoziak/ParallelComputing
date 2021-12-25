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
    void casWaitTest() {
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
    void casNotifyAll() {
    }
}