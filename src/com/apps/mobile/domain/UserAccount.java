package com.apps.mobile.domain;

public class UserAccount {
	private String user_id;//用户ID
	private String user_name;//用户姓名
	private String org_no;//所属机构
	private String dep_code;//业务部门
	private String mobile;//手机
	private String email;//邮箱
	private String mobile2;//备用联系方式
	private String status;//用户状态
	private String crt_time;//创建时间
	private String remark;//备注
	
	private String org_name;//组织机构名称
	private String org_name_1st;//备用
	private String org_name_2nd;//备用
	private String org_name_3rd;//备用
	private String org_name_4th;//备用
	
	private String ticket;		//用户登陆成功后，获得的访问会话的ticket，调用功能接口需要填写该参数；【重要】
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getOrg_no() {
		return org_no;
	}
	public void setOrg_no(String org_no) {
		this.org_no = org_no;
	}
	public String getDep_code() {
		return dep_code;
	}
	public void setDep_code(String dep_code) {
		this.dep_code = dep_code;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile2() {
		return mobile2;
	}
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCrt_time() {
		return crt_time;
	}
	public void setCrt_time(String crt_time) {
		this.crt_time = crt_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public String getOrg_name_1st() {
		return org_name_1st;
	}
	public void setOrg_name_1st(String org_name_1st) {
		this.org_name_1st = org_name_1st;
	}
	public String getOrg_name_2nd() {
		return org_name_2nd;
	}
	public void setOrg_name_2nd(String org_name_2nd) {
		this.org_name_2nd = org_name_2nd;
	}
	public String getOrg_name_3rd() {
		return org_name_3rd;
	}
	public void setOrg_name_3rd(String org_name_3rd) {
		this.org_name_3rd = org_name_3rd;
	}
	public String getOrg_name_4th() {
		return org_name_4th;
	}
	public void setOrg_name_4th(String org_name_4th) {
		this.org_name_4th = org_name_4th;
	}
}
