package org.tobyorourke.tweeter.app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.lightcouch.CouchDbClient;
import org.tobyorourke.tweeter.infrastructure.*;
import org.tobyorourke.tweeter.model.*;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import static com.rosaloves.bitlyj.Bitly.as;

public class STWTweeter {

	private static String bitlyUserName;
	private static String bitlyAPIKey;
	private static String twitterConsumerKey;
	private static String twitterConsumerSecret;
	private static String twitterAccessToken;
	private static String twitterAccessTokenSecret;
	private static String couchName;
	private static boolean couchAutoCreate;
	private static String couchProtocol;
	private static String couchHost;
	private static int couchPort;
	private static String couchUserName;
	private static String couchPassword;

	public static void main(String[] args) throws Exception {
		parseArguements(findPayload(args));

		// just about simple enough not to bother with DI
		MessageBoard messageBoard = new JSoupMessageBoardImpl(new URL("http://singletrackworld.com/forum/rss/topics"));
		IssueChecker checker = new IssueCheckerImpl(buildCouchClient());
		Tweeter tweeter = new TweeterImpl(buildLinkSquasher(), setupTwitter());
		Editor editor = new EditorImpl();
		Publisher publisher = new Publisher(tweeter, checker, editor);

		publisher.publish(messageBoard.getTopics());

		System.exit(0);

	}

	private static String findPayload(String[] programArgs) {
		for (int i = 0; i < programArgs.length; i++) {
			if (programArgs[i].equals("-payload")) {
				return programArgs[i + 1];

			}
		}
		throw new RuntimeException("No payload available.");
	}

	private static void parseArguements(String arg) throws IOException {
		String payload = readFile(arg);
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject configuration = parser.parse(payload).getAsJsonObject();

		bitlyUserName = gson.fromJson(configuration.get("bitly_user_name"), String.class);
		bitlyAPIKey = gson.fromJson(configuration.get("bitly_api_key"), String.class);
		twitterConsumerKey = gson.fromJson(configuration.get("twitter_consumer_key"), String.class);
		twitterConsumerSecret = gson.fromJson(configuration.get("twitter_consumer_secret"), String.class);
		twitterAccessToken = gson.fromJson(configuration.get("twitter_access_token"), String.class);
		twitterAccessTokenSecret = gson.fromJson(configuration.get("twitter_access_token_secret"), String.class);
		couchName = gson.fromJson(configuration.get("couch_name"), String.class);
		couchAutoCreate = gson.fromJson(configuration.get("couch_auto_create"), Boolean.class);
		couchProtocol = gson.fromJson(configuration.get("couch_protocol"), String.class);
		couchHost = gson.fromJson(configuration.get("couch_host"), String.class);
		couchPort = gson.fromJson(configuration.get("couch_port"), Integer.class);
		couchUserName = gson.fromJson(configuration.get("couch_user_name"), String.class);
		couchPassword = gson.fromJson(configuration.get("couch_password"), String.class);
	}

	private static String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel chan = stream.getChannel();
			MappedByteBuffer buf = chan.map(FileChannel.MapMode.READ_ONLY, 0, chan.size());
			return Charset.defaultCharset().decode(buf).toString();
		} finally {
			stream.close();
		}
	}

	private static CouchDbClient buildCouchClient() {
		return new CouchDbClient(couchName, couchAutoCreate, couchProtocol, couchHost, couchPort, couchUserName, couchPassword);
	}


	private static LinkSquasher buildLinkSquasher() {
		return new LinkSquasherImpl(as(bitlyUserName, bitlyAPIKey));
	}

	private static Twitter setupTwitter() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(twitterConsumerKey)
				.setOAuthConsumerSecret(twitterConsumerSecret)
				.setOAuthAccessToken(twitterAccessToken)
				.setOAuthAccessTokenSecret(twitterAccessTokenSecret)
				.setUseSSL(true);
		TwitterFactory tf = new TwitterFactory(cb.build());
		return tf.getInstance();
	}


}

