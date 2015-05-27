package gl.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String>{	
	private ChannelHandlerContext ctx;
	private String sql;
	
	public ClientHandler(String sql){
		this.sql = sql;
	}
	
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        sendSql();        
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, final String msg) {
    	System.out.println("client:"+msg+"|");
    	ctx.close();   	
    }    

    private void sendSql() {       
        ChannelFuture future = null;   

        future = ctx.write(sql);
        future.addListener(sqlSender);
        ctx.flush();
    }

    private final ChannelFutureListener sqlSender = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                System.out.println("future is ok!");
            } else {
                future.cause().printStackTrace();
                future.channel().close();
            }
        }
    };
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
