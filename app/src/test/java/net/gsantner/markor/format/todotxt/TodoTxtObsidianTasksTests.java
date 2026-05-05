package net.gsantner.markor.format.todotxt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for Obsidian Tasks plugin format support in TodoTxtTask.
 * Obsidian Tasks uses markdown checkboxes (- [ ] / - [x]) and emoji metadata.
 */
public class TodoTxtObsidianTasksTests {

    // ---- isObsidianTaskLine ----

    @Test
    public void testIsObsidianTaskLine_unchecked() {
        assertThat(TodoTxtTask.isObsidianTaskLine("- [ ] A task")).isTrue();
        assertThat(TodoTxtTask.isObsidianTaskLine("  - [ ] Indented task")).isTrue();
    }

    @Test
    public void testIsObsidianTaskLine_checked() {
        assertThat(TodoTxtTask.isObsidianTaskLine("- [x] Done task")).isTrue();
        assertThat(TodoTxtTask.isObsidianTaskLine("- [X] Done task uppercase")).isTrue();
    }

    @Test
    public void testIsObsidianTaskLine_notObsidian() {
        assertThat(TodoTxtTask.isObsidianTaskLine("x 2024-01-01 A done todotxt task")).isFalse();
        assertThat(TodoTxtTask.isObsidianTaskLine("(A) Regular todo.txt task")).isFalse();
        assertThat(TodoTxtTask.isObsidianTaskLine("Just a plain line")).isFalse();
        assertThat(TodoTxtTask.isObsidianTaskLine("")).isFalse();
    }

    // ---- isDone ----

    @Test
    public void testIsDone_obsidianDone() {
        assertThat(new TodoTxtTask("- [x] Done task ✅ 2024-01-01").isDone()).isTrue();
        assertThat(new TodoTxtTask("- [X] Done task uppercase").isDone()).isTrue();
    }

    @Test
    public void testIsDone_obsidianUndone() {
        assertThat(new TodoTxtTask("- [ ] Unchecked task").isDone()).isFalse();
        assertThat(new TodoTxtTask("- [ ] Task with due 📅 2024-06-10").isDone()).isFalse();
    }

    @Test
    public void testIsDone_todoTxtDone() {
        assertThat(new TodoTxtTask("x 2024-01-01 Done task").isDone()).isTrue();
        assertThat(new TodoTxtTask("X done task").isDone()).isTrue();
    }

    @Test
    public void testIsDone_todoTxtUndone() {
        assertThat(new TodoTxtTask("(A) Not done task").isDone()).isFalse();
    }

    // ---- getDueDate ----

    @Test
    public void testGetDueDate_obsidianFormat() {
        assertThat(new TodoTxtTask("- [ ] Submit report 📅 2024-06-10").getDueDate()).isEqualTo("2024-06-10");
        assertThat(new TodoTxtTask("- [ ] Task 📅 2024-06-10 🔼").getDueDate()).isEqualTo("2024-06-10");
        assertThat(new TodoTxtTask("- [x] Done task ✅ 2024-01-10 📅 2024-06-15").getDueDate()).isEqualTo("2024-06-15");
    }

    @Test
    public void testGetDueDate_obsidianNoDate() {
        assertThat(new TodoTxtTask("- [ ] No due date").getDueDate()).isEqualTo("");
    }

    @Test
    public void testGetDueDate_todoTxtFormat() {
        assertThat(new TodoTxtTask("(A) Task due:2024-06-10").getDueDate()).isEqualTo("2024-06-10");
    }

    // ---- getPriority ----

    @Test
    public void testGetPriority_obsidianHighest() {
        assertThat(new TodoTxtTask("- [ ] High priority task 🔺").getPriority()).isEqualTo('A');
    }

    @Test
    public void testGetPriority_obsidianHigh() {
        assertThat(new TodoTxtTask("- [ ] High priority task ⏫").getPriority()).isEqualTo('B');
    }

    @Test
    public void testGetPriority_obsidianMedium() {
        assertThat(new TodoTxtTask("- [ ] Medium priority task 🔼").getPriority()).isEqualTo('C');
    }

    @Test
    public void testGetPriority_obsidianLow() {
        assertThat(new TodoTxtTask("- [ ] Low priority task 🔽").getPriority()).isEqualTo('D');
    }

    @Test
    public void testGetPriority_obsidianLowest() {
        assertThat(new TodoTxtTask("- [ ] Lowest priority task ⏬").getPriority()).isEqualTo('E');
    }

    @Test
    public void testGetPriority_obsidianNone() {
        assertThat(new TodoTxtTask("- [ ] No priority task 📅 2024-06-10").getPriority()).isEqualTo(TodoTxtTask.PRIORITY_NONE);
    }

    @Test
    public void testGetPriority_todoTxtPriority() {
        assertThat(new TodoTxtTask("(A) Task with priority A").getPriority()).isEqualTo('A');
        assertThat(new TodoTxtTask("(B) Task with priority B").getPriority()).isEqualTo('B');
    }

    // ---- getDescription ----

    @Test
    public void testGetDescription_obsidianSimple() {
        assertThat(new TodoTxtTask("- [ ] Submit report").getDescription()).isEqualTo("Submit report");
    }

    @Test
    public void testGetDescription_obsidianWithDueDate() {
        assertThat(new TodoTxtTask("- [ ] Submit report 📅 2024-06-10").getDescription()).isEqualTo("Submit report");
    }

    @Test
    public void testGetDescription_obsidianWithPriority() {
        assertThat(new TodoTxtTask("- [ ] Submit report 🔼").getDescription()).isEqualTo("Submit report");
    }

    @Test
    public void testGetDescription_obsidianWithMultipleMeta() {
        assertThat(new TodoTxtTask("- [ ] Submit weekly report 📅 2024-06-10 🔼 🔁 every Monday").getDescription())
                .isEqualTo("Submit weekly report");
    }

    @Test
    public void testGetDescription_obsidianDoneWithDoneDate() {
        assertThat(new TodoTxtTask("- [x] Completed task ✅ 2024-01-10 📅 2024-01-15").getDescription())
                .isEqualTo("Completed task");
    }

    // ---- File recognition ----

    @Test
    public void testTodoMdFileRecognition() {
        java.util.regex.Pattern p = TodoTxtTextConverter.TODOTXT_FILE_PATTERN;
        assertThat(p.matcher("todo.md").matches()).isTrue();
        assertThat(p.matcher("TODO.md").matches()).isTrue();
        assertThat(p.matcher("todo.txt").matches()).isTrue();
    }
}
