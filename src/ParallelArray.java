import java.util.Random;

public class ParallelArray{
    int threadsAmount;
    volatile NumberAndIndex minFromThreads;
    volatile int threadCount = 0;
    int[] bigArray;

    public ParallelArray(int threads, int length){
        threadsAmount = threads;
        bigArray = new int[length];
        minFromThreads = new NumberAndIndex(bigArray[0], 0);
        fillArray();
    }

    private void findMinInThreads(){
        for(int i = 0; i < threadsAmount; i++){
            int start = (i* bigArray.length)/threadsAmount;
            int end = (i +1)* bigArray.length/threadsAmount;
            new ThreadMin(this, start, end).start();
        }
    }

    public synchronized void setPartMinimum(NumberAndIndex currMin) throws InterruptedException {
        if(currMin.getNumber() < minFromThreads.getNumber())
            minFromThreads = currMin;
        threadCount++;
        notify();
    }

    synchronized public NumberAndIndex getMin() {
        findMinInThreads();
        while (threadCount < threadsAmount){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return minFromThreads;
    }
    public NumberAndIndex partMinimum(int startIndex, int endIndex){
        NumberAndIndex min = new NumberAndIndex(bigArray[startIndex], startIndex);

        for (int i = startIndex+1; i < endIndex; i++){
            if(min.getNumber() > bigArray[i]){
                min.setIndex(i);
                min.setNumber(bigArray[i]);
            }
        }
        return  min;
    }

    private  void fillArray(){
        Random ran = new Random();
        for(int i = 0; i < bigArray.length; i++){
            bigArray[i] = ran.nextInt(-100, 1000);
        }
    }
}
