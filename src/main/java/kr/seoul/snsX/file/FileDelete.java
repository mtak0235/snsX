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



}
