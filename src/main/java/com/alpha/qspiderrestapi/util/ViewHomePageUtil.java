package com.alpha.qspiderrestapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.exception.DomainMismatchException;

@Component
public class ViewHomePageUtil {

	@Value("${organization.qsp}")
	private String qspDomainName;

	@Value("${organization.jsp}")
	private String jspDomainName;

	@Value("${organization.pysp}")
	private String pyspDomainName;

	@Value("${organization.bsp}")
	private String bspDomainName;

	public Organization checkOrganizationType(String hostName) {

		if (qspDomainName.equals(hostName) || qspDomainName.contains("http://localhost")) {
			return Organization.QSP;
		} else if (jspDomainName.equals(hostName) || jspDomainName.contains("http://localhost")) {
			return Organization.JSP;
		} else if (pyspDomainName.equals(hostName) || pyspDomainName.contains("http://localhost")) {
			return Organization.PYSP;
		} else if (bspDomainName.equals(hostName) || bspDomainName.contains("http://localhost")) {
			return Organization.BSP;
		}
		throw new DomainMismatchException("Domain name is not matching any Organisation Type");
	}

}