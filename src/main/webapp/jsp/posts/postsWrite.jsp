<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:import url="/header" />

<style>
    .write-container { max-width: 800px; margin: 40px auto; padding: 20px; border: 1px solid #d0d7de; border-radius: 6px; background: #fff; }
    .write-header { border-bottom: 1px solid #d0d7de; padding-bottom: 10px; margin-bottom: 20px; font-size: 20px; font-weight: 600; }
    .form-group { margin-bottom: 15px; }
    .form-group label { display: block; margin-bottom: 5px; font-weight: 600; font-size: 14px; }
    .form-control { width: 100%; padding: 8px; border: 1px solid #d0d7de; border-radius: 6px; box-sizing: border-box; font-size: 14px; background-color: #f6f8fa; }
    .form-control:focus { background-color: #fff; border-color: #0969da; outline: none; box-shadow: 0 0 0 3px rgba(9, 105, 218, 0.3); }
    textarea.form-control { min-height: 200px; resize: vertical; }
    .btn-submit { background-color: #2da44e; color: white; border: 1px solid rgba(27, 31, 36, 0.15); border-radius: 6px; padding: 8px 16px; font-size: 14px; font-weight: 600; cursor: pointer; float: right; }
    .btn-submit:hover { background-color: #2c974b; }
    .clearfix::after { content: ""; clear: both; display: table; }
</style>

<div class="write-container clearfix">
    <div class="write-header">New Post</div>
    
    <form action="postsWriteProc" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="category">Category</label>
            <select name="category" id="category" class="form-control">
                <option value="자유">자유게시판</option>
                <option value="질문">Q&A</option>
                <option value="공지">공지사항</option>
            </select>
        </div>

        <div class="form-group">
            <label for="title">Title</label>
            <input type="text" name="title" id="title" class="form-control" placeholder="Title" required>
        </div>

        <div class="form-group">
            <label for="content">Content</label>
            <textarea name="content" id="content" class="form-control" placeholder="Leave a comment"></textarea>
        </div>

        <div class="form-group">
            <label for="upfile">Attach files</label>
            <input type="file" name="upfile" id="upfile" class="form-control">
        </div>

        <button type="submit" class="btn-submit">Submit new post</button>
    </form>
</div>
<c:import url="/footer" />












