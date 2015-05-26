package gl.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
	
	private final SslContext sslCtx;
	private String sql;
	
    public ClientInitializer(SslContext sslCtx,String sql) {
        this.sslCtx = sslCtx;
        this.sql = sql;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc(), Client.HOST, Client.PORT));
        }

        // Enable stream compression (you can remove these two if unnecessary)
        //pipeline.addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        //pipeline.addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));

        // Add the number codec first,
        pipeline.addLast(new MsgDecoder());
        pipeline.addLast(new MsgEncoder());
        
        // and then business logic.
        pipeline.addLast(new ClientHandler(sql));
    }

}
