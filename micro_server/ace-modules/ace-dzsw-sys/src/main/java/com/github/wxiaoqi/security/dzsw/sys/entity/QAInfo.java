package com.github.wxiaoqi.security.dzsw.sys.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "com_qa_info")
public class QAInfo {
    @Id
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 留言时间
     */
    @Column(name = "message_time")
    private Date messageTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 留言人
     */
    private String commenter;

    /**
     * 留言人联系方式
     */
    @Column(name = "commenter_phone")
    private String commenterPhone;

    /**
     * 目标系统
     */
    @Column(name = "target_system")
    private String targetSystem;

    /**
     * 地址
     */
    private String address;

    /**
     * 类型(留言/建议) 追溯信息系统字段
     */
    private String type;

    /**
     * 用户ID
     */
    @Column(name = "userId")
    private Integer userid;

    /**
     * 是否回复（0表示未回复，1表示已回复）
     */
    @Column(name = "is_reply")
    private Integer isReply;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

    /**
     * 内容
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
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取留言时间
     *
     * @return message_time - 留言时间
     */
    public Date getMessageTime() {
        return messageTime;
    }

    /**
     * 设置留言时间
     *
     * @param messageTime 留言时间
     */
    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取留言人
     *
     * @return commenter - 留言人
     */
    public String getCommenter() {
        return commenter;
    }

    /**
     * 设置留言人
     *
     * @param commenter 留言人
     */
    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    /**
     * 获取留言人联系方式
     *
     * @return commenter_phone - 留言人联系方式
     */
    public String getCommenterPhone() {
        return commenterPhone;
    }

    /**
     * 设置留言人联系方式
     *
     * @param commenterPhone 留言人联系方式
     */
    public void setCommenterPhone(String commenterPhone) {
        this.commenterPhone = commenterPhone;
    }

    /**
     * 获取目标系统
     *
     * @return target_system - 目标系统
     */
    public String getTargetSystem() {
        return targetSystem;
    }

    /**
     * 设置目标系统
     *
     * @param targetSystem 目标系统
     */
    public void setTargetSystem(String targetSystem) {
        this.targetSystem = targetSystem;
    }

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取类型(留言/建议) 追溯信息系统字段
     *
     * @return type - 类型(留言/建议) 追溯信息系统字段
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型(留言/建议) 追溯信息系统字段
     *
     * @param type 类型(留言/建议) 追溯信息系统字段
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取用户ID
     *
     * @return userId - 用户ID
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置用户ID
     *
     * @param userid 用户ID
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取是否回复（0表示未回复，1表示已回复）
     *
     * @return is_reply - 是否回复（0表示未回复，1表示已回复）
     */
    public Integer getIsReply() {
        return isReply;
    }

    /**
     * 设置是否回复（0表示未回复，1表示已回复）
     *
     * @param isReply 是否回复（0表示未回复，1表示已回复）
     */
    public void setIsReply(Integer isReply) {
        this.isReply = isReply;
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }


    public List<QAReplyInfo> getReplyInfos() {
        return replyInfos;
    }

    public void setReplyInfos(List<QAReplyInfo> replyInfos) {
        this.replyInfos = replyInfos;
    }

    @Transient
    private List<QAReplyInfo> replyInfos;
}
