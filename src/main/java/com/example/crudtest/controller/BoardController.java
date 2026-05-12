package com.example.crudtest.controller;

import com.example.crudtest.dto.BoardRequestDto;
import com.example.crudtest.dto.BoardResponseDto;
import com.example.crudtest.dto.PasswordUpdateDto;
import com.example.crudtest.entity.Board;
import com.example.crudtest.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // CREATE (생성)
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardDto) {
        BoardResponseDto createdBoard = boardService.createBoard(boardDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    // READ (조회)
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getAllBoards() {
        List<BoardResponseDto> boards = boardService.gotAllBoards();
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long id) {
        BoardResponseDto board = boardService.gotBoardById(id);
        return ResponseEntity.ok(board);
    }

    @GetMapping("/writer/{writer}")
    public ResponseEntity<List<BoardResponseDto>> gotBoardByWriter(@PathVariable String writer) {
        List<BoardResponseDto> boards = boardService.gotBoardByWriter(writer);
        return ResponseEntity.ok(boards);
    }

    // boards/search?keyword=스프링
    @GetMapping("/search")
    public ResponseEntity<List<BoardResponseDto>> searchBoards(@RequestParam String search) {
        List<BoardResponseDto> boards = boardService.searchBoards(search);
        return ResponseEntity.ok(boards);
    }

    // UPDATE (수정)
    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto boardDto) {
        BoardResponseDto updatedBoard = boardService.updateBoard(id, boardDto);
        if (updatedBoard != null) {
            return ResponseEntity.ok(updatedBoard);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    // PasswordUpdate (비밀번호 수정)
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateDto passwordUpdateDto) {
        boardService.updatePassword(id, passwordUpdateDto);
        return ResponseEntity.noContent().build();
    }

    // DELETE (삭제)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boolean deleted = boardService.deleteBoard(id);

        if(deleted) {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
