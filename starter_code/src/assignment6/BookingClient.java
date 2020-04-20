/* MULTITHREADING <MyClass.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * Slip days used: <0>
 * Spring 2020
 */
package assignment6;

import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.lang.Thread;

public class BookingClient {
    private Map<String, Integer> office;
    private Theater theater;

    /**
     * @param office  maps box office id to number of customers in line
     * @param theater the theater where the show is playing
     */
    public BookingClient(Map<String, Integer> office, Theater theater) {
        // TODO: Implement this constructor
        this.office = office;
        this.theater = theater;
    }

    /**
     * Starts the box office simulation by creating (and starting) threads for each
     * box office to sell tickets for the given theater
     *
     * @return list of threads used in the simulation, should have as many threads
     *         as there are box offices
     */
    public List<Thread> simulate() {

        List<Thread> threadList = new ArrayList<>();

        Set<String> keys = office.keySet();
        Iterator itr = keys.iterator();
        while (itr.hasNext()) {
            String key = (String) itr.next();
            Thread t = new Thread(new Task(key, office.get(key)));
            threadList.add(t);
        }

        for (Thread t : threadList) {
            t.start();
        }

        for (Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return threadList;
    }

    private class Task implements Runnable {
        private String boxOfficeId;
        private int lineLength;

        public Task(String boxOfficeId, int lineLength) {
            this.boxOfficeId = boxOfficeId;
            this.lineLength = lineLength;
        }

        @Override
        public void run() {
            for (int i = 0; i < lineLength; i++) {
     //           synchronized (this) {
                    Theater.Seat seat = theater.bestAvailableSeat();
                    int client = theater.getAndIncClient();
                    if (theater.getsoldOut()){
                        break;
                    }
                    else if (seat == null) {
                        System.out.println("Sorry, we are sold out!");
                        theater.setSoldOutTrue();
                        break;
                    } 
                    else {
                        Theater.Ticket t = theater.printTicket(boxOfficeId, seat, client);
                        System.out.println(t);
                    }
       //         }
            }
        }
    }

    public static void main(String[] args) {
        // TODO: Initialize test data to description
        Map<String, Integer> office = new HashMap<>();
        office.put("BX1", 3);
        office.put("BX3", 3);
        office.put("BX2", 4);
        office.put("BX5", 3);
        office.put("BX4", 3);

        Theater theater = new Theater(3, 5, "Ouiji");

        BookingClient bc = new BookingClient(office, theater);
        bc.simulate();

        // try{
        //     Thread.sleep(10000);
        // }
        // catch (Exception e){

        // }
        // System.out.println("@@@@@@");

        // for (Theater.Ticket t : theater.getTransactionLog()) {
        //     System.out.println(t);
        //     System.out.println("@@@@@@");
        // }

    }
}
