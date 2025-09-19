package com.example.boardPrj.service;

import com.example.boardPrj.dto.BoardDTO;
import com.example.boardPrj.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    public List<BoardDTO> getAllBoards(){
        return boardMapper.findAll();
    }

    public BoardDTO getBoardById(Long id){
        return boardMapper.findById(id);
    }

    public void createBoard(BoardDTO boardDTO){
        boardMapper.insert(boardDTO);
    }

    public void updateBoard(BoardDTO boardDTO){
        boardMapper.update(boardDTO);
    }

    public void deleteBoard(Long id){
        boardMapper.delete(id);
    }

    public int getTotalCount(){
        return boardMapper.count();
    }
}
