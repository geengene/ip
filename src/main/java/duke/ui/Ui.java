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
        System.out.println(LINE);
        System.out.println(CHATBOT_BANNER);
        System.out.println("Hello! I'm " + CHATBOT_NAME + ".");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Prints the closing chatbot message.
     */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Prints an error message caused by invalid user input or storage problems.
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println("OOPS!!! " + message);
        System.out.println(LINE);
    }

    /**
     * Prints a confirmation after adding a task.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks after adding the task.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Prints a confirmation after deleting a task.
     *
     * @param deletedTask Task that was deleted.
     * @param taskCount Number of tasks after deleting the task.
     */
    public void showTaskDeleted(Task deletedTask, int taskCount) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + deletedTask);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Prints a confirmation after marking a task as done.
     */
    public void showTaskMarked(Task task) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Prints a confirmation after marking a task as not done.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(LINE);
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
}
