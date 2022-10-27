import java.util.Comparator;
import java.util.PriorityQueue;

// Data Structure for storing events in Event List
public class EventList {
    
    public PriorityQueue<Event> list;

    public EventList() {
        list = new PriorityQueue<Event>(new EventComparator());
    }
}

class EventComparator implements Comparator<Event> {

    @Override
    public int compare(Event event1, Event event2) {
        
        if (event1.clock > event2.clock) {
            return 1;
        } else {
            return -1;
        }

    }
    
}
