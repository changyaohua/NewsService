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
public class InitNewsData {
	
	private static String urlPath = "http://xsc.nuc.edu.cn/rwxx/jzyg.htm";
	private String urlNextPath = "http://xsc.nuc.edu.cn/rwxx/jzyg";
	private String urlContentPath = "http://xsc.nuc.edu.cn";
	private String currUrlPath;
	
	private static String sqlTableName = "hongtai_advance_notice";
	
	private static Set<NoticeBean> noticeSet ;
	
	/**
	 * 用于最新消息，学院新闻，工作动态网页列表的解析
	 * 
	 * @param url  讲座预告的url
	 * @return    封装解析到的网页信息的set集合
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
		Elements elements = content.getElementsByClass("c44514");
		NoticeBean notice;
		for (Element element : elements) {
			notice = new NoticeBean();
			notice.setTitle(element.text());
			String time = element.parent().nextElementSibling().getElementsByClass("timestyle44514").text();
			notice.setTime(time);
			String contentUrl = urlContentPath
					+ element.attr("href").replace("..", "");
			notice.setUrl(contentUrl);
			tempList.add(notice);
		}
		return tempList;
	}
	
	/**
	 * 因为讲座预告的的解析与最新消息，学院新闻，工作动态不同，所以单另一个方法
	 * 主要区别在获取信息的class宾浩不同c44536与c44514
	 * 主要onload()的方法也得做出相应的调整
	 * 
	 * @param url  讲座预告的url
	 * @return     封装解析到的网页信息的set集合
	 */
	public Set<NoticeBean> getAdvanceNoticeFromWeb(String url) {
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
		Elements elements = content.getElementsByClass("c44536");
		NoticeBean notice;
		for (Element element : elements) {
			notice = new NoticeBean();
			notice.setTitle(element.text());
			String time = element.parent().nextElementSibling().getElementsByClass("timestyle44536").text();
			notice.setTime(time);
			String contentUrl = urlContentPath
					+ element.attr("href").replace("..", "");
			notice.setUrl(contentUrl);
			tempList.add(notice);
		}
		return tempList;
	}
	
	public void onLoad() {
		while (true) {
			String nextUrl = getNextUrlPath(currUrlPath);
			currUrlPath = urlNextPath + nextUrl;
			Set<NoticeBean> mlist = getDataFromWeb(currUrlPath);
//			Set<NoticeBean> mlist = getAdvanceNoticeFromWeb (currUrlPath);
			if (mlist == null || nextUrl == null) {
				break;
			} else {
				noticeSet.addAll(mlist);
			}
		}

				
	}

public String getNextUrlPath(String url) {
	Document doc = null;
	String nextUrlPath = null;
	Document content = null;
	try {
		doc = Jsoup.connect(url).get();
		content = Jsoup.parse(doc.toString());
	} catch (Exception e) {
		return null;
	}
	Elements elements = content.getElementsByClass("Next");
	for (Element element : elements) {
		if (element.text().equals("下页")) {
			nextUrlPath = "/" + getIndexFromString(element.attr("href"))
					+ ".htm";
			break;
		}
	}
	return nextUrlPath;
}

private String getIndexFromString(String str) {
	String index = null;
	Pattern p = Pattern.compile("(\\d+)");
	Matcher m = p.matcher(str);
	if (m.find()) {
		index = m.group();
	}
	return index;
}
	
	public static void main(String[] args) {
		InitNewsData hongTaiNewsService = new InitNewsData();
		NoticeBiz noticeBiz = new NoticeBizImpl();
		noticeSet = hongTaiNewsService.getDataFromWeb(urlPath);
		hongTaiNewsService.onLoad();
		int count = 0;
		List<NoticeBean> noticeList = new ArrayList<NoticeBean>();
		noticeList.addAll(noticeSet);
		Collections.reverse(noticeList);
		for (NoticeBean bean : noticeList) {
			System.out.println(bean);
			count++;
		}
		System.out.println("共计：" + count);
		boolean isSuccess = noticeBiz.insertNewsData(noticeList,sqlTableName);
		if (isSuccess) {
			System.out.println("初始化数据成功！");
		} else {
			System.out.println("初始化数据失败！");
		}
	}

}
