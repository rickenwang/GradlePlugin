package com.cvte.maxhub.plugin

import org.gradle.api.Action
import org.gradle.api.provider.Property

/**
 * [title] this is hint
 *
 * created by wangkang on 2023/5/26
 */
public class GitRule implements Serializable {

    String titles = "";

    String hint = ""

    GitRule(String titles, String hint) {
        this.titles = titles
        this.hint = hint
    }

    @Override
    String toString() {
        return "[${titles}] $hint"
    }
}
