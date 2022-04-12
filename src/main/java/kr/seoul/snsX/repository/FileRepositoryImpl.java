package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.exception.FailImgSaveException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Getter
public class FileRepositoryImpl implements FileRepository{

    @Value("${file.dir}")
    public String fileDir;

    public void deleteFiles(List<Image> files) throws IllegalArgumentException, FileNotFoundException {
        for (Image img : files) {
            String filePath = fileDir + img.getUploadedFilename();
            File file = new File(filePath);
            if (file.exists()) {
                if (!file.delete()) {
                    log.info("[File Delete] Failed to delete file: filepath -> {}", filePath);
                    throw new IllegalArgumentException();
//
                } else {
                    log.info("[File Delete] Deleted file: filepath -> {}", filePath);
                }
            } else {
                throw new FileNotFoundException();
            }
        }
    }

    private String getFullPath(String filename) {
        return fileDir + filename;
    }

    public void storeFiles(List<MultipartFile> multipartFiles, List<Image> images) throws FailImgSaveException, FileNotFoundException {
        try {
            for (int i = 0; i < multipartFiles.size(); i++) {
                if (!multipartFiles.get(i).isEmpty()) {
                    storeFile(multipartFiles.get(i), images.get(i).getUploadedFilename());
                }
            }
        } catch (FailImgSaveException e) {
            /*이미 저장한 파일들 다시 삭제*/
            deleteFiles(images);
            throw e;
        }
    }

    private void storeFile(MultipartFile file, String uploadName) throws FailImgSaveException {
        try {
            file.transferTo(new File(getFullPath(uploadName)));
        } catch (Exception e) {
            throw new FailImgSaveException("[FILE SAVE] 파일 저장에 실패했습니다.");
        }
    }

    public String createStoreFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }
}
