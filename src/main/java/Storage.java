import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving tasks to disk and loading them when the chatbot starts.
 */
public class Storage {
    private static final Path DATA_FILE = Paths.get("data", "duke.txt");
    private static final String SEPARATOR = " | ";

    /**
     * Loads saved tasks from the data file. If the file does not exist yet,
     * the chatbot starts with an empty task list.
     */
    public static ArrayList<Task> loadTasks() throws DukeException {
        ArrayList<Task> loadedTasks = new ArrayList<>();

        if (!Files.exists(DATA_FILE)) {
            return loadedTasks;
        }

        try {
            List<String> lines = Files.readAllLines(DATA_FILE);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (!line.trim().isEmpty()) {
                    loadedTasks.add(parseTask(line, i + 1));
                }
            }
        } catch (IOException e) {
            throw new DukeException("I could not read the saved tasks.");
        }

        return loadedTasks;
    }

    /**
     * Saves the current tasks to the data file, creating the data folder first
     * if needed.
     */
    public static void saveTasks(ArrayList<Task> tasks) throws DukeException {
        try {
            Path folder = DATA_FILE.getParent();
            if (folder != null) {
                Files.createDirectories(folder);
            }

            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(formatTask(task));
            }
            Files.write(DATA_FILE, lines);
        } catch (IOException e) {
            throw new DukeException("I could not save the tasks.");
        }
    }

    /**
     * Converts one saved line into a task.
     */
    private static Task parseTask(String line, int lineNumber) throws DukeException {
        ArrayList<String> fields = splitFields(line);
        if (fields.size() < 3) {
            throw new DukeException("The saved task on line " + lineNumber + " is incomplete.");
        }

        String type = unescape(fields.get(0).trim(), lineNumber);
        boolean isDone = parseDoneStatus(fields.get(1).trim(), lineNumber);
        String description = unescape(fields.get(2).trim(), lineNumber);
        if (description.isEmpty()) {
            throw new DukeException("The saved task on line " + lineNumber + " has no description.");
        }

        Task task;
        if (type.equals("T") && fields.size() == 3) {
            task = new ToDo(description);
        } else if (type.equals("D") && fields.size() == 4) {
            String by = unescape(fields.get(3).trim(), lineNumber);
            if (by.isEmpty()) {
                throw new DukeException("The saved deadline on line " + lineNumber + " has no time.");
            }
            task = new Deadline(description, by);
        } else if (type.equals("E") && fields.size() == 5) {
            String from = unescape(fields.get(3).trim(), lineNumber);
            String to = unescape(fields.get(4).trim(), lineNumber);
            if (from.isEmpty() || to.isEmpty()) {
                throw new DukeException("The saved event on line " + lineNumber + " has missing times.");
            }
            task = new Event(description, from, to);
        } else {
            throw new DukeException("The saved task on line " + lineNumber + " has an invalid format.");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Returns whether a saved status field represents a completed task.
     */
    private static boolean parseDoneStatus(String value, int lineNumber) throws DukeException {
        if (value.equals("1")) {
            return true;
        }
        if (value.equals("0")) {
            return false;
        }
        throw new DukeException("The saved task on line " + lineNumber + " has an invalid done status.");
    }

    /**
     * Converts a task into one line of saved text.
     */
    private static String formatTask(Task task) throws DukeException {
        String doneValue = task.isDone() ? "1" : "0";

        if (task instanceof ToDo) {
            return String.join(SEPARATOR, "T", doneValue, escape(task.getDescription()));
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.join(SEPARATOR, "D", doneValue, escape(deadline.getDescription()), escape(deadline.getBy()));
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            return String.join(
                    SEPARATOR,
                    "E",
                    doneValue,
                    escape(event.getDescription()),
                    escape(event.getFrom()),
                    escape(event.getTo()));
        }

        throw new DukeException("I could not save an unknown task type.");
    }

    /**
     * Splits a saved line on separators while keeping escaped separators inside fields.
     */
    private static ArrayList<String> splitFields(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isEscaped = false;

        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);
            if (isEscaped) {
                field.append(current);
                isEscaped = false;
            } else if (current == '\\') {
                field.append(current);
                isEscaped = true;
            } else if (current == '|') {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(current);
            }
        }

        fields.add(field.toString());
        return fields;
    }

    /**
     * Escapes characters that have special meaning in the save file.
     */
    private static String escape(String value) {
        StringBuilder escapedValue = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (current == '\\') {
                escapedValue.append("\\\\");
            } else if (current == '|') {
                escapedValue.append("\\|");
            } else if (current == '\n') {
                escapedValue.append("\\n");
            } else {
                escapedValue.append(current);
            }
        }
        return escapedValue.toString();
    }

    /**
     * Reverses escaping used in the save file and rejects invalid escape sequences.
     */
    private static String unescape(String value, int lineNumber) throws DukeException {
        StringBuilder unescapedValue = new StringBuilder();
        boolean isEscaped = false;

        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (isEscaped) {
                if (current == 'n') {
                    unescapedValue.append('\n');
                } else if (current == '\\' || current == '|') {
                    unescapedValue.append(current);
                } else {
                    throw new DukeException("The saved task on line " + lineNumber + " has an invalid escape.");
                }
                isEscaped = false;
            } else if (current == '\\') {
                isEscaped = true;
            } else {
                unescapedValue.append(current);
            }
        }

        if (isEscaped) {
            throw new DukeException("The saved task on line " + lineNumber + " has an invalid escape.");
        }

        return unescapedValue.toString();
    }
}
