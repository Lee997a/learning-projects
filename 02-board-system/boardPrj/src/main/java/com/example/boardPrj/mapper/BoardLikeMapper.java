package com.example.boardPrj.mapper;

import com.example.boardPrj.dto.BoardLikeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardLikeMapper {

    // 좋아요 추가
    int insert(BoardLikeDTO boardLikeDTO);

    // 좋아요 취소
    int delete(@Param("boardId") Long boardId, @Param("userIp") String userIp);

    // 특정 사용자의 좋아요 여부 확인
    int existsByBoardIdAndUserIp(@Param("boardId") Long boardId, @Param("userIp") String userIp);

    // 게시글의 총 좋아요 수 조회
    int countByBoardId(Long boardId);
}
