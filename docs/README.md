# geen User Guide

geen is a personal assistant chatbot that keeps track of todos, deadlines, and events.

## Viewing a schedule

Use `schedule DATE` to view deadlines due and events taking place on a specific date.
Enter the date in `yyyy-MM-dd` format.

Example: `schedule 2019-10-15`

```text
Schedule for Oct 15 2019:
1.[D][ ] return book (by: Oct 15 2019)
2.[E][ ] project meeting (from: Oct 14 2019 to: Oct 16 2019)
```

Events spanning multiple days appear on their start date, end date, and every date
between them. Todos are not shown because they have no scheduled date. Completed
deadlines and events remain visible in the schedule.

If no task is scheduled on the requested date, geen responds with:

```text
There are no scheduled tasks on Oct 15 2019.
```
