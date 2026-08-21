import java.util.Scanner;

/**
 * Entry point for the geen chatbot.
 */
public class Duke {
    private static final String LINE = "____________________________________________________________";
    private static final String CHATBOT_NAME = "geen";
    private static final String CHATBOT_BANNER = CHATBOT_NAME;
    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND_PREFIX = "mark ";
    private static final String UNMARK_COMMAND_PREFIX = "unmark ";
    private static final int MAX_TASKS = 100;

    private static final String[] tasks = new String[MAX_TASKS];
    private static final boolean[] taskDone = new boolean[MAX_TASKS];
    private static int taskCount = 0;

    /**
     * Greets the user and handles commands until the user enters "bye".
     */
    public static void main(String[] args) {
        printGreeting();
        handleCommands();
    }

    /**
     * Prints the opening chatbot message.
     */
    private static void printGreeting() {
        System.out.println(LINE);
        System.out.println(CHATBOT_BANNER);
        System.out.println("Hello! I'm " + CHATBOT_NAME + ".");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Reads user commands and performs the requested action.
     */
    private static void handleCommands() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals(EXIT_COMMAND)) {
                printGoodbye();
                break;
            }

            if (command.equals(LIST_COMMAND)) {
                printTaskList();
            } else if (command.startsWith(MARK_COMMAND_PREFIX)) {
                markTask(command);
            } else if (command.startsWith(UNMARK_COMMAND_PREFIX)) {
                unmarkTask(command);
            } else {
                addTask(command);
            }
        }

        scanner.close();
    }

    /**
     * Stores a task and confirms that it was added.
     */
    private static void addTask(String task) {
        tasks[taskCount] = task;
        taskCount++;

        System.out.println(LINE);
        System.out.println("added: " + task);
        System.out.println(LINE);
    }

    /**
     * Marks the selected task as done.
     */
    private static void markTask(String command) {
        int taskNumber = Integer.parseInt(command.substring(MARK_COMMAND_PREFIX.length()));
        int taskIndex = taskNumber - 1;

        taskDone[taskIndex] = true;

        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  [X] " + tasks[taskIndex]);
        System.out.println(LINE);
    }

    /**
     * Marks the selected task as not done.
     */
    private static void unmarkTask(String command) {
        int taskNumber = Integer.parseInt(command.substring(UNMARK_COMMAND_PREFIX.length()));
        int taskIndex = taskNumber - 1;

        taskDone[taskIndex] = false;

        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  [ ] " + tasks[taskIndex]);
        System.out.println(LINE);
    }

    /**
     * Prints all stored tasks in the order they were added.
     */
    private static void printTaskList() {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            String statusIcon = taskDone[i] ? "X" : " ";
            System.out.println((i + 1) + ".[" + statusIcon + "] " + tasks[i]);
        }
        System.out.println(LINE);
    }

    /**
     * Prints the closing chatbot message.
     */
    private static void printGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
