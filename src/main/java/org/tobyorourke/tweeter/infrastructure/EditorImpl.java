package org.tobyorourke.tweeter.infrastructure;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tobyorourke.tweeter.model.Editor;
import org.tobyorourke.tweeter.model.Topic;

import java.io.IOException;

public class EditorImpl implements Editor {
	private static final Logger logger = LoggerFactory.getLogger(EditorImpl.class);

	@Override
	public Boolean approve(Topic topic) {
		logger.debug("Deciding whether to approve topic");
		try {
			Document doc = Jsoup.parse(topic.getLink().openStream(), "UTF8", topic.getLink().toString());
			boolean approved = !doc.getElementsByTag("h3").html().contains("Classifieds");
			logger.debug("topic " + (approved ? "approved" : "rejected"));
			return approved;
		} catch (IOException e) {
			logger.debug("problem checking for ads");
			return false;
		}
	}
}
