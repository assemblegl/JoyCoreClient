package gl.netty;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class Client implements Runnable{
	
	static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8322"));
    //static final int COUNT = Integer.parseInt(System.getProperty("count", "1"));

    public  void run() {
        // Configure SSL.
        SslContext sslCtx = null;
        if (SSL) {
            try {
				sslCtx = SslContextBuilder.forClient()
				    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			} catch (SSLException e) {				
				e.printStackTrace();
			}
        } else {
            sslCtx = null;
        }

        
        String sql = "select * from hive2.default.card limit 100";
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ClientInitializer(sslCtx,sql));

            // Make a new connection.
            ChannelFuture f;
			try {
				f = b.connect(HOST, PORT).sync();
				f.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Wait until the connection is closed.
            
            // Get the handler instance to retrieve the answer.
            //ClientHandler handler =
            //    (ClientHandler) f.channel().pipeline().last();

            // Print out the answer.
            //System.err.format("Factorial of %,d is: %,d", COUNT, handler.getFactorial());
        } finally {
            group.shutdownGracefully();
        }
    }

}
