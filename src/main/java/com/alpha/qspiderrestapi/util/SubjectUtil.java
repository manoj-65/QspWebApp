package com.alpha.qspiderrestapi.util;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class SubjectUtil {

	public <T> long getModuleCount(List<T> incomingList) {
		return incomingList.size();
	}

	public <T> double getModuleDuration(List<T> incomingList, Function<T, Double> mapper) {
		return incomingList.stream().map(mapper).reduce((double) 0, Double::sum);
	}

}
