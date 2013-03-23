package org.tobyorourke.tweeter.infrastructure;

import org.junit.Test;
import org.tobyorourke.tweeter.model.MessageBoard;
import org.tobyorourke.tweeter.model.Topic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

public class JSoupMessageBoardImplTest {

	private static final URI RSS_FEED = new File("/Users/toby.orourke/Source/singletrack-tweeter/src/test/resources/org/tobyorourke/tweeter/infrastructure/rss.xml").toURI();

	@Test
	public void testParsesDocument() throws MalformedURLException {
		MessageBoard messageBoard = new JSoupMessageBoardImpl(RSS_FEED.toURL());

		Set<Topic> topics = messageBoard.getTopics();

		assertEquals(20, topics.size());
	}
}
