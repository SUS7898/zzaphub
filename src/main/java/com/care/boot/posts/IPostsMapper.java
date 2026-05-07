package com.care.boot.posts;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface IPostsMapper {

    // 게시글 목록 조회 (카테고리 필터링 포함)
    List<PostsDTO> getPostsList(@Param("category") String category, @Param("begin") int begin, @Param("pageBlock") int pageBlock);

    // 게시글 총 개수
    int getTotalCountByCategory(@Param("category") String category);

    // 게시글 상세 조회
    PostsDTO postsContent(long id);

    // 조회수 증가
    void incrementHits(long id);

    // 게시글 작성
    void postsWriteProc(PostsDTO posts);

    // 게시글 수정
    int postsModifyProc(PostsDTO posts);

    // 게시글 삭제
    void postsDeleteProc(long id);

    // ✅ 게시글 삭제 전 댓글 전체 삭제
    void deleteCommentsByPostId(long postId);

    // 메인용: 최신글
    List<PostsDTO> getRecentByCategory(@Param("category") String category, @Param("limit") int limit);

    // 메인용: 인기글
    List<PostsDTO> getPopularPosts(@Param("limit") int limit);

    // 파일 다운로드 (필요 시 구현)
    String postsDownload(long id);
    
 // 기존 메서드 아래에 추가
    String findIdByEmail(@Param("email") String email);
    
    void addPoint(@Param("id") Long id, @Param("point") int point);
}