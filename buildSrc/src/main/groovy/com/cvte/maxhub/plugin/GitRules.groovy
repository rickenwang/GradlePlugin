package com.cvte.maxhub.plugin

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.internal.impldep.org.eclipse.jgit.annotations.NonNull

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

    public void rule(@NonNull String titles, @NonNull String hint) {
        rules.add(new GitRule(titles, hint));
    }

    //if (egrep -q '^Merge' "$1" \
    //    || egrep -q '^Revert' "$1" \
    //    || ( \
    //    (egrep -q '^\[bugfix\]' "$1" \
    //    || egrep -q '^\[feature\]' "$1" \
    //    || egrep -q '^\[optimize\]' "$1" \
    //    || egrep -q '^\[refactor\]' "$1" \
    //    || egrep -q '^\[doc\]' "$1"  \
    //    || egrep -q '^\[test\]' "$1" \
    //    || egrep -q '^\[other\]' "$1" ) \
    //    && egrep -q '^\[jira\]' "$1" \
    //    && egrep -q '^\[inf\]' "$1" \
    //    ));then
    //    exit 0
    //else
    //    echo "自定义"
    //    echo "本次提交未遵守 commit 规范："
    //    cat "$1"
    //    echo
    //    echo "请按照以下格式提交："
    //    echo "[bugfix/feature/optimize/refactor/doc/test/other] 问题描述"
    //    echo "[jira] jira 链接，没有则填 none"
    //    echo "[inf] 影响范围，必填"
    //    echo
    //    exit 1
    //fi



    static void main(String[] args) {
        println(grepItem("bugfix"))
    }
}
