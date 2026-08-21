import java.util.Scanner;

public class Duke {
    private static final String LINE = "____________________________________________________________";
    private static final String CHATBOT_NAME = "geen";
    private static final String CHATBOT_BANNER = CHATBOT_NAME;
    private static final String EXIT_COMMAND = "bye";

    public static void main(String[] args) {
        printGreeting();
        echoCommands();
    }

    private static void printGreeting() {
        System.out.println(LINE);
        System.out.println(CHATBOT_BANNER);
        System.out.println("Hello! I'm " + CHATBOT_NAME + ".");
        System.out.println("What can I do for you?");
    }

    private static void echoCommands() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals(EXIT_COMMAND)) {
                printGoodbye();
                break;
            }

            printResponse(command);
        }

        scanner.close();
    }

    private static void printResponse(String command) {
        System.out.println(LINE);
        System.out.println(command);
        System.out.println(LINE);
    }

    private static void printGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
