package com.wk.gradle.plugin.task

import com.wk.gradle.plugin.FileCreator
import com.wk.gradle.plugin.GitRule
import com.wk.gradle.plugin.GitRules
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.provider.Property
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * 支持增量编译
 *
 * created by wangkang on 2023/5/26
 */
abstract class AddGitRulesTask extends DefaultTask {

    @Input
    public abstract Property<String> getGitRootDir()

    @Input
    public abstract ListProperty<GitRule> getGitRules()

    @OutputFile
    public abstract Property<File> getGitRulesFile()

    @OutputFile
    public abstract Property<File> getGitTemplateFile()


    @TaskAction
    public void addGitRules() {

        println("GitPlugin: start to AddGitRulesTask")
        println("gitRootDir: ${gitRootDir.get()}")
        for (GitRule rule: gitRules.get()) {
            println("gitRule: ${rule}")
        }
        List<GitRule> rules = gitRules.get()

        try {

            File commitMsgFile = gitRulesFile.get()
            new FileCreator(commitMsgFile.getParent(), commitMsgFile.name,
                    GitRulesUtils.commitMsgContent(rules)).create()

            // 必须设置可执行权限
            project.exec {
                commandLine 'chmod', '+x', "${commitMsgFile.getAbsolutePath()}"
            }

            File commitTemplateFile = gitTemplateFile.get()
            new FileCreator(commitTemplateFile.getParent(), commitTemplateFile.name,
                    GitRulesUtils.commitTemplateContent(rules)).create()

            project.exec {
                commandLine 'git', 'config', '--local', 'commit.template', "${commitTemplateFile.getAbsolutePath()}"
            }

        } catch (Exception e) {
            e.printStackTrace()
        }
    }

}

