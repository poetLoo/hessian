package com.king.loo;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.loo.facade.CompanyService;
import com.loo.facade.UserService;

public class HesianClientTests {

	HessianProxyFactory factory = null;
	static final String HESSIAN_URL = "http://localhost:8888/hessionServices/hessian/";
	@Before
	public void before() {
		factory = new HessianProxyFactory();
		factory.setOverloadEnabled(true);
	}
	
	@Test
	public void testUser() {
		try {
			String url = HESSIAN_URL+"userService.hs";
			UserService userService = (UserService) factory.create(UserService.class, url);
			userService.addUser("JioJio");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCompany() {
		try {
			String url2 = HESSIAN_URL + "companyService.hs";
			CompanyService companyService = (CompanyService) factory.create(CompanyService.class, url2);
			List<String> list = companyService.findAllCompanyName();
			for(String str:list)
				System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
