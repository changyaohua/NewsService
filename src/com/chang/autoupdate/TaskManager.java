package com.chang.autoupdate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TaskManager implements ServletContextListener {

	/**
	 * 每分钟的毫秒数
	 */
	public static final long PERIOD_MINUTE = 1000*60;

	Timer timer;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		timer = new Timer("定时更新新闻数据", true);
		timer.schedule(new BackUpUpadteData(), 0, PERIOD_MINUTE*20);
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (timer != null) {
			timer.cancel();
		}
	}

	class BackUpUpadteData extends TimerTask {
		private boolean isRunning = false;  //判断定时任务是否执行完毕
		private boolean isWorkTime = false; //判断当前时间是否为工作时间
		
		private int currHour;  //当前时间的小时的值
		@Override
		public void run() {
			Calendar cal = Calendar.getInstance();
			currHour = cal.get(Calendar.HOUR_OF_DAY);
			if (currHour >= 7 && currHour <=23) {
				isWorkTime = true;
			}
			if (isWorkTime) {
				if (!isRunning) {
					isRunning = true;
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
					System.out.println("开始执行更新任务..."); // 开始任务
					
					// working add what you want to do
					AutoUpdateNoticeOperation.update();
					
					System.out.println("执行更新任务完成..."); // 任务完成
					isRunning = false;
				} else {
					System.out.println("上一次任务执行还未结束..."); // 上一次任务执行还未结束
				}
			} else {
				System.out.println("不在工作时间，未进行更新任务...");
			}
			
		}

	}

}
