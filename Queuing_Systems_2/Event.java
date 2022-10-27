// Data Structure for describing event
public class Event {
    
    // Event Types
    // Arr - Arrival Event
    // DepQ1High - Departure event of high priority customer from queue 1
    // DepQ1Low - Departure event of low priority customer from queue 1
    // DepQ2High - Departure event of high priority customer from queue 2
    // DepQ2Low - Departure event of low priority customer from queue 2
    String eventType; // Five Event types
    double clock; // time at which event takes place

    // initializing parameters for the event
    public Event(String eventType, double clock) {
        this.eventType = eventType;
        this.clock = clock;
    }
}
