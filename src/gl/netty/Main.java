package gl.netty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main { 
	
	public static void main(String[] args) {
		if(args.length < 6){
			print();
			System.exit(1);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");						
		
		int num = Integer.valueOf(args[0]);
		int conNum = Integer.valueOf(args[1]);
		String ip = args[2];
		int port = Integer.valueOf(args[3]);
		boolean ssl = false;
		
		if("1".equals(args[4])){
			ssl = true;
		}else{
			ssl = false;
		}
		
		String sql = args[5];
		
		System.out.println("start time:"+sdf.format(new Date())+",num:"+num+",conNum:"+conNum+",ip:"+ip+",port:"+port+",ssl:"+ssl+",sql:"+sql+".");
		ExecutorService pool = Executors.newFixedThreadPool(conNum);				
		
		for(int i = 0 ;i<num;i++){
			pool.execute(new Client(ip,port,ssl,sql));
		}
		
		System.out.println("end time:"+sdf.format(new Date()));
	}
	
	private static void print(){
		System.out.println("num conNum ip port ssl sql");
	}

}
