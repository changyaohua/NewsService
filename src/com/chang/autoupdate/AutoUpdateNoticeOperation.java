package com.chang.autoupdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.chang.news.InitNewsData;
import com.chang.news.InitNewsJiSuanJi;
import com.chang.news.InitNewsRuanJian;
import com.chang.news.InitSchoolJob;
import com.chang.news.InitSchoolNews;
import com.chang.news.bean.NoticeBean;
import com.chang.news.biz.NoticeBiz;
import com.chang.news.biz.NoticeBizImpl;

public class AutoUpdateNoticeOperation{

	public static void update(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("http://xsc.nuc.edu.cn/xwzx/zytz.htm", "hongtai_important_notice");
		map.put("http://xsc.nuc.edu.cn/xwzx/gzdt.htm", "hongtai_work_notice");
		map.put("http://xsc.nuc.edu.cn/xwzx/xyxw.htm", "hongtai_school_notice");
		map.put("http://xsc.nuc.edu.cn/index/lht.htm", "hongtai_top_notice");
		
		Set<NoticeBean> noticeSet = null;
		InitNewsData initNewsData = new InitNewsData();
		for(String urlPath : map.keySet()){
			noticeSet = initNewsData.getDataWithImageFromWeb(urlPath);
			updateDataBase(noticeSet,map.get(urlPath));
		}
		
		//特殊情况：讲座预告解析方式略有不同
		noticeSet = initNewsData.getAdvanceNoticeFromWeb("http://xsc.nuc.edu.cn/rwxx/jzyg.htm");
		updateDataBase(noticeSet,"hongtai_advance_notice");
		
//		noticeSet = new InitSchoolJob().getDataFromWeb("http://csce.nuc.edu.cn/xs/jyxx.htm");
//		updateDataBase(noticeSet,"hongtai_school_job");
//		
//		noticeSet = new InitSchoolNews().getDataFromWeb("http://www.nuc.edu.cn/ejlb.jsp?urltype=tree.TreeTempUrl&wbtreeid=1106");
//		updateDataBase(noticeSet,"hongtai_school_news");
//		
//		noticeSet = new InitNewsRuanJian().getDataFromWeb("http://csce.nuc.edu.cn/xs/xsdt.htm");
//		updateDataBase(noticeSet,"hongtai_news_jisuanji");
//		
//		noticeSet = new InitNewsJiSuanJi().getDataFromWeb("http://ss.nuc.edu.cn/newDispatch.php");
//		updateDataBase(noticeSet,"hongtai_news_ruanjian");
	}

	private static void updateDataBase(Set<NoticeBean> noticeSet,String sqlTableName) {
		NoticeBiz noticeBiz = new NoticeBizImpl();
		List<NoticeBean> newNoticeList = new ArrayList<NoticeBean>();
		
		newNoticeList.addAll(noticeSet);  //将Set集合转换为List集合
		NoticeBean oldFirstNoticeBean = noticeBiz.fetchFirstNotice(sqlTableName);  //获取当前数据库中的第一条数据
		List<NoticeBean> newAddNotice = null;
		if (newNoticeList.get(0).equals(oldFirstNoticeBean)) {
			// 若果没有更新数据的话不返回数据
			System.out.println(sqlTableName +  "未更新数据");
		} else {
			newAddNotice = new ArrayList<NoticeBean>();
			for (NoticeBean noticeBean : newNoticeList) {
				if (noticeBean.equals(oldFirstNoticeBean)) {
					break;
				} else {
					newAddNotice.add(noticeBean);
				}
			}

			// 将新更新的写回数据库中
			Collections.reverse(newAddNotice);
			boolean isSuccess = noticeBiz.insertNewsData(newAddNotice,
					sqlTableName);
			if (isSuccess) {
				System.out.println(sqlTableName + "新增数据成功！");
			} else {
				System.out.println(sqlTableName + "新增数据失败！");
			}
		}

	}

}
