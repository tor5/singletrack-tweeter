package org.tobyorourke.tweeter.infrastructure;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.tobyorourke.tweeter.model.Editor;
import org.tobyorourke.tweeter.model.Topic;

import java.net.URL;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class EditorImplTest {

	private URL classified;
	private URL forum;
	private Editor editor;

	@Before
	public void setUp() throws Exception {
		editor = new EditorImpl();
		classified = EditorImplTest.class.getResource("classified.html").toURI().toURL();
		forum = EditorImplTest.class.getResource("forum.html").toURI().toURL();
	}
	@Test
	public void testDisapprovesClassifieds(){
		assertFalse(editor.approve(new Topic("title", classified, "author", DateTime.now())));
	}

	@Test
	public void testApprovesForumPosts(){
		assertTrue(editor.approve(new Topic("title", forum, "author", DateTime.now())));
	}
}
