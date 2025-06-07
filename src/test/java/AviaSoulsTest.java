import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Comparator;

public class AviaSoulsTest {

    // 1. Тесты на add, findAll
    @Test
    public void shouldAddAndReturnAllTickets() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("A", "B", 1, 0, 1);
        Ticket t2 = new Ticket("X", "Y", 2, 9, 13);
        manager.add(t1);
        manager.add(t2);
        assertArrayEquals(new Ticket[]{t1, t2}, manager.findAll());
    }

    // 2. search, когда 0 билетов
    @Test
    public void searchNoTickets() {
        AviaSouls manager = new AviaSouls();
        assertArrayEquals(new Ticket[0], manager.search("a", "b"));
    }

    // 3. search, когда 1 билет подходит
    @Test
    public void searchOneTicket() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("X", "Y", 100, 1, 4);
        manager.add(new Ticket("A", "B", 200, 0, 1));
        manager.add(t1);
        assertArrayEquals(new Ticket[]{t1}, manager.search("X", "Y"));
    }

    // 4. search, когда несколько билетов (разный порядок; проверяем сортировку)
    @Test
    public void searchMultipleTicketsSorted() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("K", "L", 500, 0, 5);
        Ticket t2 = new Ticket("K", "L", 100, 1, 7);
        Ticket t3 = new Ticket("K", "L", 300, 2, 8);
        manager.add(t1);
        manager.add(t2);
        manager.add(t3);
        // сортировка по цене
        assertArrayEquals(new Ticket[]{t2, t3, t1}, manager.search("K", "L"));
    }

    // 5. search, не найдено совпадений — ищем несуществующий маршрут
    @Test
    public void searchNotFoundDifferentRoute() {
        AviaSouls manager = new AviaSouls();
        manager.add(new Ticket("M", "N", 123, 3, 9));
        assertArrayEquals(new Ticket[0], manager.search("Q", "W"));
    }

    // 6. searchAndSortBy с нуля билетов
    @Test
    public void searchAndSortByEmpty() {
        AviaSouls manager = new AviaSouls();
        assertArrayEquals(new Ticket[0], manager.searchAndSortBy("a", "b", new TicketTimeComparator()));
    }

    // 7. searchAndSortBy один билет
    @Test
    public void searchAndSortByOne() {
        AviaSouls manager = new AviaSouls();
        Ticket t = new Ticket("A", "B", 999, 6, 7);
        manager.add(t);
        assertArrayEquals(new Ticket[]{t}, manager.searchAndSortBy("A", "B", new TicketTimeComparator()));
    }

    // 8. searchAndSortBy несколько билетов (разный flight time)
    @Test
    public void searchAndSortBySeveralFlightTime() {
        AviaSouls manager = new AviaSouls();

        Ticket t1 = new Ticket("S", "T", 10, 10, 20); // 10h
        Ticket t2 = new Ticket("S", "T", 8, 0, 1);    // 1h
        Ticket t3 = new Ticket("S", "T", 11, 5, 10);  // 5h
        manager.add(t1);
        manager.add(t2);
        manager.add(t3);

        TicketTimeComparator cmp = new TicketTimeComparator();
        Ticket[] expected = {t2, t3, t1};
        assertArrayEquals(expected, manager.searchAndSortBy("S", "T", cmp));
    }

    // 9. searchAndSortBy — времена полета равны, сортировка стабильная
    @Test
    public void searchAndSortByEqualTime() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("F", "G", 50, 0, 2);  // 2ч
        Ticket t2 = new Ticket("F", "G", 60, 2, 4);  // 2ч
        Ticket t3 = new Ticket("F", "G", 55, 2, 4);  // 2ч
        manager.add(t2); manager.add(t1); manager.add(t3);

        TicketTimeComparator cmp = new TicketTimeComparator();
        Ticket[] actual = manager.searchAndSortBy("F", "G", cmp);
        // для стабильности — сортируем и actual, и expected вручную по цене
        Ticket[] expected = {t1, t3, t2};
        Arrays.sort(actual, (a, b) -> Integer.compare(a.getPrice(), b.getPrice()));
        Arrays.sort(expected, (a, b) -> Integer.compare(a.getPrice(), b.getPrice()));
        assertArrayEquals(expected, actual);
    }

    // 10. add дубликатов
    @Test
    public void shouldAddDuplicates() {
        AviaSouls manager = new AviaSouls();
        Ticket t1 = new Ticket("T", "V", 42, 1, 3);
        Ticket t2 = new Ticket("T", "V", 42, 1, 3);
        manager.add(t1);
        manager.add(t2);
        assertArrayEquals(new Ticket[]{t1, t2}, manager.search("T","V"));
    }

    // Ticket (equals/hashCode/getters)

    @Test
    public void testGetters() {
        Ticket t = new Ticket("A", "B", 666, 7, 8);
        assertEquals("A", t.getFrom());
        assertEquals("B", t.getTo());
        assertEquals(666, t.getPrice());
        assertEquals(7, t.getTimeFrom());
        assertEquals(8, t.getTimeTo());
    }

    // Покрываем все ветки equals:
    @Test
    public void ticketEqualsSelf() {
        Ticket t = new Ticket("A", "B", 1, 2, 3);
        assertTrue(t.equals(t));
    }

    @Test
    public void ticketNotEqualsNull() {
        Ticket t = new Ticket("A", "B", 1, 2, 3);
        assertFalse(t.equals(null));
    }

    @Test
    public void ticketNotEqualsOtherClass() {
        Ticket t = new Ticket("A", "B", 1, 2, 3);
        assertFalse(t.equals("not a ticket"));
    }

    @Test
    public void ticketEqualsAllFieldsSame() {
        Ticket t1 = new Ticket("A", "B", 1, 2, 5);
        Ticket t2 = new Ticket("A", "B", 1, 2, 5);
        assertTrue(t1.equals(t2));
    }

    // Проверяем ветки equals по каждому из полей:
    @Test
    public void shouldDifferIfFromDifferent() {
        Ticket t1 = new Ticket("A", "B", 1, 2, 5);
        Ticket t2 = new Ticket("Z", "B", 1, 2, 5);
        assertFalse(t1.equals(t2));
    }

    @Test
    public void shouldDifferIfToDifferent() {
        Ticket t1 = new Ticket("A", "B", 1, 2, 5);
        Ticket t2 = new Ticket("A", "C", 1, 2, 5);
        assertFalse(t1.equals(t2));
    }

    @Test
    public void shouldDifferIfPriceDifferent() {
        Ticket t1 = new Ticket("A", "B", 1, 2, 5);
        Ticket t2 = new Ticket("A", "B", 2, 2, 5);
        assertFalse(t1.equals(t2));
    }

    @Test
    public void shouldDifferIfTimeFromDifferent() {
        Ticket t1 = new Ticket("A", "B", 1, 2, 5);
        Ticket t2 = new Ticket("A", "B", 1, 3, 5);
        assertFalse(t1.equals(t2));
    }

    @Test
    public void shouldDifferIfTimeToDifferent() {
        Ticket t1 = new Ticket("A", "B", 1, 2, 5);
        Ticket t2 = new Ticket("A", "B", 1, 2, 6);
        assertFalse(t1.equals(t2));
    }

    // Покрываем hashCode
    @Test
    public void ticketHashCodeEqualIfEquals() {
        Ticket t1 = new Ticket("A", "B", 1, 2, 5);
        Ticket t2 = new Ticket("A", "B", 1, 2, 5);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    // compareTo, compare в компараторе и ветка == 0, >0, <0 
    @Test
    public void ticketCompareTo() {
        Ticket t1 = new Ticket("A", "B", 10, 1, 2);
        Ticket t2 = new Ticket("A", "B", 8, 1, 2);
        Ticket t3 = new Ticket("A", "B", 10, 1, 2);
        assertTrue(t1.compareTo(t2) > 0);
        assertTrue(t2.compareTo(t1) < 0);
        assertEquals(0, t1.compareTo(t3));
    }

    @Test
    public void ticketTimeComparatorBranches() {
        Ticket t1 = new Ticket("A", "B", 1, 2, 4); // 2ч
        Ticket t2 = new Ticket("A", "B", 2, 8, 12); // 4ч
        Ticket t3 = new Ticket("A", "B", 3, 10, 12); // 2ч
        TicketTimeComparator cmp = new TicketTimeComparator();
        assertTrue(cmp.compare(t1, t2) < 0); // 2 < 4
        assertTrue(cmp.compare(t2, t1) > 0); // 4 > 2
        assertEquals(0, cmp.compare(t1, t3)); // 2 == 2
    }

    // from совпадает, to не совпадает — должен вернуть пустой массив
    @Test
    public void searchFromMatchesToNotMatches() {
        AviaSouls manager = new AviaSouls();
        Ticket t = new Ticket("A", "Z", 100, 1, 2);
        manager.add(t);
        assertArrayEquals(new Ticket[0], manager.search("A", "B"));
    }

    // to совпадает, from не совпадает — должен вернуть пустой массив
    @Test
    public void searchToMatchesFromNotMatches() {
        AviaSouls manager = new AviaSouls();
        Ticket t = new Ticket("X", "B", 100, 1, 2);
        manager.add(t);
        assertArrayEquals(new Ticket[0], manager.search("A", "B"));
    }

    // Для searchAndSortBy аналогичные тесты
    @Test
    public void searchAndSortBy_FromMatchesToNotMatches() {
        AviaSouls manager = new AviaSouls();
        Ticket t = new Ticket("A", "Z", 100, 1, 2);
        manager.add(t);
        assertArrayEquals(new Ticket[0], manager.searchAndSortBy("A", "B", new TicketTimeComparator()));
    }

    @Test
    public void searchAndSortBy_ToMatchesFromNotMatches() {
        AviaSouls manager = new AviaSouls();
        Ticket t = new Ticket("X", "B", 100, 1, 2);
        manager.add(t);
        assertArrayEquals(new Ticket[0], manager.searchAndSortBy("A", "B", new TicketTimeComparator()));
    }
}
