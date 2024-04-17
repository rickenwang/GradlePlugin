package com.wk.gradle.plugin

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.internal.impldep.org.eclipse.jgit.annotations.NonNull

import javax.annotation.Nullable

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

    public void rule(@NonNull String titles, @NonNull String hint, @NonNull Boolean ignore = false) {
        GitRule gitRule = new GitRule(titles, hint)
        gitRule.ignore = ignore
        rules.add(gitRule)
    }

}
