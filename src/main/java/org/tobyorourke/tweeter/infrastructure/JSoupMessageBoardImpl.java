package org.tobyorourke.tweeter.infrastructure;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tobyorourke.tweeter.model.MessageBoard;
import org.tobyorourke.tweeter.model.Topic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

public class JSoupMessageBoardImpl implements MessageBoard {
	private static final Logger logger = LoggerFactory.getLogger(JSoupMessageBoardImpl.class);
	private final URL url;
	private final DateTimeFormatter formatter;

	public JSoupMessageBoardImpl(URL url) {
		this.formatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss ZZ");
		this.url = url;
	}

	@Override
	public Set<Topic> getTopics() {
		try {

			Document doc = parseDocument();
			return parseTopics(doc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private Set<Topic> parseTopics(Document doc) throws Exception {
		Set<Topic> topics = new HashSet<Topic>();
		long start = System.currentTimeMillis();
		Elements elements = doc.getElementsByTag("item");

		for (Element element : elements) {
			String title = cleanTitle(element.getElementsByTag("title").html());
			String author = element.getElementsByTag("dc:creator").html();
			DateTime created =  formatter.parseDateTime(element.getElementsByTag("pubDate").html());
			URL link =  new URL(element.getElementsByTag("link").html());

			topics.add(new Topic(title, link, author, created));
		}
		long finish = System.currentTimeMillis();
		logger.debug("creating "+topics.size()+" topics took "+(finish-start)+"ms");
		return topics;
	}

	private Document parseDocument() throws IOException {
		long start = System.currentTimeMillis();
		Document doc = Jsoup.parse(url.openStream(), "UTF8", url.toString(), Parser.xmlParser());
		long finish = System.currentTimeMillis();
		logger.debug("parsing feed took "+(finish-start)+"ms");
		return doc;
	}

	private String cleanTitle(String rawTitle){
		rawTitle = StringEscapeUtils.unescapeHtml4(rawTitle);
		if (rawTitle.contains("\"")) {
			return rawTitle.substring(rawTitle.indexOf("\"") + 1, rawTitle.length() - 1);
		} else {
			return rawTitle;
		}
	}
}
