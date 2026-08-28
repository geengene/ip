package duke.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests task list operations.
 */
public class TaskListTest {
    @Test
    public void add_emptyList_addsTaskToEnd() {
        TaskList tasks = new TaskList();

        tasks.add(new ToDo("read book"));

        assertEquals(1, tasks.size());
        assertEquals("[T][ ] read book", tasks.get(0).toString());
    }

    @Test
    public void delete_multipleTasks_removesSelectedTaskAndRenumbersRemainingTasks() {
        TaskList tasks = new TaskList();
        tasks.add(new ToDo("read book"));
        tasks.add(new ToDo("return book"));
        tasks.add(new ToDo("write report"));

        Task deletedTask = tasks.delete(1);

        assertEquals("[T][ ] return book", deletedTask.toString());
        assertEquals(2, tasks.size());
        assertEquals("[T][ ] read book", tasks.get(0).toString());
        assertEquals("[T][ ] write report", tasks.get(1).toString());
    }

    @Test
    public void markAndUnmark_existingTask_updatesDoneStatus() {
        TaskList tasks = new TaskList();
        tasks.add(new ToDo("read book"));

        Task markedTask = tasks.mark(0);

        assertEquals("[T][X] read book", markedTask.toString());
        assertEquals("[T][X] read book", tasks.get(0).toString());

        Task unmarkedTask = tasks.unmark(0);

        assertEquals("[T][ ] read book", unmarkedTask.toString());
        assertEquals("[T][ ] read book", tasks.get(0).toString());
    }
}
