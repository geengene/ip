/**
 * Represents a task that needs to be done by a specific date or time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates a new deadline task.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline in the format shown to the user.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
