import java.util.Comparator;

public class TicketTimeComparator implements Comparator<Ticket> {
    @Override
    public int compare(Ticket t1, Ticket t2) {
        int flightTime1 = t1.getTimeTo() - t1.getTimeFrom(); // Время полета первого билета
        int flightTime2 = t2.getTimeTo() - t2.getTimeFrom(); // Время полета второго билета
        return Integer.compare(flightTime1, flightTime2); // Сравниваем время полёта
    }
}
