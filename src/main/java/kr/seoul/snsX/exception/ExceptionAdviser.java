package kr.seoul.snsX.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdviser {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public String customizedHandler(RuntimeException e, Model model) {
        model.addAttribute("msg", e.getMessage());
        log.error(e.getMessage());
        return "error/error";
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public String savePostEx(FailImgSaveException e, Model model) {
        model.addAttribute("msg", e.getMessage());
        log.error(e.getMessage());
        return "error/error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NumberFormatException.class, MissingPathVariableException.class})
    public String invalidPathEx(Exception e, Model model) {
        model.addAttribute("msg", e.getMessage());
        log.error(e.getMessage());
        return "error/error";
    }


}


