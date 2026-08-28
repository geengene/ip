package duke;

import duke.exception.DukeException;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;

import java.util.Scanner;

/**
 * Entry point for the geen chatbot.
 */
public class Duke {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a chatbot that saves tasks in the given file.
     */
    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = loadTasks();
    }

    /**
     * Loads tasks saved from earlier runs.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (DukeException e) {
            ui.showError(e.getMessage());
            return new TaskList();
        }
    }

    /**
     * Greets the user and handles commands until the user enters "bye".
     */
    public void run() {
        ui.showGreeting();
        handleCommands();
    }

    /**
     * Starts the chatbot.
     */
    public static void main(String[] args) {
        new Duke("data/duke.txt").run();
    }

    /**
     * Reads user commands and performs the requested action.
     */
    private void handleCommands() {
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
    private boolean handleCommand(String command) throws DukeException {
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
        } else if (commandType == Parser.CommandType.FIND) {
            findTasks(command);
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
    private void addToDo(String command) throws DukeException {
        addTask(Parser.parseToDo(command));
    }

    /**
     * Finds tasks matching the keyword in the user command.
     */
    private void findTasks(String command) throws DukeException {
        String keyword = Parser.parseFindKeyword(command);
        ui.showMatchingTasks(tasks.find(keyword));
    }

    /**
     * Creates a deadline task from the user command.
     */
    private void addDeadline(String command) throws DukeException {
        addTask(Parser.parseDeadline(command));
    }

    /**
     * Creates an event task from the user command.
     */
    private void addEvent(String command) throws DukeException {
        addTask(Parser.parseEvent(command));
    }

    /**
     * Stores a task and confirms that it was added.
     */
    private void addTask(Task task) throws DukeException {
        tasks.add(task);
        storage.saveTasks(tasks.getTasks());

        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Deletes the selected task from the list.
     */
    private void deleteTask(String command) throws DukeException {
        int taskIndex = Parser.parseTaskIndex(command, Parser.CommandType.DELETE, tasks.size());
        Task deletedTask = tasks.delete(taskIndex);
        storage.saveTasks(tasks.getTasks());

        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    /**
     * Marks the selected task as done.
     */
    private void markTask(String command) throws DukeException {
        int taskIndex = Parser.parseTaskIndex(command, Parser.CommandType.MARK, tasks.size());

        Task task = tasks.mark(taskIndex);
        storage.saveTasks(tasks.getTasks());

        ui.showTaskMarked(task);
    }

    /**
     * Marks the selected task as not done.
     */
    private void unmarkTask(String command) throws DukeException {
        int taskIndex = Parser.parseTaskIndex(command, Parser.CommandType.UNMARK, tasks.size());

        Task task = tasks.unmark(taskIndex);
        storage.saveTasks(tasks.getTasks());

        ui.showTaskUnmarked(task);
    }

    /**
     * Prints all stored tasks in the order they were added.
     */
    private void printTaskList() {
        ui.showTaskList(tasks);
    }
}
