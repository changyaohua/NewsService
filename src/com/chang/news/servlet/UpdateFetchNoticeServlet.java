package com.chang.news.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chang.news.InitNewsData;
import com.chang.news.bean.NoticeBean;
import com.chang.news.biz.NoticeBiz;
import com.chang.news.biz.NoticeBizImpl;

/**
 * Servlet implementation class FirstFetchNoticeServlet
 */
@WebServlet("/UpdateFetchNoticeServlet")
public class UpdateFetchNoticeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateFetchNoticeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NoticeBiz noticeBiz = new NoticeBizImpl();
		Set<NoticeBean> noticeSet = null;
		String urlPath = (String)request.getAttribute("urlPath");
		
		if ("http://xsc.nuc.edu.cn/rwxx/jzyg.htm".equals(urlPath)) {
			noticeSet = new InitNewsData().getAdvanceNoticeFromWeb(urlPath);
		} else {
			noticeSet = new InitNewsData().getDataFromWeb(urlPath);
		}
		List<NoticeBean> newNoticeList = new ArrayList<NoticeBean>();
		newNoticeList.addAll(noticeSet);
		NoticeBean oldFirstNoticeBean = noticeBiz.fetchFirstNotice();
		List<NoticeBean> newAddNotice = null;
		if (newNoticeList.get(0).equals(oldFirstNoticeBean)) {
			//若果没有更新数据的话不返回数据
			System.out.println("未更新数据");
		} else {
			newAddNotice = new ArrayList<NoticeBean>();
			for (NoticeBean noticeBean : newNoticeList) {
				if (noticeBean.equals(oldFirstNoticeBean)) {
					break;
				} else {
					newAddNotice.add(noticeBean);
				}
			}
			String sqlTableName = (String)request.getAttribute("sqlTableName");
			List<NoticeBean> noticeBeanList = noticeBiz.fetchNoticeByPageNO(1,sqlTableName);
			
	        //将新更新的写回数据库中
	        Collections.reverse(newAddNotice);
	        boolean isSuccess = noticeBiz.insertNewsData(newAddNotice,sqlTableName);
			if (isSuccess) {
				System.out.println("新增数据成功！");
			} else {
				System.out.println("新增数据失败数据失败！");
			}
			
			//将新更新的数据返回客户端
			String jsonArray = JSON.toJSONString(noticeBeanList);
			System.out.println(jsonArray.toString());
			response.getOutputStream().write(jsonArray.getBytes("UTF-8"));  
	        response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
