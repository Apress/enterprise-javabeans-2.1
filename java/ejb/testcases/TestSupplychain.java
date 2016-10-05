package ejb.testcases;

import ejb.supplychain.stock.*;
import ejb.supplychain.producer.*;
import ejb.test.*;

import javax.ejb.CreateException;
import javax.transaction.UserTransaction;


public class TestSupplychain extends EJBTestCase {

    private StockHome    stockHome;
    private ProducerHome producerHome;
    

    public TestSupplychain() {
        super();
    }


    public void prepareTest()
        throws Exception
    {
        stockHome    = (StockHome)narrow("Stock", StockHome.class);
        producerHome = (ProducerHome)narrow("Producer", ProducerHome.class);
    }


    public void testCreateFindRemove_Stock()
        throws Exception
    {
       try {
           this.stockHome.create(null, 100, 0);
           this.fail("create(null, 100, 0): expected CreateException");
       } catch(CreateException ex) {
           //as expected
       }
       try {
           this.stockHome.create("stock1", 100, 101);
           this.fail("create('stock1', 100, 101): expected CreateException");
       } catch(CreateException ex) {
           //as expected
       }
       try {
           this.stockHome.create("stock1", 100, -1);
           this.fail("create('stock1', 100, -1): expected CreateException");
       } catch(CreateException ex) {
           //as expected
       }
       Stock stock = this.stockHome.create("stock", 100, 0);
       this.assertNotNull("stock after create", stock);
       Stock s = this.stockHome.findByPrimaryKey("stock");
       this.assertNotNull("stock after find", s);
       stock.remove();
    }


    public void testGet_Stock()
        throws Exception
    {
        Stock stock = this.stockHome.create("stock-get", 100, 100);
        this.assertNotNull("stock after create", stock);
        this.assertEquals("stock-volume", 100, stock.getVolume());
        stock.get(10);
        stock.get(10);
        stock.get(10);
        stock.get(10);
        stock.get(10);
        this.assertEquals("stock-volume", 50, stock.getVolume());
        stock.get(10);
        stock.get(10);
        stock.get(10);
        stock.get(10);
        stock.get(10);
        this.assertEquals("stock-volume", 0, stock.getVolume());
        try {
            stock.get(10);
            this.fail("expected ProcessingErrorException");
        } catch(ProcessingErrorException ex) {
            //as expected
        }
        stock.remove();
    }


    public void testPut_Stock()
        throws Exception
    {
        Stock stock = this.stockHome.create("stock-put", 100, 0);
        this.assertNotNull("stock after create", stock);
        this.assertEquals("stock-volume", 0, stock.getVolume());
        stock.put(10);
        stock.put(10);
        stock.put(10);
        stock.put(10);
        stock.put(10);
        this.assertEquals("stock-volume", 50, stock.getVolume());
        stock.put(10);
        stock.put(10);
        stock.put(10);
        stock.put(10);
        stock.put(10);
        this.assertEquals("stock-volume", 100, stock.getVolume());
        try {
            stock.put(10);
            this.fail("expected ProcessingErrorException");
        } catch(ProcessingErrorException ex) {
            //as expected
        }
        stock.remove();
    }


    public void testStockInClientTA()
        throws Exception
    {
        Stock source1  = this.stockHome.create("source1", 500, 200);
        Stock source2  = this.stockHome.create("source2", 500, 500);
        Stock target   = this.stockHome.create("target", 100, 0);

        UserTransaction tx =
            (UserTransaction)this.narrow("javax.transaction.UserTransaction",
                                         UserTransaction.class);

        int step = 1;

        int amount = 10;
        tx.begin();
        try {
            source1.get(amount);
            source2.get(amount * 2);
            target.put(amount);
            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
        }
        this.assertEquals(step + ":volume source1", 190, source1.getVolume());
        this.assertEquals(step + ":volume source2", 480, source2.getVolume());
        this.assertEquals((step++) + ":volume target", 10, target.getVolume());

        amount = 80;
        tx.begin();
        try {
            source1.get(amount);
            source2.get(amount * 2);
            target.put(amount);
            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
        }
        this.assertEquals(step + ":volume source1", 110, source1.getVolume());
        this.assertEquals(step + ":volume source2", 320, source2.getVolume());
        this.assertEquals((step++) + ":volume stock3", 90, target.getVolume());

        amount = 20;
        tx.begin();
        try {
            source1.get(amount);
            source2.get(amount * 2);
            target.put(amount);
            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
        }
        this.assertEquals("final:volume source1", 110, source1.getVolume());
        this.assertEquals("final:volume source2", 320, source2.getVolume());
        this.assertEquals("final:volume stock3", 90, target.getVolume());


    }


    public void testProducer()
        throws Exception
    {
        Stock stock1 = this.stockHome.create("stock1", 500, 200);
        Stock stock2 = this.stockHome.create("stock2", 500, 500);
        Stock stock3 = this.stockHome.create("stock3", 100, 0);

        int step = 1;

        Producer p = this.producerHome.create(); 

        p.produce(10);
        this.assertEquals(step + ":volume stock1", 190, stock1.getVolume());
        this.assertEquals(step + ":volume stock2", 480, stock2.getVolume());
        this.assertEquals((step++) + ":volume stock3", 10, stock3.getVolume());

        p.produce(40);
        this.assertEquals(step + ":volume stock1", 150, stock1.getVolume());
        this.assertEquals(step + ":volume stock2", 400, stock2.getVolume());
        this.assertEquals((step++) + ":volume stock3", 50, stock3.getVolume());

        p.produce(50);
        this.assertEquals(step + ":volume stock1", 100, stock1.getVolume());
        this.assertEquals(step + ":volume stock2", 300, stock2.getVolume());
        this.assertEquals((step++) + ":volume stock3", 100, stock3.getVolume());

        try {
            p.produce(10);
            this.fail("Expected UnappropriateStockException");
        } catch(UnappropriateStockException ex) {
            //as expected
        }
        this.assertEquals("final:volume stock1", 100, stock1.getVolume());
        this.assertEquals("final:volume stock2", 300, stock2.getVolume());
        this.assertEquals("final:volume stock3", 100, stock3.getVolume());
    }


    public void finalizeTest()
        throws Exception
    {
        this.cleanUp("stock");
        this.cleanUp("stock1");
        this.cleanUp("stock2");
        this.cleanUp("stock3");
        this.cleanUp("source1");
        this.cleanUp("source2");
        this.cleanUp("target");
        this.cleanUp("stock-get");
        this.cleanUp("stock-put");
    }


    private void cleanUp(String name) {
        try {
            Stock stock = this.stockHome.findByPrimaryKey(name);
            stock.remove();
        } catch(Exception ex) {}
    }

}
