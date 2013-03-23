package org.tobyorourke.tweeter.infrastructure;

import com.rosaloves.bitlyj.Bitly;
import com.rosaloves.bitlyj.BitlyMethod;
import com.rosaloves.bitlyj.ShortenedUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LinkSquasherImplTest {

	@Mock
	private Bitly.Provider provider;
	@Mock
	private ShortenedUrl shortUrl;

	private LinkSquasherImpl linkSquasher;

	@Test
	public void testCreatesShortLink() throws Exception{
		linkSquasher = new LinkSquasherImpl(provider);
		when(provider.call(isA(BitlyMethod.class))).thenReturn(shortUrl);
		when(shortUrl.getShortUrl()).thenReturn("http://bit.ly/11tL38U");
		String shortUrl = linkSquasher.squash(new URL("http://www.example.com/this/is/a/really/long/url/that/needs/to/be/squashed"));
		assertTrue(shortUrl.length() <= 21);

	}
}
