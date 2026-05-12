package com.example.crudtest.service;

import com.example.crudtest.dto.BoardResponseDto;
import com.example.crudtest.dto.BoardRequestDto;
import com.example.crudtest.dto.PasswordUpdateDto;
import com.example.crudtest.entity.Board;
import com.example.crudtest.exception.custom.BoardNotFoundException;
import com.example.crudtest.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;  // 생성자 주입

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board toEntity(BoardRequestDto dto) {
        Board board = new Board();
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setWriter(dto.getWriter());
        board.setPassword(dto.getPassword());
        return board;
    }

    public BoardResponseDto toDto(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getWriter()
        );
    }

    // CREATE (생성)
    public BoardResponseDto createBoard(BoardRequestDto boardDto) {
        Board board = toEntity(boardDto);
        Board createdBoard = boardRepository.save(board);
        return toDto(createdBoard);
    }

    // READ (조회)
    public List<BoardResponseDto> gotAllBoards() {
        return boardRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public BoardResponseDto gotBoardById(Long id) {
        return boardRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(()->
                        new BoardNotFoundException(
                                "게시글이 존재하지 않습니다."
                        ));
    }

    public List<BoardResponseDto> gotBoardByWriter(String writer) {
        return boardRepository.findByWriter(writer)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<BoardResponseDto> searchBoards(String keyword) {
        return boardRepository.findByTitleContaining(keyword)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // UPDATE (수정)
    public BoardResponseDto updateBoard(Long id, BoardRequestDto newboardDto) {
        Board existingBoard = boardRepository.findById(id).orElseThrow(()->
                new BoardNotFoundException(
                        "게시글이 존재하지 않습니다."
                ));

        if (existingBoard == null) {
            return null;
        }
        existingBoard.setTitle(newboardDto.getTitle());
        existingBoard.setContent(newboardDto.getContent());
        existingBoard.setWriter(newboardDto.getWriter());
        Board updatedBoard = boardRepository.save(existingBoard);

        return toDto(updatedBoard);
    }

    // PasswordUpdate (비밀번호 수정)
    public void updatePassword(Long id, PasswordUpdateDto passwordUpdateDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(()->
                        new BoardNotFoundException(
                                "게시글이 존재하지 않습니다."
                        ));

        // 기존 비밀번호 확인
        if (!board.getPassword().equals(passwordUpdateDto.getCurrentPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        // 새 비밀번호로 변경
        board.setPassword(passwordUpdateDto.getNewPassword());
        boardRepository.save(board);
    }

    // DELETE (삭제)
    public boolean deleteBoard(Long id) {
        Board existingBoard = boardRepository.findById(id).orElse(null);

        if (existingBoard != null) {
            boardRepository.delete(existingBoard);
            return true;
        }
        return false;
    }
}
