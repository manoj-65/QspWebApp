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

	@Value("${organization.prosp}")
	private String bspDomainName;

	public Organization checkOrganizationType(String hostName) {

		if (qspDomainName.equals(hostName) || hostName.contains("http://localhost")) {
			return Organization.QSP;
		} else if (jspDomainName.equals(hostName) || hostName.contains("http://localhost")) {
			return Organization.JSP;
		} else if (pyspDomainName.equals(hostName) || hostName.contains("http://localhost")) {
			return Organization.PYSP;
		} else if (bspDomainName.equals(hostName) || hostName.contains("http://localhost")) {
			return Organization.PROSP;
		}
		throw new DomainMismatchException("Domain name is not matching any Organisation Type");
	}

}
