package org.uugu.itools.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 文本读取Util
 * Created by silence on 3/3/15.
 */
public class FileReader {

    public static final String TEST_DIRECTORY_PATH_1 = "D:/testDirectory1";
    public static final File DIRECTORY1 = new File(TEST_DIRECTORY_PATH_1);

    public static final String TEST_FILE_PATH_1 = "/Users/silence/Downloads/catalina.out";
    public static final File FILE1 = new File(TEST_FILE_PATH_1);

    public static final String TEST_FILE_PATH_2 = "/Users/silence/Downloads/catalina_result.txt";
    public static final File FILE2 = new File(TEST_FILE_PATH_2);

    public static final String TEST_FILE_PATH_3 = "/Users/silence/Downloads/catalina_result_name.txt";
    public static final File FILE3 = new File(TEST_FILE_PATH_3);

    public static final String TEST_FILE_PATH_4 = "/Users/silence/Downloads/catalina_result_value.txt";
    public static final File FILE4 = new File(TEST_FILE_PATH_4);

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        try {
            fileReader.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile() throws IOException {
//            String fileSize = FileUtils.byteCountToDisplaySize(1024 * 1024);
//
//            // 清空某目录下的所有目录,含文件夹和文件
//            FileUtils.cleanDirectory(DIRECTORY_1);
//
//            // 如字面意思
//            FileUtils.convertFileCollectionToFileArray(null);
//
//            // 将参数1目录下的全部内容复制到参数2目录
//            FileUtils.copyDirectory(DIRECTORY_1, DIRECTORY_2);
//
//            // 将参数1目录整个复制到参数2目录下
//            FileUtils.copyDirectoryToDirectory(DIRECTORY_1, DIRECTORY_2);
//
//            // copy參數1文件到參數2
//            FileUtils.copyFile(FILE_1, FILE_2);
//
//            // copy參數1文件到參數2目錄下
//            FileUtils.copyFileToDirectory(FILE_1, DIRECTORY_1);
//
//            // 强制删除文件
//            FileUtils.forceDelete(FILE_1);
//
//            // 将文件转为InputStrem,对应有openOutStream方法
//            FileUtils.openInputStream(FILE_1);
//            FileUtils.openOutputStream(FILE_1);
//
//            // 读取文件转为字节数组
//            FileUtils.readFileToByteArray(FILE_1);
//
//            // 读取文件转换为String类型,方便文本读取
//            FileUtils.readFileToString(FILE_1, "UTF-8");
//
//            // 返回目录的大小
//            FileUtils.sizeOfDirectory(DIRECTORY_1);
//
//            // 写字符串到参数1文件中
//            FileUtils.writeStringToFile(FILE_1, "test", "UTF-8");
        List<String> contents = FileUtils.readLines(FILE1);
        //遍历输出contents
        for (String line : contents) {
            if (line.startsWith("===========Insert Data")) {
                System.out.println(line);
                int nn = line.indexOf(":");
                String name = line.substring(11, nn);
                String value = line.substring(nn + 1, line.length());
                FileUtils.writeStringToFile(FILE3, name + "\n", true);
                FileUtils.writeStringToFile(FILE4, value + "\n", true);
            }
        }
    }

}
