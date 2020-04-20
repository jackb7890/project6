/* MULTITHREADING <MyClass.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * John Buchanan
 * jcb5464
 * 16295
 * Slip days used: <0>
 * Spring 2020
 */
package assignment6;

import java.util.ArrayList;
import java.util.List;

public class Theater {

    /**
     * the delay time you will use when print tickets
     */
    private int printDelay = 50; // 50 ms. Use it in your Thread.sleep()

    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Represents a seat in the theater A1, A2, A3, ... B1, B2, B3 ...
     */
    static class Seat {
        private int rowNum;
        private int seatNum;

        public Seat(int rowNum, int seatNum) {
            this.rowNum = rowNum;
            this.seatNum = seatNum;
        }

        public int getSeatNum() {
            return seatNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        @Override
        public String toString() {
            String result = "";
            int tempRowNumber = rowNum + 1;
            do {
                tempRowNumber--;
                result = ((char) ('A' + tempRowNumber % 26)) + result;
                tempRowNumber = tempRowNumber / 26;
            } while (tempRowNumber > 0);
            result += seatNum;
            return result;
        }
    }

    /**
     * Represents a ticket purchased by a client
     */
    static class Ticket {
        private String show;
        private String boxOfficeId;
        private Seat seat;
        private int client;
        public static final int ticketStringRowLength = 31;

        public Ticket(String show, String boxOfficeId, Seat seat, int client) {
            this.show = show;
            this.boxOfficeId = boxOfficeId;
            this.seat = seat;
            this.client = client;
        }

        public Seat getSeat() {
            return seat;
        }

        public String getShow() {
            return show;
        }

        public String getBoxOfficeId() {
            return boxOfficeId;
        }

        public int getClient() {
            return client;
        }

        @Override
        public String toString() {
            String result, dashLine, showLine, boxLine, seatLine, clientLine, eol;

            eol = System.getProperty("line.separator");

            dashLine = new String(new char[ticketStringRowLength]).replace('\0', '-');

            showLine = "| Show: " + show;
            for (int i = showLine.length(); i < ticketStringRowLength - 1; ++i) {
                showLine += " ";
            }
            showLine += "|";

            boxLine = "| Box Office ID: " + boxOfficeId;
            for (int i = boxLine.length(); i < ticketStringRowLength - 1; ++i) {
                boxLine += " ";
            }
            boxLine += "|";

            seatLine = "| Seat: " + seat.toString();
            for (int i = seatLine.length(); i < ticketStringRowLength - 1; ++i) {
                seatLine += " ";
            }
            seatLine += "|";

            clientLine = "| Client: " + client;
            for (int i = clientLine.length(); i < ticketStringRowLength - 1; ++i) {
                clientLine += " ";
            }
            clientLine += "|";

            result = dashLine + eol + showLine + eol + boxLine + eol + seatLine + eol + clientLine + eol + dashLine;

            return result;
        }
    }

    private int numRows;
    private int seatsPerRow;
    private String show;
    private ArrayList<ArrayList<Seat>> seats = new ArrayList<ArrayList<Seat>>();
    private ArrayList<Ticket> transactionLog = new ArrayList<Ticket>();
    private int clientNumber = 0;
    private boolean soldOut = false;

    public Theater(int numRows, int seatsPerRow, String show) {
        // TODO: Implement this constructor
        this.numRows = numRows;
        this.seatsPerRow = seatsPerRow;
        this.show = show;
        for (int r = 0; r < numRows; r++) {
            seats.add(new ArrayList<Seat>());
            for (int c = 0; c < seatsPerRow; c++) {
                seats.get(r).add(new Seat(r, c));
            }
        }

    }

    /**
     * Calculates the best seat not yet reserved
     *
     * @return the best seat or null if theater is full
     */
    public Seat bestAvailableSeat() {
        // TODO: Implement this method
        synchronized (this) {
            for (int r = 0; r < seats.size(); r++) {
                if (seats.get(r).size() > 0) {
                    return seats.get(r).remove(0);
                }
            }
            return null;
        }
    }

    /**
     * Prints a ticket for the client after they reserve a seat Also prints the
     * ticket to the console
     *
     * @param seat a particular seat in the theater
     * @return a ticket or null if a box office failed to reserve the seat
     */
    public Ticket printTicket(String boxOfficeId, Seat seat, int client) {
        // TODO: Implement this method
    //    synchronized (this) {
            try {
                Thread.sleep(printDelay);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (boxOfficeId == null)
                return null;
            if (seatTaken(seat))
                return null;
synchronized(this){
            Ticket ticket = new Ticket(show, boxOfficeId, seat, client);
            transactionLog.add(ticket);
            return ticket;
}
    //    }
    }

    /**
     * 
     * @param seat the seat that is being checked for availability
     * @return if the seat is available return true, false otherwise
     */
    private boolean seatTaken(Seat seat) {
        ArrayList<Seat> row = seats.get(seat.getRowNum());
        for (Seat s : row) {
            if (s.getSeatNum() == seat.getSeatNum())
                return true;
        }
        return false;
    }

    /**
     * increments client number each call
     * 
     * @return the next client number to put on ticket
     */
    public int getAndIncClient() {
        clientNumber++;
        return clientNumber - 1;
    }

    /**
     * Lists all tickets sold for this theater in order of purchase
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        return transactionLog;
    }

    public boolean getsoldOut() {
        return soldOut;
    }

    public void setSoldOutTrue() {
        soldOut = true;
    }
}
