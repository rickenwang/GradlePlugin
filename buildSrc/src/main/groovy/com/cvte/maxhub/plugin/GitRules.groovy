package com.cvte.maxhub.plugin

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.internal.impldep.org.eclipse.jgit.annotations.NonNull

/**
 *
 * [bugfix] 我是自定义
 * [jira] none
 * [inf] none
 *
 * created by wangkang on 2023/5/26
 */
public class GitRules {

    final List<GitRule> rules = new LinkedList<>()

    public void rule(@NonNull String titles, @NonNull String hint) {
        rules.add(new GitRule(titles, hint));
    }

    static void main(String[] args) {
        println(grepItem("bugfix"))
    }
}
