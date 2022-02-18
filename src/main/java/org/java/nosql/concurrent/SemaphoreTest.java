package org.java.nosql.concurrent;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    public static void main(String[] args) {
        int N = 8;            //工人数
        Semaphore semaphore = new Semaphore(1); //准入数
        for(int i=0;i<N;i++) {
            new Worker(i,semaphore).start();
        }
    }

    static class Worker extends Thread{
        private int num;
        private Semaphore semaphore;
        public Worker(int num, Semaphore semaphore){
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();	//尝试获取一个准入的许可，若无法获取，就会线程等待
                System.out.println("工人"+this.num+"占用一个机器在生产...");
                Thread.sleep(2000);
                System.out.println("工人"+this.num+"释放出机器");
                semaphore.release();    //在线程访问结束后，释放一个许可
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
