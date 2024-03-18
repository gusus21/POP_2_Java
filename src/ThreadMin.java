public class ThreadMin extends Thread{
    final ParallelArray arr;
    final int startIdx;
    final int endIdx;

    public ThreadMin(ParallelArray arr, int startIdx, int endIdx) {
        this.arr = arr;
        this.startIdx = startIdx;
        this.endIdx = endIdx;
    }

    @Override
    public void run() {
        NumberAndIndex min = arr.partMinimum(startIdx, endIdx);
        try {
            arr.setPartMinimum(min);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
