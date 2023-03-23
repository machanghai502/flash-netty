package the.flash.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import the.flash.codec.PacketDecoder;
import the.flash.codec.PacketEncoder;
import the.flash.codec.Spliter;
import the.flash.server.handler.LifeCyCleTestHandler;
import the.flash.server.handler.LoginRequestHandler;
import the.flash.server.handler.MessageRequestHandler;

import java.util.Date;

/**
 * channelhandler的生命周期
 *
 * 增加一个pipeline上所有的inboundhandler的生命周期方法调用，结果是每个方法都会一次按照pipeline的顺序依次调用。然后下一个方法触发的时候继续按照事件传播的顺序依次调用channelhandler的对应方法
 *
 * the.flash.server.handler.LifeCyCleTestHandler逻辑处理器被添加：handlerAdded()
 * the.flash.codec.PacketDecoder逻辑处理器被添加：handlerAdded()
 * the.flash.server.handler.LoginRequestHandler逻辑处理器被添加：handlerAdded()
 * the.flash.server.handler.MessageRequestHandler逻辑处理器被添加：handlerAdded()
 * the.flash.codec.Spliterchannel 绑定到线程(NioEventLoop)：channelRegistered()
 * the.flash.server.handler.LifeCyCleTestHandlerchannel 绑定到线程(NioEventLoop)：channelRegistered()
 * the.flash.codec.PacketDecoderchannel 绑定到线程(NioEventLoop)：channelRegistered()
 * the.flash.server.handler.LoginRequestHandlerchannel 绑定到线程(NioEventLoop)：channelRegistered()
 * the.flash.server.handler.MessageRequestHandlerchannel 绑定到线程(NioEventLoop)：channelRegistered()
 * the.flash.codec.Spliterchannel 准备就绪：channelActive()
 * the.flash.server.handler.LifeCyCleTestHandlerchannel 准备就绪：channelActive()
 * the.flash.codec.PacketDecoderchannel 准备就绪：channelActive()
 * the.flash.server.handler.LoginRequestHandlerchannel 准备就绪：channelActive()
 * the.flash.server.handler.MessageRequestHandlerchannel 准备就绪：channelActive()
 * the.flash.codec.Spliterchannel 有数据可读：channelRead()
 * the.flash.server.handler.LifeCyCleTestHandlerchannel 有数据可读：channelRead()
 * the.flash.codec.PacketDecoderchannel 有数据可读：channelRead()
 * the.flash.server.handler.LoginRequestHandlerchannel 有数据可读：channelRead()
 * Thu Mar 23 22:25:46 CST 2023: 收到客户端登录请求……
 * Thu Mar 23 22:25:46 CST 2023: 登录成功!
 * the.flash.codec.Spliterchannel 某次数据读完：channelReadComplete()
 * the.flash.server.handler.LifeCyCleTestHandlerchannel 某次数据读完：channelReadComplete()
 * the.flash.codec.PacketDecoderchannel 某次数据读完：channelReadComplete()
 * the.flash.server.handler.LoginRequestHandlerchannel 某次数据读完：channelReadComplete()
 * the.flash.server.handler.MessageRequestHandlerchannel 某次数据读完：channelReadComplete()
 */
public class NettyServer {

    private static final int PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new LifeCyCleTestHandler());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });
        bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
