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
	private String sql;
	private boolean ssl;
    private String ip ;
    private int port ;   

    public Client(String ip,int port,boolean ssl,String sql){
    	this.ip = ip;
    	this.port = port;
    	this.ssl = ssl;
    	this.sql = sql;
    }
    
    public  void run() {
        // Configure SSL.
        SslContext sslCtx = null;
        if (ssl) {
            try {
				sslCtx = SslContextBuilder.forClient()
				    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			} catch (SSLException e) {				
				e.printStackTrace();
			}
        } else {
            sslCtx = null;
        }
                
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ClientInitializer(sslCtx,ip,port,sql));

            // Make a new connection.
            ChannelFuture f;
			try {
				f = b.connect(ip, port).sync();
				f.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        } finally {
            group.shutdownGracefully();
        }
    }

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
    

}
