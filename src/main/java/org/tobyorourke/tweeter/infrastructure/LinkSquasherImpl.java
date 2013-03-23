package org.tobyorourke.tweeter.infrastructure;

import com.rosaloves.bitlyj.Bitly;
import org.tobyorourke.tweeter.model.LinkSquasher;

import java.net.URL;

import static com.rosaloves.bitlyj.Bitly.shorten;

public class LinkSquasherImpl implements LinkSquasher{

	private Bitly.Provider provider;

	public LinkSquasherImpl(Bitly.Provider provider) {
		this.provider = provider;
	}

	@Override
	public String squash(URL longURL) {
		return provider.call(shorten(longURL.toString())).getShortUrl();
	}

}
