package org.benmobile.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PingUtil {
	
	//ping的ip
	private static final String HERMAN_IP = "www.baidu.com";
	
	/**
	 * @see 方法一 最常用的 PING 方法(Runtime)直接调用服务器本身的ping命令
	 * @author Herman.Xiong
	 * @date 2014年5月9日 14:46:33
	 * @version V2.0
	 * @since jdk 1.6 , tomcat 6.0
	 * @return void
	 */
	public static void test0() {
		// 获取当前程序的运行进程对象
		Runtime runtime = Runtime.getRuntime();
		// 声明处理类对象
		Process process = null;
		// 结果
		boolean step = true;
		int i=0;
		while(step){
			ping(i,runtime,process);
			if(i>5){
				break;
			}
			i++;
		}
		if(i>5){
			System.out.println("ping " + HERMAN_IP+"通了......");
		}
	}
	
	private static void ping(int count,Runtime runtime,Process process){
		try {
			// 执行PING命令
			process = runtime.exec("ping " + HERMAN_IP);
			// 实例化输入流
			InputStream is = process.getInputStream();
			// 把输入流转换成字节流
			InputStreamReader isr = new InputStreamReader(is);
			// 从字节中读取文本
			BufferedReader br = new BufferedReader(isr);
			String line="";
			while ((line = br.readLine()) != null) {
				if (line.contains("TTL")) {
					System.out.println(line);
				}else if((count==0)&&line.indexOf("具有 32 字节的数据")>-1){
					System.out.println(line);
					count++;
				}
			}
			is.close();
			isr.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			runtime.exit(1);
		}
	}
	public static void main(String[] args) {
		//循环ping 100次
		test0();
		//一直ping只需改HERMAN_IP = "www.baidu.com -t";
	}
}
