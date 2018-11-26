package com.loo.hessian;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.caucho.hessian.server.HessianServlet;

@WebListener
public class HessianRegister implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
        try {
			register(sce.getServletContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("-----容器停止后调用（在所有servlet和filter销毁后执行）-------");
	}
	
	private static Set<String> getAllConfigs() throws Exception{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document =  builder.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(HessianConstant.HESSIAN_SERVICE_CONFIG_FILE));
		Element rootElement = document.getDocumentElement();
		NodeList nodeList = rootElement.getElementsByTagName(HessianConstant.HESSIAN_CONFIG_OBJECT);
		int len = nodeList.getLength();
		if(len ==0)
			return null;
		Set<String> set = new HashSet<String>();
		for(int i=0;i<len;i++) {
			Element element = (Element)nodeList.item(i);
			if(element == null
					|| element.getTextContent() ==null
					|| "".equals(element.getTextContent().trim()))
				continue;
			
			set.add(element.getTextContent().trim());
		}
		return set;
	}
	
	private void register(ServletContext sc) throws Exception {
		Set<String> setConf = getAllConfigs();
		if(setConf != null && setConf.size() >0) {
			Iterator<String> it = setConf.iterator();
			while(it.hasNext()) {
				String className = it.next();
				String simpleName = getSimpleName(className);
				ServletRegistration sr = sc.addServlet(simpleName+HessianConstant.HESSION_SERVLET_NAME_SUFFIX, HessianServlet.class); 
		        sr.setInitParameter(HessianConstant.HESSIAN_SERVICE_CLASS, className);
		        sr.addMapping("/hessian/" + simpleName + HessianConstant.HESSIAN_SUFFIX);
			}
		}
	}
	
	private String getSimpleName(String className) {
		String simpleName = className.substring(className.lastIndexOf(".") + 1);
		if(simpleName.endsWith("Impl")) {
			return lowerCase(simpleName.substring(0,simpleName.length()-4));
		}
		return lowerCase(simpleName);
	}
	
	private static String lowerCase(String str) {
		if(str !=null && str.length() >1) {
			char[] cs=str.toCharArray();
	        cs[0]+=32;
	        return String.valueOf(cs);
		}
		return str;
	}
	
}
