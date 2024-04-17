package com.wk.gradle.plugin

import com.wk.gradle.plugin.task.AddGitRulesTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project

/**
 * created by wangkang on 2023/5/26
 */
public class GitPlugin implements Plugin<Project> {

    final String commitRuleFileName = "commit-msg"
    final String templateFileName = "template"
    String mGitRootDir = null

    void apply(Project project) {

        def exts = createCustomExtends(project)

        project.tasks.register("addGitRules", AddGitRulesTask) { task ->
            parseExtension(project, exts)
            task.gitRootDir = mGitRootDir
            task.gitRulesFile = new File(getGitRuleFilePath())
            task.gitTemplateFile = new File(getGitTemplateFilePath())
            task.gitRules.addAll(exts.ruleList)
        }

        project.tasks.findByName("preBuild").dependsOn("addGitRules")
    }

    private GitPluginExtension createCustomExtends(Project project) {
        return project.extensions.create('gitCommit', GitPluginExtension)
    }

    private void parseExtension(Project project, GitPluginExtension extension) {
        mGitRootDir = extension.gitRootDir.getOrNull()
        if (mGitRootDir == null) {
            mGitRootDir = project.rootDir
        }
    }

    private String getGitHookDirPath() {
        return "$mGitRootDir/.git/hooks"
    }

    private String getGitTemplateDirPath() {
        return "$mGitRootDir/.git/template"
    }

    private String getGitRuleFilePath() {
        return "${getGitHookDirPath()}/$commitRuleFileName"
    }

    private String getGitTemplateFilePath() {
        return "${getGitTemplateDirPath()}/$templateFileName"
    }


    private boolean applyRuleForCustom(Project project) {

        if (ruleFilePath == null) {
            return false
        }

        File ruleFile = project.file(ruleFilePath)

        if (!ruleFile.exists()) {
            project.logger.error("GitPlugin: $ruleFilePath is not exist")
            return false
        }

        File gitHooksDir = project.file(getGitHookDirPath())

        project.copy {
            from ruleFile
            rename { String filename ->
                return "commit-msg"
            }
            into gitHooksDir
        }
        return true
    }

    private boolean applyRuleForDefault() {
        try {
            FileCreator fileCreator = new FileCreator(getGitHookDirPath(), commitRuleFileName,
                    FileContents.GIT_COMMIT_MSG_CONTENT)
            fileCreator.override = true
            fileCreator.create()
            return true
        } catch (Exception e) {
            project.logger.error("GitPlugin: applyForDefault failed $e")
            return false
        }
    }

    private boolean applyRuleExecutable() {
        boolean res = false
        new File(getGitHookDirPath()).listFiles().each { File file ->
            if (file.name == commitRuleFileName) {
                file.setExecutable(true)
                res = true
            }
        }
        return res
    }

    private boolean applyTemplateDefault(Project project) {
        try {
            FileCreator fileCreator = new FileCreator("${getGitTemplateDirPath()}", "commit-template",
                    FileContents.GIT_TEMPLATE_CONTENT)
            fileCreator.override = true
            fileCreator.create()
            File templateFile = new File(fileCreator.getFilePath())
            //templateFile.setReadable(true)
            //templateFile.setWritable(true)
            project.exec {
                commandLine 'git', 'config', '--local', 'commit.template', "${fileCreator.getFilePath()}"
            }
            return true
        } catch (Exception e) {
            project.logger.error("GitPlugin: applyForDefault failed $e")
            return false
        }
    }

    private boolean applyTemplateCustom(Project project) {

        if (templateFilePath == null) {
            return false
        }

        if (!new File(templateFilePath).exists()) {
            project.logger.error("GitPlugin: $templateFilePath is not exist")
            return false
        }

        project.exec {
            commandLine 'git', 'config', '--local', 'commit.template', "$templateFilePath"
        }
        return true
    }


}




