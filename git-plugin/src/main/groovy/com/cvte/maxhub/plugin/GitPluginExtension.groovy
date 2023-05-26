package com.cvte.maxhub.plugin;

import org.gradle.api.provider.Property;

/**
 * created by wangkang on 2023/5/26
 */
public interface GitPluginExtension {

    Property<String> getGitRuleFile()
    Property<String> getTemplateFilePath()
    Property<String> getGitRootDir()
    Property<Boolean> getForce()
}
