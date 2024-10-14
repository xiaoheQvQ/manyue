package com.hsx.netty;

import java.io.Serializable;

public class ChatMsg implements Serializable {

	private static final long serialVersionUID = 3611169682695799175L;
	
	private String senderId;		// 发送者的用户id	
	private String receiverId;		// 接受者的用户id
	private String msg;				// 聊天内容
	private Long msgId;			// 用于消息的签收
	
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "ChatMsg{" +
				"senderId='" + senderId + '\'' +
				", receiverId='" + receiverId + '\'' +
				", msg='" + msg + '\'' +
				", msgId='" + msgId + '\'' +
				'}';
	}
}
