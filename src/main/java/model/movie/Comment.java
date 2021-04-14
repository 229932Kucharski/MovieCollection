package model.movie;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;

public class Comment {

    private final int commentId;
    private final int userId;
    private final int videoId;
    private String content;
    private final LocalDate commentDate;

    public Comment(int commentId, int userId, int videoId, String content, LocalDate commentDate) {
        this.commentId = commentId;
        this.userId = userId;
        this.videoId = videoId;
        this.content = content;
        this.commentDate = commentDate;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentId() {
        return commentId;
    }

    public int getUserId() {
        return userId;
    }

    public int getVideoId() {
        return videoId;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCommentDate() {
        return commentDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("commentId", commentId)
                .append("userId", userId)
                .append("videoId", videoId)
                .append("content", content)
                .append("commentDate", commentDate)
                .toString();
    }
}
