package com.example.boardPrj.service;

import com.example.boardPrj.dto.BoardLikeDTO;
import com.example.boardPrj.mapper.BoardLikeMapper;
import com.example.boardPrj.mapper.BoardMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional  // 이것이 중요!
@Slf4j
public class BoardLikeService {

    private final BoardLikeMapper boardLikeMapper;
    private final BoardMapper boardMapper;

    public boolean toggleLike(Long boardId, String userIp) {
        log.info("toggleLike 호출 - boardId: {}, userIp: {}", boardId, userIp);

        boolean isLiked = isLikedByUser(boardId, userIp);
        log.info("현재 좋아요 상태: {}", isLiked);

        try {
            if (isLiked) {
                // 좋아요 취소
                log.info("좋아요 취소 시도");
                int deleteResult = boardLikeMapper.delete(boardId, userIp);
                log.info("좋아요 취소 결과: {}", deleteResult);

                if (deleteResult <= 0) {
                    throw new RuntimeException("좋아요 취소에 실패했습니다.");
                }
            } else {
                // 좋아요 추가
                log.info("좋아요 추가 시도");
                BoardLikeDTO boardLikeDTO = new BoardLikeDTO(boardId, userIp);
                int insertResult = boardLikeMapper.insert(boardLikeDTO);
                log.info("좋아요 추가 결과: {}", insertResult);

                if (insertResult <= 0) {
                    throw new RuntimeException("좋아요 추가에 실패했습니다.");
                }
            }

            // 좋아요 수 업데이트
            updateLikeCount(boardId);

            return !isLiked;

        } catch (Exception e) {
            log.error("좋아요 처리 중 오류 발생", e);
            throw e; // 트랜잭션 롤백을 위해 예외 재발생
        }
    }

    @Transactional(readOnly = true)
    public boolean isLikedByUser(Long boardId, String userIp) {
        log.info("좋아요 여부 확인 - boardId: {}, userIp: {}", boardId, userIp);
        int count = boardLikeMapper.existsByBoardIdAndUserIp(boardId, userIp);
        log.info("좋아요 여부 확인 결과: {}", count > 0);
        return count > 0;
    }

    private void updateLikeCount(Long boardId) {
        log.info("좋아요 수 업데이트 시도 - boardId: {}", boardId);
        int likeCount = boardLikeMapper.countByBoardId(boardId);
        log.info("조회된 좋아요 수: {}", likeCount);

        int updateResult = boardMapper.updateLikeCount(boardId, likeCount);
        log.info("좋아요 수 업데이트 결과: {}", updateResult);

        if (updateResult <= 0) {
            throw new RuntimeException("좋아요 수 업데이트에 실패했습니다.");
        }
    }

    @Transactional(readOnly = true)
    public int getLikeCount(Long boardId) {
        log.info("좋아요 수 조회 - boardId: {}", boardId);
        int count = boardLikeMapper.countByBoardId(boardId);
        log.info("좋아요 수 조회 결과: {}", count);
        return count;
    }
}