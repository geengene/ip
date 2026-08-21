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
    private static final int MAX_TASKS = 100;

    private static final String[] tasks = new String[MAX_TASKS];
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
     * Prints all stored tasks in the order they were added.
     */
    private static void printTaskList() {
        System.out.println(LINE);
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
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
