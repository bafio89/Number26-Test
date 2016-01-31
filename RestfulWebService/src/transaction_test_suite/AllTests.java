package transaction_test_suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/*
 * Author: Fabio Fiorella
 * 
 * Test all test case
 */
@RunWith(Suite.class)
@SuiteClasses({GetWithNoTransactionsTest.class,
			   InsertTransactionsTest.class,
			   GetWithTransactionStoredTest.class
			  })
public class AllTests {
	
	
}
