package com.cvte.maxhub.plugin.task;

import com.cvte.maxhub.plugin.GitRule;

import java.util.List;

public class GitRulesUtils {

    // [bugfix] 我是自定义
    // [jira] none
    // [inf] none
    public static String commitTemplateContent(List<GitRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return ""
        }
        StringBuilder sb = new StringBuilder()
        for (GitRule rule: rules) {
            sb.append("[${rule.titles[0]}] ${rule.hint}\n")
        }
        return sb.toString()
    }

    public static String commitMsgContent(List<GitRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return ""
        }

        StringBuilder sb = new StringBuilder()
        sb.append("if (egrep -q '^Merge' \"\$1\"")
        sb.append(" || egrep -q '^Revert' \"\$1\"")
        sb.append(" || (")
        int counter = 0
        for (GitRule rule: rules) {
            boolean isFirst = counter == 0
            boolean isLast = counter == rules.size() - 1
            if (!isFirst) {
                sb.append(" && ")
            }
            sb.append(grepItems(rule.titles))
            if (isLast) {
                sb.append(")")
            }
            counter++
        }
        sb.append(");then\n")
        sb.append("    exit 0\n")

        sb.append("else\n")
        sb.append("    echo \"本次提交未遵守 commit 规范：\"\n")
        sb.append("    cat \"\$1\"\n")
        sb.append("    echo \" \"\n")
        sb.append("    echo \"请按照以下格式提交：\"\n")
        for (GitRule rule: rules) {
            sb.append("    echo \"[${mergeStringArr(rule.titles)}] ${rule.hint}\"\n")
        }
        sb.append("    exit 1\n")
        sb.append("fi")
        return sb.toString()
    }

    private static String grepItems(String[] titles) {

        StringBuilder sb = new StringBuilder()
        int counter = 0
        for (String title: titles) {
            boolean isFirst = counter == 0
            boolean isLast = counter == titles.size() - 1
            // (egrep -q '^\[bugfix\]' "$1" \
            if (isFirst) {
                sb.append("(")
            } else {
                sb.append(" || ")
            }

            sb.append(grepItem(title))

            if (isLast) {
                sb.append(" )")
            }
            // sb.append(" \\n")
            counter++
        }
        return sb.toString()
    }

    private static String grepItem(String title) {
        return "egrep -q '^\\[${title}\\]' \"\$1\""
    }

    public static String mergeStringArr(String[] titles) {
        if (titles == null) {
            return ""
        }
        StringBuilder sb = new StringBuilder();
        int counter = 0
        for (int i = 0; i < titles.length; i++) {
            boolean isLast = counter == titles.length - 1
            sb.append(titles[i])
            if (!isLast) {
                sb.append("/")
            }
            counter++
        }

        return sb.toString()
    }

}
