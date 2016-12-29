package controller;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import static controller.ApachePastvuLoader.getCountOfClaim;
import static controller.ApachePastvuLoader.getStatuses;

/**
 * Created by Ilya Evlampiev on 09.12.2015.
 */
@SuppressWarnings("Since15")
public class ApachePastvuOrchestrator {
    static int counter=1579942;
    static final int MAXCOUNTER = 1579950;

    static Queue<Integer> forgotten=new ArrayDeque<Integer>(){};
    public static void main(String [] args) throws InterruptedException {
        Thread ap = null;
        for (int i=0;i<100;i++) {
            ap = new ApachePastvuLoader();
            ap.sleep(500);
            ap.start();
        }
        if(ap.isAlive()) System.out.println("Подождите. Производятся вычисления...");
        ap.join();
        Map<Integer, String> statuses = getStatuses();
        Iterator<String> statusText = statuses.values().iterator();
        long sum = 0;
        for (Iterator<Integer> iter = statuses.keySet().iterator(); iter.hasNext();) {
            Integer status = iter.next();
            String statusT = statusText.next();
            long count = getCountOfClaim(status);
            sum += count;
            System.out.println("Заявок со статусом '" + statusT + "': " + count);
        }
        System.out.println("Всего заявок: " + sum);
    }

    synchronized static int getNext()
    {
        if (!forgotten.isEmpty())
        {
            return forgotten.poll();
        }
       counter++;
       return counter;
    }

    synchronized static void addFailed(int failnum)
    {
        forgotten.add(failnum);
    }
}
