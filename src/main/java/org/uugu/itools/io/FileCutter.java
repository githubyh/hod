package org.uugu.itools.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件分割器
 *
 * Created by silence on 3/23/15.
 */
public class FileCutter {

    public static final String OUTPUT_DIRECTORY_PATH = "F:/sc-exception/CutFile";
    public static final File DIRECTORY = new File(OUTPUT_DIRECTORY_PATH);

    public static final String TEST_FILE_PATH = "F:/sc-exception/catalina22.out";
    public static final File FILE = new File(TEST_FILE_PATH);

    private static final Long EACH_FILE_SIZE = 20480000L;

    public static void main(String[] args) throws IOException {
        System.out.println("===========================文件分割开始：================================");
        FileCutter fileCutter = new FileCutter();
        fileCutter.cutFile();
        System.out.println("===========================文件分割结束！================================");
    }

    public void cutFile() throws IOException {
        Long fileSize = FileUtils.sizeOf(FILE);

        Long fileNum = fileSize / EACH_FILE_SIZE + 1;

        System.out.println("共计需要分割文件个数：" + fileNum);

        File file;

        Long inputOffSet = 0L;

        for(Long i = 0L; i < fileNum; i++ ){
            file = new File(OUTPUT_DIRECTORY_PATH+"/CutFile" + (i+1) + ".txt");

            IOUtils.copyLarge(FileUtils.openInputStream(FILE), FileUtils.openOutputStream(file), inputOffSet, EACH_FILE_SIZE);

            System.out.println("第"+ (i+1) +"个文件分割成功！文件名称：" + file.getName());

            inputOffSet = inputOffSet + EACH_FILE_SIZE;
        }

    }
}
