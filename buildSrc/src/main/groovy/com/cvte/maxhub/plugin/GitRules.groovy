package com.cvte.maxhub.plugin

import org.gradle.api.Action
import org.gradle.api.provider.ListProperty;
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
abstract public class GitPluginExtension {

    abstract Property<String> getRuleFile()
    abstract Property<String> getTemplateFile()
    abstract Property<String> getGitRootDir()
    abstract Property<Boolean> getForce()

//    public void rule(String title, String hint) {
//        println("GitPlugin: add rule ${title} ${hint}")
//    }

    public void rule(Action<? super String> action) {
        // action.execute(getCustomData());
    }
}
