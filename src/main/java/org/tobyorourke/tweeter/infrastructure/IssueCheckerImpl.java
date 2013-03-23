package org.tobyorourke.tweeter.infrastructure;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.tobyorourke.tweeter.model.Check;
import org.tobyorourke.tweeter.model.IssueChecker;

public class IssueCheckerImpl implements IssueChecker {

	private final CouchDbClient dbClient;
	public static final String LAST_CHECK = "last-check";

	public IssueCheckerImpl(CouchDbClient dbClient) {
		this.dbClient = dbClient;
	}

	@Override
	public Check getLastPublished() {
		try {
			return dbClient.find(Check.class, LAST_CHECK);
		} catch (NoDocumentException e){
			return new Check(System.currentTimeMillis(), LAST_CHECK, null);
		}
	}

	@Override
	public void publishNewIssue(Check check) {
		if(check.get_rev() != null){
			check.setLastCheck(System.currentTimeMillis());
			dbClient.update(check);
		} else {
			dbClient.save(check);
		}
	}
}
