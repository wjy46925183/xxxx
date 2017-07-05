package spring.update.creator;

import java.io.File;

/**
 * The file creator to create file name with apk version name
 * @author lzh
 */
public interface ApkFileCreator {

    File create(String versionName);
}
