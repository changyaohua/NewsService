package com.chang.news;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chang.news.bean.NoticeBean;
import com.chang.news.biz.NoticeBiz;
import com.chang.news.biz.NoticeBizImpl;

/**
 * 此类用于从解析网页并将数据加载到mysql数据库中
 * 
 * @author 常耀华
 * @version v1.0
 *
 */
public class InitNewsRuanJian {
	
	private int page = 1;
	private static String urlPath = "http://ss.nuc.edu.cn/newDispatch.php";
	private String urlNextPath = "http://ss.nuc.edu.cn/newDispatch.php?&page=";
	private String urlContentPath = "http://ss.nuc.edu.cn/";
	private String currUrlPath;

	private static String sqlTableName = "hongtai_news_ruanjian";

	private static Set<NoticeBean> noticeSet;

	/**
	 * 用于中北大学计算机工程学院就业信息网页列表的解析
	 * 
	 * @param url
	 *            校园新闻的url
	 * @return 封装解析到的网页信息的set集合
	 */
	public Set<NoticeBean> getDataFromWeb(String url) {
		currUrlPath = url;
		Document doc = null;
		Set<NoticeBean> tempList = new LinkedHashSet<NoticeBean>();
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Document content = Jsoup.parse(doc.toString());
		Elements elements = content.select(".hdnews li");
		NoticeBean notice;
		for (Element element : elements) {
			notice = new NoticeBean();
			notice.setTitle(element.child(1).text());
			String time = element.child(2).text();
			notice.setTime(time);
			String contentUrl = urlContentPath + element.child(1).attr("href");
			notice.setUrl(contentUrl);
			tempList.add(notice);

		}
		return tempList;
	}

	public void onLoad() {
		while (true) {
			page++;
			currUrlPath = urlNextPath + page;
			Set<NoticeBean> mlist = null;
			try {
				mlist = getDataFromWeb(currUrlPath);
			} catch (Exception e) {
				break;
			}
			if (mlist.size() < 5 ||mlist == null) {
				noticeSet.addAll(mlist);
				break;
			} else {
				noticeSet.addAll(mlist);
			}
		}

	}

	

	public static void main(String[] args) {
		InitNewsRuanJian initSchoolJob = new InitNewsRuanJian();
		NoticeBiz noticeBiz = new NoticeBizImpl();
		noticeSet = initSchoolJob.getDataFromWeb(urlPath);
		initSchoolJob.onLoad();
		int count = 0;
		List<NoticeBean> noticeList = new ArrayList<NoticeBean>();
		noticeList.addAll(noticeSet);
		Collections.reverse(noticeList);
		for (NoticeBean bean : noticeList) {
			System.out.println(bean);
			count++;
		}
		System.out.println("共计：" + count);
		boolean isSuccess = noticeBiz.insertNewsData(noticeList, sqlTableName);
		if (isSuccess) {
			System.out.println("初始化数据成功！");
		} else {
			System.out.println("初始化数据失败！");
		}
	}

}
