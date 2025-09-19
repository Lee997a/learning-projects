package com.example.boardPrj.mapper;

import com.example.boardPrj.dto.BoardDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    // 전체 게시글 조회
    List<BoardDTO> findAll();

    // 특정 게시글 조회
    BoardDTO findById(Long id);

    // 게시글 등록
    int insert(BoardDTO boardDTO);

    // 게시글 수정
    int update(BoardDTO boardDTO);

    // 게시글 삭제
    int delete(Long id);

    // 전체 게시글 수 조회
    int count();
}
