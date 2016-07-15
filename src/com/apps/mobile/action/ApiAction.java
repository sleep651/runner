package com.apps.mobile.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apps.common.utils.WsConstants;
import com.apps.mobile.domain.InvalidTicketException;
import com.apps.mobile.domain.ResponseEmptyProperty;
import com.apps.mobile.domain.ResponseProperty;
import com.apps.mobile.domain.ResponsePropertyList;
import com.apps.mobile.domain.Ticket;
import com.apps.mobile.domain.TicketVO;
import com.apps.mobile.domain.UserAccount;
import com.apps.mobile.service.ApiService;
import com.teamsun.core.action.BaseAction;


@Controller
@RequestMapping("/ApiAction.do")
public class ApiAction extends BaseAction implements ApiLogInterface{
	
    @Autowired
    private ApiService apiService;
    
	/******************************************************
	1.1	login 【登录接口】
	输入参数：
		String mobile  	手机号
		String pwd		密码 
	返回值：json对象
	字段说明：
		(1)status:返回状态；
			1:成功;
			0:失败;
			2：无效用户;
		   -1：服务端异常
		(2)message:返回结果描述
		(3)entity：返回登陆用户的对象UserAccount
			user_id:用户ID
			user_name:用户姓名
			org_no:所属机构
			dep_code:业务部门
			mobile:手机
			email:邮箱
			mobile2:备用联系方式
			status:用户状态
			crt_time:创建时间
			remark:备注
			org_name:组织机构名称
			org_name_1st:备用
			org_name_2nd:备用
			org_name_3rd:备用
			org_name_4th:备用
			ticket:用户登陆成功后，获得的访问会话的ticket，调用功能接口需要填写该参数；【重要】
	********************************************************/    
    @RequestMapping(params = "method=login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setHeader("Content-type", "text/html;charset=UTF-8");  
    	response.setCharacterEncoding("UTF-8");  
    	PrintWriter out = response.getWriter();
    	ResponseProperty<UserAccount> ret = new ResponseProperty<UserAccount>();
    	TicketVO ticket = new TicketVO();
    	try {
    		String mobile = request.getParameter("mobile");
    		String pwd = request.getParameter("pwd");
    		
			if (!StringUtils.isEmpty(mobile)
					&&!StringUtils.isEmpty(pwd)) {
				UserAccount userAccount = apiService.isCodeLogonSuccess(mobile,null);
				//手机号存在
				if (userAccount != null) {
					userAccount = apiService.isCodeLogonSuccess(mobile,pwd);
					if(userAccount != null){
						ticket.setTicket(Ticket.getTicketNumber(userAccount));
						ticket.setAccount(userAccount);
						userAccount.setTicket(ticket.getTicket().toString());
						ret.setStatus(WsConstants.SHT_SUCCESS);
						ret.setMessage("登录成功!");
						ret.setEntity(userAccount);
					}else{
						ret.setStatus(WsConstants.SHT_VALIDATION);
						ret.setMessage("登录失败：密码错误！");
					}
					apiService.insertSetpLogonAsyn(userAccount.getUser_id(), ret.getStatus().toString(), "");
				} else {
					ret.setStatus(WsConstants.SHT_NO_SESSION);
					ret.setMessage("登录失败：无效用户！");
					apiService.insertSetpLogonAsyn(mobile, ret.getStatus().toString(), "");
				}
			} else {
				ret.setStatus(WsConstants.SHT_VALIDATION);
				ret.setMessage("登录失败：用户名或密码不能为空！");
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    		ret = new ResponseProperty(WsConstants.SHT_ERROR, "服务端异常:" + e.toString());
    	}
    	out.write(objectToJsonString(ret));
    	out.close();
    }
    /******************************************************
	1.2	addStepNumber 【添加步行记录】
	输入参数：
		String ticket   	登陆成功时返回的ticket
		String setp_num  	步数
	返回值：json对象
	字段说明：
		(1)status:返回状态；
			1:成功;
			0:失败;
			2：无效用户;
		   -1：服务端异常
		(2)message:返回结果描述
     ********************************************************/    
    @RequestMapping(params = "method=addStepNumber")
    public void addStepNumber(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setHeader("Content-type", "text/html;charset=UTF-8");  
    	response.setCharacterEncoding("UTF-8");  
    	PrintWriter out = response.getWriter();
    	ResponseEmptyProperty ret = new ResponseEmptyProperty();
    	try {
    		String ticket = request.getParameter("ticket"); //登陆信息ticket
    		String setp_num = request.getParameter("setp_num");
            //判断当前ticket
        	UserAccount userAccount = checkTicket(ticket);
        	if(userAccount != null){
        		try {
					Integer step_num_int = Integer.parseInt(setp_num);
					apiService.insertStepNumber(userAccount.getUser_id(), step_num_int.toString(), "", "");
					ret = new ResponseEmptyProperty(WsConstants.SHT_SUCCESS,"成功");
				} catch (Exception e) {
					e.printStackTrace();
					ret = new ResponseEmptyProperty(WsConstants.SHT_VALIDATION,"setp_num参数格式错误：setp_num=" + setp_num);
				}
        	}
        	else{
        		ret = new ResponseEmptyProperty(WsConstants.SHT_NO_SESSION,"无效的ticket:ticket=" + ticket);
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    		ret = new ResponseEmptyProperty(WsConstants.SHT_ERROR, "服务端异常:" + e.toString());
    	}
    	out.write(objectToJsonString(ret));
    	out.close();
    }
    /******************************************************
	1.3	getRankList 【统计排名】
	输入参数：
		String ticket   	登陆成功时返回的ticket
		String rank_flag  	排名标识：1个人排名，2部门排名
		String period_flag  周期标识：1日，2周，3月，4季，5年
		String stat_date  时间：用户选择时间，默认当日,格式如：20160410
	返回值：json对象
	字段说明：
		(1)status:返回状态；
			1:成功;
			0:失败;
			2：无效用户;
		   -1：服务端异常
		(2)message:返回结果描述
		(3)entityList：返回列表
			rank_flag=1时，返回如下字段：
				USER_NAME:姓名
		   		SETP_NUM:步数
		   		RANK1:分行中排名
		   		RANK2:部门中排名
			rank_flag=2时，返回如下字段：
				ORG_NAME:部门
		   		SETP_NUM:人均步数
		   		RANK1:排名
     ********************************************************/    
    @RequestMapping(params = "method=getRankList")
    public void getRankList(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setHeader("Content-type", "text/html;charset=UTF-8");  
    	response.setCharacterEncoding("UTF-8");  
    	PrintWriter out = response.getWriter();
    	ResponsePropertyList<Map> ret = new ResponsePropertyList<Map>();
    	try {
    		String ticket = request.getParameter("ticket"); //登陆信息ticket
    		String rank_flag = request.getParameter("rank_flag");
    		String period_flag = request.getParameter("period_flag");
    		String stat_date = request.getParameter("stat_date");
    		//判断当前ticket
    		UserAccount userAccount = checkTicket(ticket);
    		if(userAccount != null){
    			if("1".equals(rank_flag)){
    				ret.setEntityList(apiService.getUserRankList(period_flag, stat_date,userAccount.getOrg_no()));
    			}else if("2".equals(rank_flag)){
    				ret.setEntityList(apiService.getOrgRankList(period_flag, stat_date,userAccount.getOrg_no()));
    			}
				ret.setStatus(WsConstants.SHT_SUCCESS);
				ret.setMessage("成功");    			
    		}
    		else{
    			ret = new ResponsePropertyList(WsConstants.SHT_NO_SESSION,"无效的ticket:ticket=" + ticket);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		ret = new ResponsePropertyList(WsConstants.SHT_ERROR, "服务端异常:" + e.toString());
    	}
    	out.write(objectToJsonString(ret));
    	out.close();
    }
    /******************************************************
 	1.4	updatePassword 【修改密码】
 	输入参数：
 		String ticket   	登陆成功时返回的ticket
 		String new_pwd  	新密码
 	返回值：json对象
 	字段说明：
 		(1)status:返回状态；
 			1:成功;
 			0:失败;
 			2：无效用户;
 		   -1：服务端异常
 		(2)message:返回结果描述
      ********************************************************/    
     @RequestMapping(params = "method=updatePassword")
     public void updatePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
     	response.setHeader("Content-type", "text/html;charset=UTF-8");  
     	response.setCharacterEncoding("UTF-8");  
     	PrintWriter out = response.getWriter();
     	ResponseEmptyProperty ret = new ResponseEmptyProperty();
     	try {
     		String ticket = request.getParameter("ticket"); //登陆信息ticket
     		String new_pwd = request.getParameter("new_pwd");
             //判断当前ticket
         	UserAccount userAccount = checkTicket(ticket);
         	if(userAccount != null){
				apiService.updatePassword(userAccount.getUser_id(), new_pwd);
				ret = new ResponseEmptyProperty(WsConstants.SHT_SUCCESS,"成功");
         	}
         	else{
         		ret = new ResponseEmptyProperty(WsConstants.SHT_NO_SESSION,"无效的ticket:ticket=" + ticket);
         	}
     	} catch (Exception e) {
     		e.printStackTrace();
     		ret = new ResponseEmptyProperty(WsConstants.SHT_ERROR, "服务端异常:" + e.toString());
     	}
     	out.write(objectToJsonString(ret));
     	out.close();
     }    
    
    /******************************************************
     * 检查ticket是否合法，如果合法，则返回对应的UserAccount，不合法则抛出异常
     ********************************************************/
    public UserAccount checkTicket(String ticket) {
        UserAccount userAccount = null;
        try {
            long ticketNumber = Long.valueOf(ticket);
            userAccount = Ticket.getUserAccount(ticketNumber);
            userAccount.setTicket(ticket);
        } catch (InvalidTicketException e) {
            e.printStackTrace();
        }
        return userAccount;
    }
}
