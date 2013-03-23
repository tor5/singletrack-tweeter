package org.tobyorourke.tweeter.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Publisher {

	private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
	private final Tweeter tweeter;
	private final IssueChecker checker;
	private final Editor editor;
	private Check check;

	public Publisher(Tweeter tweeter, IssueChecker checker, Editor editor) {
		this.tweeter = tweeter;
		this.checker = checker;
		this.editor = editor;
		this.check = checker.getLastPublished();
	}


	private Long getLastPublishTime() {
		return check.getLastCheck();
	}


	public Map<Topic, Result> publish(Set<Topic> topics){
		Set<Topic> toPublish = excludeAlreadySeenMessages(topics);
		Map<Topic, Result> results = new HashMap<Topic, Result>();
		for (Topic topic : toPublish) {
			Result result = tweeter.post(topic);
			results.put(topic, result);
		}
		checker.publishNewIssue(check);
		return results;
	}

	private Set<Topic> excludeAlreadySeenMessages(Set<Topic> topics) {
		return Sets.filter(topics, new Predicate<Topic>() {
			@Override
			public boolean apply(Topic topic) {
				return topic.getCreated().isAfter(getLastPublishTime()) && editor.approve(topic);
			}
		});
	}
}
