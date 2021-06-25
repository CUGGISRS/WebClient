package com.github.wxiaoqi.security.dzsw.sys.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "com_qa_reply_info")
public class QAReplyInfo {
    @Id
    private Integer id;

    @Column(name = "qa_id")
    private Integer qaId;

    /**
     * 回复日期
     */
    @Column(name = "reply_time")
    private Date replyTime;

    /**
     * 回复人
     */
    private String responder;

    /**
     * 是否已删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

    /**
     * 回复内容
     */
    private String content;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return qa_id
     */
    public Integer getQaId() {
        return qaId;
    }

    /**
     * @param qaId
     */
    public void setQaId(Integer qaId) {
        this.qaId = qaId;
    }

    /**
     * 获取回复日期
     *
     * @return reply_time - 回复日期
     */
    public Date getReplyTime() {
        return replyTime;
    }

    /**
     * 设置回复日期
     *
     * @param replyTime 回复日期
     */
    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    /**
     * 获取回复人
     *
     * @return responder - 回复人
     */
    public String getResponder() {
        return responder;
    }

    /**
     * 设置回复人
     *
     * @param responder 回复人
     */
    public void setResponder(String responder) {
        this.responder = responder;
    }

    /**
     * 获取是否已删除
     *
     * @return is_deleted - 是否已删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否已删除
     *
     * @param isDeleted 是否已删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取回复内容
     *
     * @return content - 回复内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置回复内容
     *
     * @param content 回复内容
     */
    public void setContent(String content) {
        this.content = content;
    }
}