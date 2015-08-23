package com.chang.news.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.chang.news.bean.NoticeBean;
import com.chang.news.biz.NoticeBiz;
import com.chang.news.biz.NoticeBizImpl;

/**
 * Servlet implementation class HongTaiNoticeServlet
 */
@WebServlet("/HongTaiNoticeServlet")
public class HongTaiNoticeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NoticeBiz noticeBiz = new NoticeBizImpl();
		List<NoticeBean> noticeBeanList = noticeBiz.fetchAllNotice();
		JSONArray jsonArray = JSONArray.fromObject(noticeBeanList);
		System.out.println(jsonArray.toString());
		response.getOutputStream().write(jsonArray.toString().getBytes("UTF-8"));  
        response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
