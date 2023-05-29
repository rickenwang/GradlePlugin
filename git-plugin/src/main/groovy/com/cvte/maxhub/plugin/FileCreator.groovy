package com.cvte.maxhub.plugin

/**
 * created by wangkang on 2023/5/26
 */
class FileCreator {

    String mContent
    String mOutputDirectory
    String mFileName
    boolean override = false

    public FileCreator(String outputDirectory, String fileName, String content) {
        mContent = content
        mOutputDirectory = outputDirectory
        mFileName = fileName
    }

    void create() {
        File file = new File("${getFilePath()}")
        if (file.exists() && !override) {
            return
        }
        file.parentFile.mkdirs()
        file.createNewFile()
        file.write(mContent)
    }

    String getFilePath() {
        return "$mOutputDirectory/$mFileName"
    }
}

