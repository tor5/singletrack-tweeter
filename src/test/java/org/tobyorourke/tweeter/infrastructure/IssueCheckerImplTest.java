package org.tobyorourke.tweeter.infrastructure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightcouch.CouchDbClient;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.tobyorourke.tweeter.model.Check;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith (PowerMockRunner.class)
@PrepareForTest( { CouchDbClient.class })
public class IssueCheckerImplTest {

	@Mock
	private CouchDbClient dbClient;
	private IssueCheckerImpl issueChecker;
	private long testTime;

	@Before
	public void setup(){
		issueChecker = new IssueCheckerImpl(dbClient);
		testTime = System.currentTimeMillis();
	}

	@Test
	public void testCanReadLastCheck(){

		when(dbClient.find(Check.class, IssueCheckerImpl.LAST_CHECK)).thenReturn(new Check(testTime, "id", "revision"));

		Check check = issueChecker.getLastPublished();

		assertEquals(testTime, check.getLastCheck());
	}

	@Test
	public void testSaveFirstEverCheck(){
		Check check = new Check(testTime, null, null);
		issueChecker.publishNewIssue(check);

		verify(dbClient).save(check);
	}


	@Test
	public void testSaveSubsequentChecks(){
		Check check = new Check(testTime, "id", "revision");
		issueChecker.publishNewIssue(check);

		verify(dbClient).update(check);
	}

}
