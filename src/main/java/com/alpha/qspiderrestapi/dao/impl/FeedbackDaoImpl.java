package com.alpha.qspiderrestapi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.FeedbackDao;
import com.alpha.qspiderrestapi.entity.FeedBack;
import com.alpha.qspiderrestapi.repository.FeedBackRepository;

@Repository
public class FeedbackDaoImpl implements FeedbackDao{
	@Autowired
	private FeedBackRepository feedBackRepository ;
	
	@Override
	public FeedBack saveFeedback(FeedBack feedBack) {
		return feedBackRepository.save(feedBack);
	}

}
