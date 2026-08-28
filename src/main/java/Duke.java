import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for the geen chatbot.
 */
public class Duke {
    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String DELETE_COMMAND = "delete";
    private static final String DELETE_COMMAND_PREFIX = "delete ";
    private static final String MARK_COMMAND = "mark";
    private static final String MARK_COMMAND_PREFIX = "mark ";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String UNMARK_COMMAND_PREFIX = "unmark ";
    private static final String TODO_COMMAND = "todo";
    private static final String TODO_COMMAND_PREFIX = "todo ";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String DEADLINE_COMMAND_PREFIX = "deadline ";
    private static final String EVENT_COMMAND = "event";
    private static final String EVENT_COMMAND_PREFIX = "event ";
    private static final String BY_SEPARATOR = "/by";
    private static final String FROM_SEPARATOR = "/from";
    private static final String TO_SEPARATOR = "/to";

    private static final Ui ui = new Ui();
    private static final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Greets the user and handles commands until the user enters "bye".
     */
    public static void main(String[] args) {
        loadTasks();
        ui.showGreeting();
        handleCommands();
    }

    /**
     * Loads tasks saved from earlier runs.
     */
    private static void loadTasks() {
        try {
            tasks.addAll(Storage.loadTasks());
        } catch (DukeException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Reads user commands and performs the requested action.
     */
    private static void handleCommands() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            try {
                if (command.equals(EXIT_COMMAND)) {
                    ui.showGoodbye();
                    break;
                }

                handleCommand(command);
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
        }

        scanner.close();
    }

    /**
     * Performs the action requested by one user command.
     */
    private static void handleCommand(String command) throws DukeException {
        if (command.equals(LIST_COMMAND)) {
            printTaskList();
        } else if (command.equals(DELETE_COMMAND) || command.startsWith(DELETE_COMMAND_PREFIX)) {
            deleteTask(command);
        } else if (command.equals(MARK_COMMAND) || command.startsWith(MARK_COMMAND_PREFIX)) {
            markTask(command);
        } else if (command.equals(UNMARK_COMMAND) || command.startsWith(UNMARK_COMMAND_PREFIX)) {
            unmarkTask(command);
        } else if (command.equals(TODO_COMMAND) || command.startsWith(TODO_COMMAND_PREFIX)) {
            addToDo(command);
        } else if (command.equals(DEADLINE_COMMAND) || command.startsWith(DEADLINE_COMMAND_PREFIX)) {
            addDeadline(command);
        } else if (command.equals(EVENT_COMMAND) || command.startsWith(EVENT_COMMAND_PREFIX)) {
            addEvent(command);
        } else {
            throw new DukeException("Sorry, I don't understand that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
        }
    }

    /**
     * Creates a todo task from the user command.
     */
    private static void addToDo(String command) throws DukeException {
        String description = getCommandDetails(command, TODO_COMMAND, TODO_COMMAND_PREFIX);
        if (description.isEmpty()) {
            throw new DukeException("A todo needs a description. For example: todo read book");
        }

        addTask(new ToDo(description));
    }

    /**
     * Creates a deadline task from the user command.
     */
    private static void addDeadline(String command) throws DukeException {
        String details = getCommandDetails(command, DEADLINE_COMMAND, DEADLINE_COMMAND_PREFIX);
        int byIndex = details.indexOf(BY_SEPARATOR);

        if (byIndex < 0) {
            throw new DukeException("A deadline needs /by. For example: deadline submit report /by 2019-10-15");
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + BY_SEPARATOR.length()).trim();
        if (description.isEmpty()) {
            throw new DukeException("A deadline needs a description before /by.");
        }
        if (by.isEmpty()) {
            throw new DukeException("A deadline needs a date after /by.");
        }

        addTask(new Deadline(description, parseDate(by, "deadline submit report /by 2019-10-15")));
    }

    /**
     * Creates an event task from the user command.
     */
    private static void addEvent(String command) throws DukeException {
        String details = getCommandDetails(command, EVENT_COMMAND, EVENT_COMMAND_PREFIX);
        int fromIndex = details.indexOf(FROM_SEPARATOR);
        int toIndex = details.indexOf(TO_SEPARATOR, fromIndex + FROM_SEPARATOR.length());

        if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
            throw new DukeException("An event needs /from and /to. For example: event meeting /from 2019-10-15 /to 2019-10-16");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + FROM_SEPARATOR.length(), toIndex).trim();
        String to = details.substring(toIndex + TO_SEPARATOR.length()).trim();
        if (description.isEmpty()) {
            throw new DukeException("An event needs a description before /from.");
        }
        if (from.isEmpty()) {
            throw new DukeException("An event needs a start date after /from.");
        }
        if (to.isEmpty()) {
            throw new DukeException("An event needs an end date after /to.");
        }

        addTask(new Event(
                description,
                parseDate(from, "event meeting /from 2019-10-15 /to 2019-10-16"),
                parseDate(to, "event meeting /from 2019-10-15 /to 2019-10-16")));
    }

    /**
     * Parses a date typed by the user in yyyy-MM-dd format.
     */
    private static LocalDate parseDate(String dateText, String exampleCommand) throws DukeException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new DukeException("Dates must be in yyyy-MM-dd format. For example: " + exampleCommand);
        }
    }

    /**
     * Returns the details typed after a command word.
     */
    private static String getCommandDetails(String command, String commandWord, String commandPrefix) {
        if (command.equals(commandWord)) {
            return "";
        }

        return command.substring(commandPrefix.length()).trim();
    }

    /**
     * Stores a task and confirms that it was added.
     */
    private static void addTask(Task task) throws DukeException {
        tasks.add(task);
        Storage.saveTasks(tasks);

        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Deletes the selected task from the list.
     */
    private static void deleteTask(String command) throws DukeException {
        int taskIndex = parseTaskIndex(command, DELETE_COMMAND, DELETE_COMMAND_PREFIX);
        Task deletedTask = tasks.remove(taskIndex);
        Storage.saveTasks(tasks);

        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    /**
     * Marks the selected task as done.
     */
    private static void markTask(String command) throws DukeException {
        int taskIndex = parseTaskIndex(command, MARK_COMMAND, MARK_COMMAND_PREFIX);

        tasks.get(taskIndex).markAsDone();
        Storage.saveTasks(tasks);

        ui.showTaskMarked(tasks.get(taskIndex));
    }

    /**
     * Marks the selected task as not done.
     */
    private static void unmarkTask(String command) throws DukeException {
        int taskIndex = parseTaskIndex(command, UNMARK_COMMAND, UNMARK_COMMAND_PREFIX);

        tasks.get(taskIndex).markAsNotDone();
        Storage.saveTasks(tasks);

        ui.showTaskUnmarked(tasks.get(taskIndex));
    }

    /**
     * Returns the zero-based task index from commands such as "mark 2".
     */
    private static int parseTaskIndex(String command, String commandWord, String commandPrefix) throws DukeException {
        String taskNumberText = getCommandDetails(command, commandWord, commandPrefix);
        if (taskNumberText.isEmpty()) {
            throw new DukeException("Please include a task number. For example: " + commandWord + " 2");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new DukeException("Task numbers must be whole numbers. For example: " + commandWord + " 2");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DukeException("Task " + taskNumber + " does not exist. Use list to see the available task numbers.");
        }

        return taskNumber - 1;
    }

    /**
     * Prints all stored tasks in the order they were added.
     */
    private static void printTaskList() {
        ui.showTaskList(tasks);
    }
}
