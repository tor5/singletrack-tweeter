package org.tobyorourke.tweeter.model;


import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;
import java.util.Collections;
import java.util.Set;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PublisherTest {

	public URL classifiedUrl;
	@Mock
	private Tweeter tweeter;
	@Mock
	private IssueChecker checker;
	@Mock
	private Editor editor;
	private Publisher publisher;

	private static final long FIFTEEN_SECONDS = 15 * 1000L;
	public static final long TEN_SECONDS = 10000L;

	@Before
	public void setup() throws Exception {
		classifiedUrl = new URL("http://singletrackworld.com/forum/topic/fs-berghaus-gore-tex-pro-shell-smock-xl-2");
		when(checker.getLastPublished()).thenReturn(new Check(System.currentTimeMillis()- TEN_SECONDS, "last-check", null));
		publisher = new Publisher(tweeter, checker, editor);
	}

	@Test
	public void testCopesWithNoTopics(){
		publisher.publish(Collections.EMPTY_SET);
		verify(tweeter, times(0)).post(isA(Topic.class));

	}

	@Test
	public void testSendsSingleMessage(){

		Topic topic = new Topic("title", null, "author", DateTime.now());
		when(editor.approve(topic)).thenReturn(true);
		Set<Topic> topics = Sets.newHashSet(topic);
		publisher.publish(topics);

		verify(tweeter, times(1)).post(topic);

	}

	@Test
	public void testSendsManyMessages(){

		Topic topic = new Topic("title", null, "author", DateTime.now());
		Topic topic2 = new Topic("title2", null, "author", DateTime.now());
		when(editor.approve(isA(Topic.class))).thenReturn(true);
		Set<Topic> topics = Sets.newHashSet(topic, topic2);
		publisher.publish(topics);

		verify(tweeter, times(2)).post(isA(Topic.class));

	}

	@Test
	public void testDoesNotSendOldMessages(){

		Topic topic = new Topic("title", null, "author", DateTime.now());
		Topic topic2 = new Topic("title2", null, "author", DateTime.now().minus(FIFTEEN_SECONDS));
		when(editor.approve(isA(Topic.class))).thenReturn(true);
		Set<Topic> topics = Sets.newHashSet(topic, topic2);
		publisher.publish(topics);

		verify(tweeter, times(1)).post(isA(Topic.class));

	}

	@Test
	public void testExcludesClassifieds(){
		Topic topic = new Topic("title", classifiedUrl, "author", DateTime.now());
		when(editor.approve(topic)).thenReturn(false);
		Set<Topic> topics = Sets.newHashSet(topic);
		publisher.publish(topics);
		verify(tweeter, times(0)).post(isA(Topic.class));
	}



}
