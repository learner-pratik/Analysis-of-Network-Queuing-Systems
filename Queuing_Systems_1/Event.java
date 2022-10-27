// Data Structure for describing event
public class Event {
    
    // Event Types
    // ARR1 - Arrival Event from first machine
    // ARR2 - Arrival Event from second machine
    // DEP - Departure Event from system
    String eventType; // Three Event types - ARR1, ARR2, DEP
    double clock; // time at which event takes place

    // initializing parameters for the event
    public Event(String eventType, double clock) {
        this.eventType = eventType;
        this.clock = clock;
    }
}
