package com.hsx.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description: 用于检测channel的心跳handler 
 * 				 继承ChannelInboundHandlerAdapter，从而不需要实现channelRead0方法
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 用户事件触发
	 * @param ctx
	 * @param evt
	 * @throws Exception
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

	}
	
}
