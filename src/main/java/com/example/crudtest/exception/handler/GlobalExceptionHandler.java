package com.example.crudtest.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.example.crudtest.exception.custom.BoardNotFoundException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)    // Validation 예외 처리
    @ResponseBody
    public ResponseEntity<Map<String, String>>
    handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()   // 어떤 필드가 실패했는지 가져옴
                .getFieldErrors()   // 필드별 에러 목록 가져오기 -> "title", "content" 등
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));   // -> 앞에서 작성한 message 값

        return ResponseEntity.badRequest().body(errors);    // http 상태코드 반환, body에 에러 정보 포함
    }

    @ExceptionHandler(BoardNotFoundException.class)     // 게시글 없음 예외 처리, BoardNotFoundException 발생 시 실행
    @ResponseBody
    public ResponseEntity<String>
    handleBoardNotFound(
            BoardNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)   // 상태코드 404
                .body(ex.getMessage());         // message 반환
    }

    @ExceptionHandler(Exception.class)  // 나머지 모든 예외 처리
    @ResponseBody
    public ResponseEntity<String>
    handleException(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)   // 상태코드 반환
                .body("서버 오류 발생");  // message 반환
    }
}