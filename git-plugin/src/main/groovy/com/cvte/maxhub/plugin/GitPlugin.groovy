package com.cvte.maxhub.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * created by wangkang on 2023/5/26
 */
public class GitPlugin implements Plugin<Project> {

    final String commitRuleFileName = "commit-msg"
    String ruleFilePath = null
    String templateFilePath = null
    String gitRootDir = null
    boolean force = false

    void apply(Project project) {

        def exts = createCustomExtends(project)
//        if (!shouldSkip()) {
//            project.task("gitCommitCheck") {
//                doLast {
//
//                    parseExtension(exts)
//
//                    execute(project)
//                }
//            }
//            project.tasks.findByName("preBuild").dependsOn("gitCommitCheck")
//        }

        project.task("gitCommitCheck") {
            doLast {
                parseExtension(project, exts)
                if (!shouldSkip()) {
                    execute(project)
                }
            }
        }
        project.tasks.findByName("preBuild").dependsOn("gitCommitCheck")
    }

    private void execute(Project project) {
        boolean applyRuleExist = applyRuleForCustom(project)
        if (!applyRuleExist) {
            applyRuleExist = applyRuleForDefault()
        }

        boolean applyRuleExecutable = applyRuleExecutable()

        boolean applyTemplate = applyTemplateCustom(project)
        if (!applyTemplate) {
            applyTemplate = applyTemplateDefault(project)
        }

        if (applyRuleExist && applyRuleExecutable && applyTemplate) {
            println("GitPlugin: execute success")
        } else {
            project.logger.error("GitPlugin: execute failed, applyRuleExist is $applyRuleExist, applyRuleExecutable is $applyRuleExecutable, applyTemplate is $applyTemplate")
        }
    }

    private GitPluginExtension createCustomExtends(Project project) {

        return project.extensions.create('gitCommit', GitPluginExtension)
    }

    private void parseExtension(Project project, GitPluginExtension extension) {
        ruleFilePath = extension.ruleFile.getOrNull()
        templateFilePath = extension.templateFile.getOrNull()
        gitRootDir = extension.gitRootDir.getOrNull()
        force = extension.force.getOrElse(false)
        if (gitRootDir == null) {
            gitRootDir = project.rootDir
        }
        println("GitPlugin: rule file path is $ruleFilePath")
        println("GitPlugin: template file path is $templateFilePath")
        println("GitPlugin: git root dir is $gitRootDir")
        println("GitPlugin: force is $force")
    }

    private String getGitHookDirPath() {
        return "$gitRootDir/.git/hooks"
    }

    private String getGitTemplateDirPath() {
        return "$gitRootDir/.git/template"
    }

    private String getGitRuleFilePath() {
        return "${getGitHookDirPath()}/$commitRuleFileName"
    }

    private boolean shouldSkip() {
        if (new File(getGitRuleFilePath()).exists() && !force) {
            println("GitPlugin: commit-msg file already exists in .git/hooks directory, so skip the execution of these tasks!")
            return true
        }
        return false
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




