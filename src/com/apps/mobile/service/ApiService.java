package com.apps.mobile.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apps.common.dao.TaskDao;
import com.apps.mobile.domain.UserAccount;

@Service
public class ApiService {
	@Autowired
	private TaskDao taskDao;
	
	@SuppressWarnings("unchecked")
	public UserAccount isCodeLogonSuccess(String mobile, String pwd) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("pwd", pwd);
		ArrayList<UserAccount> list = (ArrayList<UserAccount>) taskDao.getSqlMapClientTemplate().queryForList("api.isCodeLogonSuccess", params);
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	public void insertSetpLogon(String user_id, String result,String remark) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user_id", user_id);
		params.put("result", result);
		params.put("remark", remark);
		taskDao.insert("api.insertSetpLogon", params);
	}
	public void insertStepNumber(String user_id, String setp_num,String remark1,String remark2) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user_id", user_id);
		params.put("setp_num", setp_num);
		params.put("remark1", remark1);
		params.put("remark2", remark2);
		taskDao.insert("api.insertStepNumber", params);
	}
	
    public List<Map> getUserRankList(String period_flag,String stat_date) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("period_flag", period_flag);
		params.put("stat_date", stat_date);
        return taskDao.getSqlMapClientTemplate().queryForList("api.getUserRankList", params);
    }
    
    public List<Map> getOrgRankList(String period_flag,String stat_date) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("period_flag", period_flag);
    	params.put("stat_date", stat_date);
    	return taskDao.getSqlMapClientTemplate().queryForList("api.getOrgRankList", params);
    }
	
	public void insertSetpLogonAsyn(String user_id, String result,String remark) {
		new InsertSetpLogonThread(user_id, result,remark).start();
	}
	
	/* 异步执行-记录登陆日志 */
	class InsertSetpLogonThread extends Thread {

		private String user_id;
		private String result;
		private String remark;
		public InsertSetpLogonThread() {
		}

		public InsertSetpLogonThread(String user_id, String result,String remark) {
			this.user_id = user_id;
			this.result = result;
			this.remark = remark;
		}

		public void run() {
			insertSetpLogon(user_id, result, remark);
		}

	}	
}