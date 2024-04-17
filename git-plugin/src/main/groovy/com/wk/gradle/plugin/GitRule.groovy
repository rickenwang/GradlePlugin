package com.wk.gradle.plugin

import com.wk.gradle.plugin.task.GitRulesUtils


/**
 * [title] this is hint
 *
 * created by wangkang on 2023/5/26
 */
public class GitRule implements Serializable {

    //
    private String[] titles = null

    private String hint = ""

    boolean ignore = false

    GitRule(String titles, String hint) {
        String[] titleArr = titles.split(",")
        this.titles = new String[titleArr.size()]
        for (int i = 0; i < this.titles.size(); i++) {
            this.titles[i] = titleArr[i].trim()
        }
        this.hint = hint
    }

    @Override
    String toString() {
        return "[${GitRulesUtils.mergeStringArr(titles)}] $hint $ignore"
    }
}
