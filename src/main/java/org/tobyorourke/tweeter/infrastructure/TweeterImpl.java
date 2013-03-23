package org.tobyorourke.tweeter.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tobyorourke.tweeter.model.LinkSquasher;
import org.tobyorourke.tweeter.model.Result;
import org.tobyorourke.tweeter.model.Topic;
import org.tobyorourke.tweeter.model.Tweeter;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TweeterImpl implements Tweeter {

	private static final Logger logger = LoggerFactory.getLogger(TweeterImpl.class);
	private final Twitter twitter;
	private final LinkSquasher squasher;

	public TweeterImpl(LinkSquasher squasher, Twitter twitter) {
		this.squasher = squasher;
		this.twitter = twitter;
	}

	@Override
	public Result post(Topic topic) {
		try {
			long start = System.currentTimeMillis();
			twitter.updateStatus(new StatusUpdate(topic.getTitle() + " " + squasher.squash(topic.getLink())));
			long finish = System.currentTimeMillis();
			logger.debug("tweeting: topic = [" + topic + "] took "+(finish-start)+"ms");
		} catch (TwitterException e) {
			return Result.FAIL;
		}
		return Result.OK;
	}
}
