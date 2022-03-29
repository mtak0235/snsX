package kr.seoul.snsX.file;

import kr.seoul.snsX.entity.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<Image> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<Image> storeFileResult = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            if (!file.isEmpty()) {
                storeFileResult.add(storeFile(file));
            }
        }
        return storeFileResult;
    }

    private Image storeFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        file.transferTo(new File(getFullPath(storeFileName)));
        return new Image(originalFileName, storeFileName);
    }

    private String createStoreFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

}
