package the.flash.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import the.flash.protocol.response.MessageResponsePacket;

import java.util.Date;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {
        System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //如果pipeline中的上一个handler的channelInactive没有调用父类的channelInactive，则不会触发当前的channelInactive方法
        System.out.println("MessageResponseHandler channelInactive invoked");
        super.channelInactive(ctx);
    }
}
