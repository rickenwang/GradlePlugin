package com.cvte.maxhub.plugin;

import org.gradle.api.provider.Property;

/**
 * created by wangkang on 2023/5/26
 */
public interface GitPluginExtension {

    Property<String> getRuleFile()
    Property<String> getTemplateFile()
    Property<String> getGitRootDir()
    Property<Boolean> getForce()
}
