import java.util.Scanner;

/**
 * Entry point for the geen chatbot.
 */
public class Duke {
    private static final Ui ui = new Ui();
    private static TaskList tasks;

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
            tasks = new TaskList(Storage.loadTasks());
        } catch (DukeException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
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
                boolean isExit = handleCommand(command);
                if (isExit) {
                    break;
                }
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
        }

        scanner.close();
    }

    /**
     * Performs the action requested by one user command.
     */
    private static boolean handleCommand(String command) throws DukeException {
        Parser.CommandType commandType = Parser.parseCommandType(command);

        if (commandType == Parser.CommandType.EXIT) {
            ui.showGoodbye();
            return true;
        } else if (commandType == Parser.CommandType.LIST) {
            printTaskList();
        } else if (commandType == Parser.CommandType.DELETE) {
            deleteTask(command);
        } else if (commandType == Parser.CommandType.MARK) {
            markTask(command);
        } else if (commandType == Parser.CommandType.UNMARK) {
            unmarkTask(command);
        } else if (commandType == Parser.CommandType.TODO) {
            addToDo(command);
        } else if (commandType == Parser.CommandType.DEADLINE) {
            addDeadline(command);
        } else if (commandType == Parser.CommandType.EVENT) {
            addEvent(command);
        }

        return false;
    }

    /**
     * Creates a todo task from the user command.
     */
    private static void addToDo(String command) throws DukeException {
        addTask(Parser.parseToDo(command));
    }

    /**
     * Creates a deadline task from the user command.
     */
    private static void addDeadline(String command) throws DukeException {
        addTask(Parser.parseDeadline(command));
    }

    /**
     * Creates an event task from the user command.
     */
    private static void addEvent(String command) throws DukeException {
        addTask(Parser.parseEvent(command));
    }

    /**
     * Stores a task and confirms that it was added.
     */
    private static void addTask(Task task) throws DukeException {
        tasks.add(task);
        Storage.saveTasks(tasks.getTasks());

        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Deletes the selected task from the list.
     */
    private static void deleteTask(String command) throws DukeException {
        int taskIndex = Parser.parseTaskIndex(command, Parser.CommandType.DELETE, tasks.size());
        Task deletedTask = tasks.delete(taskIndex);
        Storage.saveTasks(tasks.getTasks());

        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    /**
     * Marks the selected task as done.
     */
    private static void markTask(String command) throws DukeException {
        int taskIndex = Parser.parseTaskIndex(command, Parser.CommandType.MARK, tasks.size());

        Task task = tasks.mark(taskIndex);
        Storage.saveTasks(tasks.getTasks());

        ui.showTaskMarked(task);
    }

    /**
     * Marks the selected task as not done.
     */
    private static void unmarkTask(String command) throws DukeException {
        int taskIndex = Parser.parseTaskIndex(command, Parser.CommandType.UNMARK, tasks.size());

        Task task = tasks.unmark(taskIndex);
        Storage.saveTasks(tasks.getTasks());

        ui.showTaskUnmarked(task);
    }

    /**
     * Prints all stored tasks in the order they were added.
     */
    private static void printTaskList() {
        ui.showTaskList(tasks);
    }
}
