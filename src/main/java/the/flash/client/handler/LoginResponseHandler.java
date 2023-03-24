package the.flash.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import the.flash.protocol.request.LoginRequestPacket;
import the.flash.protocol.response.LoginResponsePacket;
import the.flash.util.LoginUtil;

import java.util.Date;
import java.util.UUID;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");

        // 写数据
        //ctx.channel().writeAndFlush(loginRequestPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        if (loginResponsePacket.isSuccess()) {
            System.out.println(new Date() + ": 客户端登录成功");
            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
        }
    }

    /**
     * 这个方法会被调用，服务端调用close方法，把当前channel关闭了，客户都感知到了会触发这个方法。
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接被关闭!");
        //如果没有下边的代码，则不会触发pipeline中的下一个handler（MessageResponseHandler），也就是MessageResponseHandler中的channelInactive不会被调用
        //channelInactive是顺着pipeline链式调用的
        //super.channelInactive(ctx);
    }
}
