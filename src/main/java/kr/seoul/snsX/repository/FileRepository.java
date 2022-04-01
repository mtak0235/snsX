package kr.seoul.snsX.repository;

import kr.seoul.snsX.entity.Image;
import kr.seoul.snsX.exception.FailImgSaveException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface FileRepository {

    void deleteFiles(List<Image> files) throws IllegalArgumentException, FileNotFoundException;

    List<Image> storeFiles(List<MultipartFile> multipartFiles) throws FailImgSaveException, FileNotFoundException;

    String getFileDir();
}
