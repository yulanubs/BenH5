package org.benmobile.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * @see PingTest java ping工具测试
 * @author Herman.Xiong
 * @date 2014年5月9日 14:43:01
 */
public class PingTest {
	public PingTest() {
	}

	// ping的ip
	private static final String HERMAN_IP = "video.kotliner.mobi";

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
		// 返回行信息
		String line = null;
		// 输入流
		InputStream is = null;
		// 字节流
		InputStreamReader isr = null;
		// 缓冲流
		BufferedReader br = null;
		// 结果
		boolean res = false;
		try {
			// 执行PING命令
			process = runtime.exec("ping " + HERMAN_IP);
			// 实例化输入流
			is = process.getInputStream();
			// 把输入流转换成字节流
			isr = new InputStreamReader(is);
			// 从字节中读取文本
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if (line.contains("TTL")) {
					res = true;
				}
			}
			is.close();
			isr.close();
			br.close();
			if (res) {
				System.out.println("ping 通  ...");
			} else {
				System.out.println("ping 不通...");
			}
		} catch (IOException e) {
			e.printStackTrace();
			runtime.exit(1);
		}
	}

	/**
	 * @see JDK1.5 PING的新方法但不能用 ，因为 该PING请求端口为7 而大型网站会关闭不需要的端口防止入侵
	 * @author Herman.Xiong
	 * @date 2014年5月9日 15:00:49
	 * @version V2.0
	 * @since jdk 1.6, tomcat 6.0
	 * @return
	 */
	public static void test1() {
		InetAddress address;
		try {
			address = InetAddress.getByName(HERMAN_IP);
			System.out.println("Name: " + address.getHostName());
			System.out.println("Addr: " + address.getHostAddress());
			System.out.println("Reach: " + address.isReachable(3000)); // 是否能通信
			// 返回true或false
			System.out.println(address.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		test0();
		test1();
	}
}
