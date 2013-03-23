package org.tobyorourke.tweeter.model;

import lombok.Data;
import lombok.ToString;
import org.joda.time.DateTime;

import java.net.URL;

@Data
@ToString
public class Topic {

	private final String title;
	private final URL link;
	private final String author;
	private final DateTime created;
}
