package cn.ttpai.mqlistener.admin.common.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**   
 * @Description:ajax工具类 
 * @author: rickycoder
 * @date:   2014年12月24日 下午7:48:45   
 */
public class AjaxUtil {
	/**
	 * 
	 * @Description: 判断是否是AJAX请求 
	 * @param request
	 * @return 
	 * @return: Boolean      
	 * @throws
	 */
	public static Boolean isAjax(HttpServletRequest request){
		String requestType=request.getHeader("X-Requested-With");
		return Objects.equals("XMLHttpRequest", requestType);
	}
	
	/**
	 * 
	 * @Description: 判断JSONP请求 
	 * @param request
	 * @return 
	 * @return: Boolean      
	 * @throws
	 */
	public static Boolean isJsonp(HttpServletRequest request){
		return (isAjax(request) && null!=request.getParameter("callback"));
	}
}
