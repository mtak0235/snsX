package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.exception.FailImgSaveException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface FileRepository {

    void deleteFiles(List<Image> files) throws IllegalArgumentException, FileNotFoundException;

    void storeFiles(List<MultipartFile> multipartFiles, List<Image> images) throws FailImgSaveException, FileNotFoundException;

    String getFileDir();

    String createStoreFileName(String originalFilename);
}
