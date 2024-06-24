package com.alpha.qspiderrestapi.service.impl;

import com.alpha.qspiderrestapi.entity.FeedBack;
import com.alpha.qspiderrestapi.service.EmailData;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FeedbackEmailData implements EmailData {

	private final FeedBack feedback;

	@Override
	public String getSubject() {
		return "Feedback Received - " + feedback.getFeedBackId();
	}

	@Override
	public String getContent() {
		StringBuilder content = new StringBuilder();
		content.append("Feedback ID: ").append(feedback.getFeedBackId()).append("\n");
		content.append(feedback.toString());
		return content.toString();
	}
}
