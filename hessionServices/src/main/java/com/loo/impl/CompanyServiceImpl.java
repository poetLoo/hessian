package com.loo.impl;

import java.util.Arrays;
import java.util.List;

import com.loo.facade.CompanyService;

public class CompanyServiceImpl implements CompanyService {

	@Override
	public List<String> findAllCompanyName() {
		return Arrays.asList("Microsoft", "Apple", "华为");
	}

}
