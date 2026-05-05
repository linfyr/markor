package net.gsantner.markor.format.todotxt;

import android.graphics.Typeface;

import net.gsantner.markor.frontend.textview.SyntaxHighlighterBase;
import net.gsantner.markor.model.AppSettings;

public class TodoTxtBasicSyntaxHighlighter extends SyntaxHighlighterBase {

    private final static int COLOR_CATEGORY = 0xffef6C00;
    private final static int COLOR_CONTEXT = 0xff88b04b;

    private final static int COLOR_PRIORITY_A = 0xffEF2929;
    private final static int COLOR_PRIORITY_B = 0xffd16900;
    private final static int COLOR_PRIORITY_C = 0xff59a112;
    private final static int COLOR_PRIORITY_D = 0xff0091c2;
    private final static int COLOR_PRIORITY_E = 0xffa952cb;
    private final static int COLOR_PRIORITY_F = 0xff878986;

    private final static int COLOR_DONE_DARK = 0x999d9d9d;
    private final static int COLOR_DONE_LIGHT = 0x993d3d3d;
    private final static int COLOR_DATE_DARK = COLOR_DONE_DARK;
    private final static int COLOR_DATE_LIGHT = 0xcc6d6d6d;

    public TodoTxtBasicSyntaxHighlighter(final AppSettings as) {
        super(as);
    }

    @Override
    public void generateSpans() {
        createSmallBlueLinkSpans();
        createColorSpanForMatches(TodoTxtTask.PATTERN_CONTEXTS, COLOR_CONTEXT);
        createColorSpanForMatches(TodoTxtTask.PATTERN_PROJECTS, COLOR_CATEGORY);
        createStyleSpanForMatches(TodoTxtTask.PATTERN_KEY_VALUE_PAIRS, Typeface.ITALIC);

        // Priorities
        createSpanForMatches(TodoTxtTask.PATTERN_PRIORITY_A, new HighlightSpan().setForeColor(COLOR_PRIORITY_A).setBold(true));
        createSpanForMatches(TodoTxtTask.PATTERN_PRIORITY_B, new HighlightSpan().setForeColor(COLOR_PRIORITY_B).setBold(true));
        createSpanForMatches(TodoTxtTask.PATTERN_PRIORITY_C, new HighlightSpan().setForeColor(COLOR_PRIORITY_C).setBold(true));
        createSpanForMatches(TodoTxtTask.PATTERN_PRIORITY_D, new HighlightSpan().setForeColor(COLOR_PRIORITY_D).setBold(true));
        createSpanForMatches(TodoTxtTask.PATTERN_PRIORITY_E, new HighlightSpan().setForeColor(COLOR_PRIORITY_E).setBold(true));
        createSpanForMatches(TodoTxtTask.PATTERN_PRIORITY_F, new HighlightSpan().setForeColor(COLOR_PRIORITY_F).setBold(true));
        createStyleSpanForMatches(TodoTxtTask.PATTERN_PRIORITY_G_TO_Z, Typeface.BOLD);

        createColorSpanForMatches(TodoTxtTask.PATTERN_CREATION_DATE, _isDarkMode ? COLOR_DATE_DARK : COLOR_DATE_LIGHT, 1);
        createColorSpanForMatches(TodoTxtTask.PATTERN_DUE_DATE, COLOR_PRIORITY_A, 2, 3);

        // Obsidian Tasks plugin format highlighting
        // Highlight Obsidian priority emojis
        createSpanForMatches(TodoTxtTask.PATTERN_OB_PRIORITY_HIGHEST, new HighlightSpan().setForeColor(COLOR_PRIORITY_A).setBold(true));
        createSpanForMatches(TodoTxtTask.PATTERN_OB_PRIORITY_HIGH, new HighlightSpan().setForeColor(COLOR_PRIORITY_B).setBold(true));
        createSpanForMatches(TodoTxtTask.PATTERN_OB_PRIORITY_MEDIUM, new HighlightSpan().setForeColor(COLOR_PRIORITY_C).setBold(true));
        createSpanForMatches(TodoTxtTask.PATTERN_OB_PRIORITY_LOW, new HighlightSpan().setForeColor(COLOR_PRIORITY_D).setBold(true));
        createSpanForMatches(TodoTxtTask.PATTERN_OB_PRIORITY_LOWEST, new HighlightSpan().setForeColor(COLOR_PRIORITY_E).setBold(true));
        // Highlight Obsidian due date (📅 YYYY-MM-DD) - same color as todo.txt due date
        createColorSpanForMatches(TodoTxtTask.PATTERN_OB_DUE_DATE, COLOR_PRIORITY_A, 1);
        // Highlight other Obsidian date emojis + dates
        createColorSpanForMatches(TodoTxtTask.PATTERN_OB_SCHEDULED_DATE, _isDarkMode ? COLOR_DATE_DARK : COLOR_DATE_LIGHT, 1);
        createColorSpanForMatches(TodoTxtTask.PATTERN_OB_START_DATE, _isDarkMode ? COLOR_DATE_DARK : COLOR_DATE_LIGHT, 1);
        createColorSpanForMatches(TodoTxtTask.PATTERN_OB_CREATED_DATE, _isDarkMode ? COLOR_DATE_DARK : COLOR_DATE_LIGHT, 1);
        createColorSpanForMatches(TodoTxtTask.PATTERN_OB_DONE_DATE, _isDarkMode ? COLOR_DATE_DARK : COLOR_DATE_LIGHT, 1);
        createColorSpanForMatches(TodoTxtTask.PATTERN_OB_CANCELLED_DATE, _isDarkMode ? COLOR_DATE_DARK : COLOR_DATE_LIGHT, 1);

        // Strike out done tasks
        // Note - as we now sort by start, projects, contexts, tags and due date will be highlighted for done tasks
        createSpanForMatches(TodoTxtTask.PATTERN_DONE, new HighlightSpan().setForeColor(_isDarkMode ? COLOR_DONE_DARK : COLOR_DONE_LIGHT).setStrike(true));
        // Obsidian done tasks: - [x] ...
        createSpanForMatches(TodoTxtTask.PATTERN_OB_DONE, new HighlightSpan().setForeColor(_isDarkMode ? COLOR_DONE_DARK : COLOR_DONE_LIGHT).setStrike(true));
    }
}