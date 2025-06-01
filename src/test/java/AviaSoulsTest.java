import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AviaSoulsTest {

    // Тесты для класса AviaSouls
    @Test
    public void testAddAndSearchByPrice() {
        AviaSouls manager = new AviaSouls();
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        Ticket ticket2 = new Ticket("A", "B", 200, 13, 15);
        Ticket ticket3 = new Ticket("A", "C", 150, 11, 13);

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);

        Ticket[] result = manager.search("A", "B");
        assertEquals(2, result.length);
        assertEquals(ticket1, result[0]);
        assertEquals(ticket2, result[1]);
    }

    @Test
    public void testSearchAndSortByTime() {
        AviaSouls manager = new AviaSouls();
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12); // 2 hours
        Ticket ticket2 = new Ticket("A", "B", 200, 13, 16); // 3 hours
        Ticket ticket3 = new Ticket("A", "B", 150, 14, 15); // 1 hour

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);

        TicketTimeComparator timeComparator = new TicketTimeComparator();
        Ticket[] result = manager.searchAndSortBy("A", "B", timeComparator);

        assertEquals(3, result.length);
        assertEquals(ticket3, result[0]); // 1 hour
        assertEquals(ticket1, result[1]); // 2 hours
        assertEquals(ticket2, result[2]); // 3 hours
    }

    @Test
    public void testSearchNoTickets() {
        AviaSouls manager = new AviaSouls();
        Ticket[] result = manager.search("A", "B");
        assertEquals(0, result.length); // Ожидаем, что не будет найдено ни одного билета
    }

    @Test
    public void testSearchMultipleTicketsWithSamePrice() {
        AviaSouls manager = new AviaSouls();
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        Ticket ticket2 = new Ticket("A", "B", 100, 13, 15);

        manager.add(ticket1);
        manager.add(ticket2);

        Ticket[] result = manager.search("A", "B");
        assertEquals(2, result.length); // Ожидаем, что найдутся оба билета
        assertEquals(ticket1, result[0]); // Проверяем порядок, если он важен
        assertEquals(ticket2, result[1]);
    }

    @Test
    public void testSearchWithDifferentAirports() {
        AviaSouls manager = new AviaSouls();
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        Ticket ticket2 = new Ticket("B", "A", 150, 13, 15);

        manager.add(ticket1);
        manager.add(ticket2);

        Ticket[] result = manager.search("A", "B");
        assertEquals(1, result.length); // Ожидаем, что найдется только один билет
        assertEquals(ticket1, result[0]);
    }

    @Test
    public void testSearchWithNonExistentAirports() {
        AviaSouls manager = new AviaSouls();
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        manager.add(ticket1);

        Ticket[] result = manager.search("X", "Y"); // Поиск по несуществующим аэропортам
        assertEquals(0, result.length); // Ожидаем, что не будет найдено ни одного билета
    }

    @Test
    public void testAddDuplicateTickets() {
        AviaSouls manager = new AviaSouls();
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        Ticket ticket2 = new Ticket("A", "B", 100, 10, 12); // Дубликат

        manager.add(ticket1);
        manager.add(ticket2);

        Ticket[] result = manager.search("A", "B");
        assertEquals(2, result.length); // Ожидаем, что оба билета будут найдены
    }

    @Test
    public void testEmptyManagerSearch() {
        AviaSouls manager = new AviaSouls();
        Ticket[] result = manager.findAll();
        assertEquals(0, result.length); // Ожидаем, что в пустом менеджере нет билетов
    }

    @Test
    public void testSearchAndSortByTimeWithNoTickets() {
        AviaSouls manager = new AviaSouls();
        TicketTimeComparator timeComparator = new TicketTimeComparator();
        Ticket[] result = manager.searchAndSortBy("A", "B", timeComparator);
        assertEquals(0, result.length); // Ожидаем, что не будет найдено ни одного билета
    }

    @Test
    public void testSearchAndSortByTimeWithOneTicket() {
        AviaSouls manager = new AviaSouls();
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        manager.add(ticket1);

        TicketTimeComparator timeComparator = new TicketTimeComparator();
        Ticket[] result = manager.searchAndSortBy("A", "B", timeComparator);
        assertEquals(1, result.length); // Ожидаем, что найден только один билет
        assertEquals(ticket1, result[0]); // Проверяем, что билет правильный
    }

    @Test
    public void testSearchAndSortByTimeWithMultipleTickets() {
        AviaSouls manager = new AviaSouls();
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12); // 2 hours
        Ticket ticket2 = new Ticket("A", "B", 200, 15, 16); // 1 hour
        Ticket ticket3 = new Ticket("A", "B", 150, 11, 13); // 2 hours

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);

        TicketTimeComparator timeComparator = new TicketTimeComparator();
        Ticket[] result = manager.searchAndSortBy("A", "B", timeComparator);

        assertEquals(3, result.length); // Ожидаем 3 билета
        assertEquals(ticket2, result[0]); // 1 hour
        assertEquals(ticket1, result[1]); // 2 hours
        assertEquals(ticket3, result[2]); // 2 hours
    }

    // Тесты для класса Ticket
    @Test
    public void testTicketCreation() {
        Ticket ticket = new Ticket("A", "B", 100, 10, 12);
        assertNotNull(ticket);
        assertEquals("A", ticket.getFrom());
        assertEquals("B", ticket.getTo());
        assertEquals(100, ticket.getPrice());
        assertEquals(10, ticket.getTimeFrom());
        assertEquals(12, ticket.getTimeTo());
    }

    @Test
    public void testEqualsSameObject() {
        Ticket ticket = new Ticket("A", "B", 100, 10, 12);
        assertTrue(ticket.equals(ticket)); // Сравнение с самим собой
    }

    @Test
    public void testEqualsDifferentObjects() {
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        Ticket ticket2 = new Ticket("A", "B", 100, 10, 12);
        assertTrue(ticket1.equals(ticket2)); // Ожидаем, что два одинаковых билета равны
    }

    @Test
    public void testNotEqualsDifferentTickets() {
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        Ticket ticket2 = new Ticket("A", "C", 100, 10, 12); // Разные направления
        assertFalse(ticket1.equals(ticket2));

        Ticket ticket3 = new Ticket("A", "B", 200, 10, 12); // Разные цены
        assertFalse(ticket1.equals(ticket3));

        Ticket ticket4 = new Ticket("A", "B", 100, 11, 12); // Разное время вылета
        assertFalse(ticket1.equals(ticket4));
    }

    @Test
    public void testEqualsNull() {
        Ticket ticket = new Ticket("A", "B", 100, 10, 12);
        assertFalse(ticket.equals(null)); // Ожидаем, что сравнение с null дает false
    }

    @Test
    public void testEqualsDifferentClass() {
        Ticket ticket = new Ticket("A", "B", 100, 10, 12);
        assertFalse(ticket.equals("Not a Ticket")); // Ожидаем, что сравнение с объектом другого класса дает false
    }

    @Test
    public void testHashCode() {
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        Ticket ticket2 = new Ticket("A", "B", 100, 10, 12);
        assertEquals(ticket1.hashCode(), ticket2.hashCode()); // Ожидаем, что хэш-коды равны
    }

    @Test
    public void testCompareTo() {
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        Ticket ticket2 = new Ticket("A", "B", 200, 10, 12);
        Ticket ticket3 = new Ticket("A", "B", 50, 10, 12);

        assertTrue(ticket1.compareTo(ticket2) < 0); // ticket1 дешевле ticket2
        assertTrue(ticket2.compareTo(ticket1) > 0); // ticket2 дороже ticket1
        assertTrue(ticket1.compareTo(ticket3) > 0); // ticket1 дороже ticket3
    }

    @Test
    public void testCompareToEqualTickets() {
        Ticket ticket1 = new Ticket("A", "B", 100, 10, 12);
        Ticket ticket2 = new Ticket("A", "B", 100, 10, 12);
        assertEquals(0, ticket1.compareTo(ticket2)); // Ожидаем, что они равны
    }
}

