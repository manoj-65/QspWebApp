package com.alpha.qspiderrestapi.daoImpl;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alpha.qspiderrestapi.dao.WeightageDao;
import com.alpha.qspiderrestapi.entity.Weightage;

@SpringBootTest
public class WeightageDaoImplTest {
	
	@Autowired
	private WeightageDao weightageDao;
	
	@Test
	public void testIsEntityPresent() {	
		List<Weightage> weightages = weightageDao.findSubCategoryWeightages(2);
		System.err.println(weightages);
		weightages.forEach(w->System.err.println(w.getQspiders()+", "+w.getSubCategory().getSubCategoryTitle()));
	}
}
