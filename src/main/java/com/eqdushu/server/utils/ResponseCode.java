package com.eqdushu.server.utils;

public enum ResponseCode {
	
	success("成功"), 
	unknown_user("用户不存在"),
	unknown_error("未知错误"),
	existed_user("该用户已存在"),
	user_need_audit("该用户尚未通过审核"),
	incorrect_password("密码错误"),
	abnormal_account("账号状态异常"),
	operate_failed("操作失败，请稍后重试"),
	incorrect_credentials("手机号或密码不正确"), 
	excessive_attempts("超过最大尝试次数"), 
	user_notfound("未发现该用户"),
	invalid_attribute("输入参数有误"),
	unauthorized("用户未登录"),
	need_admin("需要管理员权限"),
	need_reader("需要公司正式成员权限"),
	duplicate_company_create("请勿重复创建公司"),
	duplicate_company_code("公司编码重复，请重新设置"),
	company_code_error("公司编码不存在，请重新填写"),
	company_admin_lost("你想让公司没有管理员了么"),

	create_book_error("新增书籍失败"),
	book_not_exit("该书籍不存在"),
	borrow_jrl_error("借阅流水登记失败"),
	add_booknum_error("增加书籍数量失败"),
	upd_borrowsts_error("修改借阅状态失败"),
	book_error("借阅书籍失败"),
	inbook_error("书籍已存在"),
	updjrl_error("修改流水失败"),

	sendsms_error("发送短信异常"),
	smserror_frequent("请不要频繁发送短信"),
	verifysms_error("短信校验错误");

	private String msg;

	private ResponseCode(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
