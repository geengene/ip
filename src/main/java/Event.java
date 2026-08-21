/**
 * Represents a task that starts and ends at specific dates or times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates a new event task.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in the format shown to the user.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
