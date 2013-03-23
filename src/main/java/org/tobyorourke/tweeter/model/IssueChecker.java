package org.tobyorourke.tweeter.model;

public interface IssueChecker {

	Check getLastPublished();

	void publishNewIssue(Check check);
}
