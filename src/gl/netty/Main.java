package gl.netty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main { 
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("start time:"+sdf.format(new Date()));
		
		ExecutorService pool = Executors.newFixedThreadPool(10);
		
		//new Thread(new ProcessThread()).start();				

		//int maxarg = Integer.valueOf(args[0]);
		
		
		for(int i = 0 ;i<20;i++){
			pool.execute(new Client());
		}
		
		/*while(!pool.isTerminated()){						
			try {			
				Thread.sleep(1000);											
			} catch (InterruptedException e) {							
				e.printStackTrace();
			}
		}
		
		System.out.println("end time:"+sdf.format(new Date()));*/
	}

}
