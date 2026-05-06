package com.care.boot.posts;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface IPostsMapper {
    // 게시글 목록 (시작 인덱스, 가져올 개수)
    List<PostsDTO> postsForm(@Param("begin") int begin, @Param("pageBlock") int pageBlock);

    int totalCount();

    // 게시글 작성
    void postsWriteProc(PostsDTO posts);

    // 게시글 상세 조회 (long 타입으로 통일)
    PostsDTO postsContent(long id);

    // 조회수 증가
    void incrementHits(long id);

    // 파일 다운로드 경로 조회
    String postsDownload(long id);

    // 게시글 수정
    int postsModifyProc(PostsDTO posts);

    // 게시글 삭제
    void postsDeleteProc(long id);
    

}