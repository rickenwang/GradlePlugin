package com.cvte.maxhub.plugin;

import org.gradle.api.provider.Property;

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
public interface GitPluginExtension {

    Property<String> getRuleFile()
    Property<String> getTemplateFile()
    Property<String> getGitRootDir()
    Property<Boolean> getForce()

    Property<List<List>> getRules()
}
