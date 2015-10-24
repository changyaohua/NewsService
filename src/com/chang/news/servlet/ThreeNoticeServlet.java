package com.chang.news.servlet;

import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class SecondNoticeServlet
 */
@WebServlet("/ThreeNoticeServlet")
public class ThreeNoticeServlet extends HongTaiNoticeServlet {
	private static final long serialVersionUID = 1L;

    @Override
    protected void initSQLTableName() {
    	// TODO Auto-generated method stub
    	sqlTableName = "hongtai_school_notice";
    	urlPath = "http://xsc.nuc.edu.cn/xwzx/xyxw.htm";
    }
}
