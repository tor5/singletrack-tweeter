package org.tobyorourke.tweeter.infrastructure;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.tobyorourke.tweeter.model.LinkSquasher;
import org.tobyorourke.tweeter.model.Result;
import org.tobyorourke.tweeter.model.Topic;
import twitter4j.Twitter;

import java.net.URL;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TweeterImplTest {

	@Mock
	private LinkSquasher linkSquasher;
	@Mock
	private Twitter twitter;

	private TweeterImpl tweeter;


	@Test
	public void testSendTweet() throws Exception {
		tweeter = new TweeterImpl(linkSquasher, twitter);
		Result result = tweeter.post(new Topic("test", new URL("http://example.com"),"author", DateTime.now()));
		assertEquals(Result.OK, result);
	}
}
