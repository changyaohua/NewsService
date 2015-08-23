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
public class initNewsData {
	
	private static String urlPath = "http://xsc.nuc.edu.cn/xwzx/zytz.htm";
	private String urlNextPath = "http://xsc.nuc.edu.cn/xwzx/zytz";
	private String urlContentPath = "http://xsc.nuc.edu.cn";
	private String currUrlPath;
	
	private static Set<NoticeBean> noticeSet ;
	
	public Set<NoticeBean> getDataFromWeb(String url) {
		currUrlPath = url;
		Document doc = null;
		Set<NoticeBean> tempList = new LinkedHashSet<NoticeBean>();
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
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
	
	public void onLoad() {
		while (true) {
			String nextUrl = getNextUrlPath(currUrlPath);
			currUrlPath = urlNextPath + nextUrl;
			Set<NoticeBean> mlist = getDataFromWeb(currUrlPath);
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
		initNewsData hongTaiNewsService = new initNewsData();
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
		boolean isSuccess = noticeBiz.insertNewsData(noticeList);
		if (isSuccess) {
			System.out.println("初始化数据成功！");
		} else {
			System.out.println("初始化数据失败！");
		}
	}

}
