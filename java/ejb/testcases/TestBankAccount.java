package ejb.testcases;

import ejb.bankaccount.*;
import ejb.test.*;
import javax.ejb.*;

public class TestBankAccount extends EJBTestCase {

    private BankAccountHome bankHome;
    private BankAccount ba1;
    private BankAccount ba2;
    private BankAccount ba3;

    public TestBankAccount() {
        super();
    }


    public void prepareTest()
        throws Exception
    {
        bankHome = (BankAccountHome)narrow("BankAccount", BankAccountHome.class);
    }


    public void testCreate()
        throws Exception
    {
        final String one = "111";
        ba1 = bankHome.create(one, "TestAccount", 0);
        assertNotNull("BankAccount", ba1);
    }


    public void testDelete()
        throws Exception
    {
        final String two = "222";
        ba2 = bankHome.create(two, "TestAccount", 0);
        assertNotNull("BankAccount", ba2);
        ba2.remove();
        try {
            bankHome.findByPrimaryKey(two);
            fail("expected FinderException, " + two + " should not exist");
        } catch(FinderException fex) {
            //expected
        }
    }


    public void testUpdate()
        throws Exception
    {
        final String three = "333";
        ba3 = bankHome.create(three, "TestAccount", 0);
        assertNotNull("BankAccount", ba3);
        float balance = ba3.getBalance();
        assertEquals(balance, 0);
        ba3.increaseBalance(100);
        balance = ba3.getBalance();
        assertEquals(balance, 100);
        ba3.decreaseBalance(50);
        balance = ba3.getBalance();
        assertEquals(balance, 50);
    }


    public void finalizeTest()
        throws Exception
    {
        ba1.remove();
        ba3.remove();
    }

}
