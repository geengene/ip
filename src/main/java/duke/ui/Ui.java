package duke.ui;

import duke.task.Task;
import duke.task.TaskList;

/**
 * Handles all messages shown to the user.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String CHATBOT_NAME = "geen";
    private static final String CHATBOT_BANNER = CHATBOT_NAME;

    /**
     * Prints the opening chatbot message.
     */
    public void showGreeting() {
        showMessage(
                CHATBOT_BANNER,
                "Hello! I'm " + CHATBOT_NAME + ".",
                "What can I do for you?");
    }

    /**
     * Prints the closing chatbot message.
     */
    public void showGoodbye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    /**
     * Prints an error message caused by invalid user input or storage problems.
     */
    public void showError(String message) {
        showMessage("OOPS!!! " + message);
    }

    /**
     * Prints a confirmation after adding a task.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks after adding the task.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage(
                "Got it. I've added this task:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints a confirmation after deleting a task.
     *
     * @param deletedTask Task that was deleted.
     * @param taskCount Number of tasks after deleting the task.
     */
    public void showTaskDeleted(Task deletedTask, int taskCount) {
        showMessage(
                "Noted. I've removed this task:",
                "  " + deletedTask,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints a confirmation after marking a task as done.
     */
    public void showTaskMarked(Task task) {
        showMessage(
                "Nice! I've marked this task as done:",
                "  " + task);
    }

    /**
     * Prints a confirmation after marking a task as not done.
     */
    public void showTaskUnmarked(Task task) {
        showMessage(
                "OK, I've marked this task as not done yet:",
                "  " + task);
    }

    /**
     * Prints all stored tasks in the order they were added.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Prints tasks matching a find keyword.
     */
    public void showMatchingTasks(TaskList tasks) {
        System.out.println(LINE);
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Prints any number of message lines between the standard chatbot separators.
     *
     * @param lines Message lines to print in order.
     */
    private void showMessage(String... lines) {
        System.out.println(LINE);
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println(LINE);
    }
}
