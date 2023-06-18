package com.cvte.maxhub.plugin

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested;

/**
 * gitCommitter {
 *     rules = [['bugfix,feature', 'hint1'], ['jira', 'hint2']]
 *
 * }
 *
 *
 * [bugfix] 我是自定义
 * [jira] none
 * [inf] none
 *
 * created by wangkang on 2023/5/26
 */
abstract public class GitPluginExtension {

    abstract Property<String> getGitRootDir()

    private GitRules rules = new GitRules()

    public void rules(Action<GitRules> action) {
        action.execute(rules)
    }

    public List<GitRule> getRuleList() {
        return rules.rules
    }
}
