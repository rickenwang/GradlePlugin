package com.cvte.maxhub.plugin

/**
 * created by wangkang on 2023/5/26
 */
class FileContents {

    static final String GIT_TEMPLATE_CONTENT = """[bugfix] 问题描述
[jira] none
[inf] none"""

    static final String GIT_COMMIT_MSG_CONTENT = """#!/bin/bash
if (egrep -q '^Merge' "\$1" \\
    || egrep -q '^Revert' "\$1" \\
    || ((egrep -q '^\\[bugfix\\]' "\$1" \\
    || egrep -q '^\\[feature\\]' "\$1" \\
    || egrep -q '^\\[optimize\\]' "\$1" \\
    || egrep -q '^\\[refactor\\]' "\$1" \\
    || egrep -q '^\\[doc\\]' "\$1"  \\
    || egrep -q '^\\[test\\]' "\$1" \\
    || egrep -q '^\\[other\\]' "\$1" ) \\
    && egrep -q '^\\[jira\\]' "\$1" \\
    && egrep -q '^\\[inf\\]' "\$1"));then
    exit 0
else
    echo "本次提交未遵守 commit 规范："
    cat "\$1"
    echo
    echo "请按照以下格式提交："
    echo "[bugfix/feature/optimize/refactor/doc/test/other] 问题描述"
    echo "[jira] jira 链接，没有则填 none"
    echo "[inf] 影响范围，必填"
    echo
    exit 1
fi"""
}

