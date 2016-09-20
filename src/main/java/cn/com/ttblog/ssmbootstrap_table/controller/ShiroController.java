package cn.com.ttblog.ssmbootstrap_table.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * shiro方法测试 
 */
@RequestMapping("/shiro")
@Controller
public class ShiroController {

	private static final Logger LOG = LoggerFactory.getLogger(ShiroController.class);
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	@ResponseBody
	public Object index(){
		LOG.debug("shiro-index");
		return "shiro方法测试";
	}
	
	@RequestMapping(value="/isauth",method=RequestMethod.GET)
	@ResponseBody
	public Object isauth(){
		Subject subject=SecurityUtils.getSubject();
		LOG.debug("subject.isAuthenticated():{}",subject.isAuthenticated());
		return subject.isAuthenticated();
	}
	
	@RequestMapping(value="/getPrincipal",method=RequestMethod.GET)
	@ResponseBody
	public Object getPrincipal(){
		Subject subject=SecurityUtils.getSubject();
		Object p=subject.getPrincipal();
		LOG.debug("getPrincipal:{}",p);
//		Subject subject=SecurityUtils.getSubject();
		return p;
	}
	
	@RequestMapping(value="/hasRole/{role}",method=RequestMethod.GET)
	@ResponseBody
	public Object hasRole(@PathVariable("role") String role){
		Subject subject=SecurityUtils.getSubject();
		LOG.debug("subject.hasRole({}):{}",role,subject.hasRole(role));
		return subject.hasRole(role);
	}
	

	@RequestMapping(value="/ispermit/{resource}",method=RequestMethod.GET)
	@ResponseBody
	public Object hasPermit(@PathVariable("resource") String resource){
		Subject subject=SecurityUtils.getSubject();
		LOG.debug("subject.isPermitted({}):{}",resource,subject.isPermitted(resource));
		return subject.isPermitted(resource);
	}
	
	@RequestMapping(value="/getSession",method=RequestMethod.GET)
	@ResponseBody
	public Object getSession(){
		Subject subject=SecurityUtils.getSubject();
		LOG.debug("subject.getSession():{}",subject.getSession());
		return subject.getSession();
	}
	
	@RequestMapping(value="/getSession/{attr}",method=RequestMethod.GET)
	@ResponseBody
	public Object getSessionAttr(@PathVariable("attr") String attr){
		Subject subject=SecurityUtils.getSubject();
		Session session=subject.getSession();
		LOG.debug("session.getAttribute({}):{}",attr,session.getAttribute(attr));
		return session.getAttribute(attr);
	}
	
	@RequestMapping(value = "/session/{key}")
	@ResponseBody
	public JSONObject getSessionAttr(@PathVariable("key") String key,HttpServletRequest request) {
		JSONObject j=new JSONObject();
		j.put("shiro-"+key, SecurityUtils.getSubject().getSession().getAttribute(key));
		j.put("shiro-id", SecurityUtils.getSubject().getSession().getId());
		j.put("httpsession-"+key, request.getSession().getAttribute(key));
		j.put("httpsession-id", request.getSession().getId());
		//request拿到的session和shiro拿到的是一个，request被shiro处理过了
		return j;
	}
	
	@RequestMapping(value = "/session/{key}/{val}")
	@ResponseBody
	public boolean getSessionAttr(@PathVariable("key") String key,@PathVariable("val")String val,HttpServletRequest request) {
		request.getSession().setAttribute(key, val);
		return true;
	}
}