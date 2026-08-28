package duke.task;

import java.util.ArrayList;

/**
 * Stores the chatbot's tasks and provides operations for changing the list.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list using tasks loaded from storage.
     *
     * @param tasks Tasks to use as the initial list content.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes and returns the task at the given zero-based index.
     *
     * @param taskIndex Zero-based index of the task to delete.
     * @return Deleted task.
     */
    public Task delete(int taskIndex) {
        return tasks.remove(taskIndex);
    }

    /**
     * Marks the task at the given zero-based index as done.
     *
     * @param taskIndex Zero-based index of the task to mark.
     * @return Marked task.
     */
    public Task mark(int taskIndex) {
        Task task = tasks.get(taskIndex);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at the given zero-based index as not done.
     *
     * @param taskIndex Zero-based index of the task to unmark.
     * @return Unmarked task.
     */
    public Task unmark(int taskIndex) {
        Task task = tasks.get(taskIndex);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the task at the given zero-based index.
     */
    public Task get(int taskIndex) {
        return tasks.get(taskIndex);
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list for storage and display code.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
