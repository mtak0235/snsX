package kr.seoul.snsX.file;

import kr.seoul.snsX.entity.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
public class FileDelete {

    @Value("${file.dir}")
    private String fileDir;

    public void deleteFiles(List<Image> files) {
        for (Image img : files) {
            String filePath = fileDir + img.getUploadedFilename();
            File file = new File(filePath);
            if (file.exists()) {
                if (!file.delete()) {
                    log.info("[File Delete] Failed to delete file: filepath -> {}", filePath);
                } else {
                    log.info("[File Delete] Deleted file: filepath -> {}", filePath);
                }
            }
        }
    }

}
