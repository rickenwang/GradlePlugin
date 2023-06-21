# 背景

插件的任务是：

- 规范检查：不规范的 commit 在本地不允许执行成功；
- 无感接入：只要在项目中配置好插件后，同一个项目，所有人的 commit 规则都保持一致，其他人无需任何操作；
- 轻松配置：只需要在 Gradle 脚本中配置好规则，无需将 commit-msg 和 template 文件在多个项目中多次拷贝；
- 增量编译：规则不变时，任务不会多次执行，提高项目编译效率；

最终 git 提交规范格式如下：
```
[bugfix] 这里是 bug 的描述
[jira] 这里 jira 连接
[influence] 这里是影响范围
```

# 一、接入
## 1.1、添加仓库地址和插件依赖

在根项目下的 build.gradle 文件下添加：
```
allprojects {
repositories {
...
maven { url 'https://jitpack.io' }
}

    dependencies {
        ...
        classpath 'com.github.rickenwang.GradlePlugin:git-commit-checker:0.2.2'
    }
}
```

## 1.2、添加插件和配置

在 app 的 build.gradle 文件下添加：
```
apply plugin: 'git-commit-checker'


gitCommit {

    gitRootDir = "${project.rootDir}"

    rules {
        rule "bugfix,feature,hotfix", "hint1"
        rule "jira", "hint2", true
        rule "influence", "这是本次提交的影响"
    }
}
```

- gitRootDir：git 的根目录，可以不配置，默认为整个项目的根目录；
- rule：表示本次 commit 信息的一行 message，格式为 [title] hint，一行 message 可以支持多个可选的 title，需要用 , 隔开；
- rule 后面最后的布尔值为  true，表示不强制校验该 rule，只会在模板中显示，不填默认为 false，表示需要校验该 rule；


插件一共做了两件事：

1. 检查你的 commit 信息是否符合你设置的规则；
2. 创建提交 commit 时的模板；


如上配置，生成的 commit 规则是：

```
[bugfix/feature/hotfix] 这里是任意描述，title 必须是这三个中的一个
[jira] 这里是任意描述
[influence] 这里是任意描述
```

生成的 commit 模板为：

```
[bugfix] hint1
[jira] hint2
[influence] 这是本次提交的影响
```

注意：模板中的 title 就是配置的 rule 中的第一个 title。

# 二、使用

# 2.1、自动执行 addGitRules 任务

配置好后，我们只需要 rebuild project 就可以了，可以在 build console 下看到如下输出：

```
> Task :app:addGitRules
GitPlugin: start to AddGitRulesTask
gitRootDir: ~/Develop/Github/GradlePlugin
gitRule: [bugfix,feature,hotfix] hint1
gitRule: [jira] hint2
gitRule: [influence] 可选
```
注意：仅仅 sync 项目不会执行 addGitRules 任务。

## 2.2、测试提交模板

到这里，你可以输入：
```
git commit
```

这时就会看到默认的提交模板了：
```
[bugfix] hint1
[jira] hint2
[influence] 可选

# Please enter the commit message for your changes. Lines starting
# with '#' will be ignored, and an empty message aborts the commit.
#
# On branch master
# Your branch is ahead of 'origin/master' by 4 commits.
#   (use "git push" to publish your local commits)
#
# Changes to be committed:
#       modified:   app/build.gradle
```

## 2.3、测试提交规范

如果提交的信息不符合规范，那么也会提示你本次提交不对：

```
本次提交未遵守 commit 规范：
[hello] hint1
[jira] hint2
[influence] 可选

# Please enter the commit message for your changes. Lines starting
# with '#' will be ignored, and an empty message aborts the commit.
#
# On branch master
# Your branch is ahead of 'origin/master' by 4 commits.
#   (use "git push" to publish your local commits)
#
# Changes to be committed:
#       modified:   app/build.gradle
#

请按照以下格式提交：
[bugfix,feature,hotfix] hint1
[jira] hint2
[influence] 可选
```

# 三、清除配置


如果在 build.gradle 文件下配置 rule 为空，或者直接删除 gitCommit 拓展块，那么下次构建时，插件会清除 commit 规则和模板：
```
// 注释下面代码
gitCommit {

    gitRootDir = "${project.rootDir}"

    rules {
        rule "bugfix,feature,hotfix", "hint1"
        rule "jira", "hint2"
        rule "influence", "这是本次提交的影响"
    }
}
```

# 四、增量编译

插件支持了增量编译，只会在第一次构建时创建 git 规则，如果配置不变，那么第二次构建时不会重复执行任务，提高编译效率。

```
> Task :app:addGitRules UP-TO-DATE
```