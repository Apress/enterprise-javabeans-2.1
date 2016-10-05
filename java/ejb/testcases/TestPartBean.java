package ejb.testcases;

import ejb.part.*;
import ejb.test.*;
import javax.ejb.*;

public class TestPartBean extends EJBTestCase {
    
    PartHome partHome;
    Part p1;
    Part p2;
    Part p3;


    public TestPartBean() {
        super();
    }


    public void prepareTest()
        throws Exception
    {
        partHome = (PartHome)narrow("Part", PartHome.class);
    }


    public void testCreate()
        throws Exception
    {
        final String one = "11111";
        p1 = partHome.create(one);
        assertNotNull("Part", p1);
    }


    public void testDelete()
        throws Exception
    {
        final String two = "22222";
        p2 = partHome.create(two);
        assertNotNull("Part", p2);
        p2.remove();
        try {
            p2 = partHome.findByPrimaryKey(two);
            fail("expected FinderException, part " + two + " should not exist");
        } catch(FinderException fex) {
            //expected
        }
    }


    public void testUpdate()
        throws Exception
    {
        final String three = "33333";
        p3 = partHome.create(three);
        assertNotNull("Part", p3);
        TsPartDetails pd = p3.getPartDetails();
        pd.setPartDescription("Test Part");
        pd.setSupplierName("Test Supplier");
        pd.setPrice(120);
        TsPartDetails pd1 = p3.setPartDetails(pd);
        assertEquals(pd.getPartNumber(), pd1.getPartNumber());
        assertEquals(pd.getPartDescritption(), pd1.getPartDescritption());
        assertEquals(pd.getSupplierName(), pd1.getSupplierName());
        assertEquals(pd.getPrice(), pd1.getPrice());
        try {
            p3.setPartDetails(pd);
            fail("expected OutOfDateException");
        } catch(OutOfDateException ex) {
            //expected
        }
    }


    public void finalizeTest()
        throws Exception
    {
        p1.remove();
        p3.remove();
    }

}
