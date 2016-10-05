package ejb.test;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Enumeration;
import java.text.DateFormat;
import java.lang.reflect.Method;

public final class EJBTest {
    
    private static final String TC  = "test.class.";
    private static final String TO  = "test.output.dir";
    private static final String EAP = "ejb-test-";
    private static final String MP  = "test";
    private static final String BR  = "<br>";
    private static final String AT  = "\tat ";
    
    private Properties   theProps;
    private StringBuffer theBuffer;
    private Object[]     theClasses;
    private String       theDate;
    private long         beginTime;
    private long         endTime;
    private long         totalTime;
    private int          errCount;
    private int          totalErrors;

    
    public EJBTest() {
        DateFormat df = DateFormat.getDateTimeInstance
                                    (DateFormat.SHORT, DateFormat.SHORT);
        theProps      = null;
        theBuffer     = null;
        theClasses    = null;
        theDate       = df.format(new Date());
        totalTime     = 0;
        totalErrors   = 0;
    }
    

    public void init(String propertyFile) {
        String msg = "";
        InputStream in = null;
        in = getClass().getResourceAsStream(propertyFile);
        if(in == null) {
            in = getClass().getResourceAsStream("/" + propertyFile);
        }
        if(in != null) {
            try {
                File f = new File(propertyFile);
                in = new FileInputStream(f);
            } catch(Exception ex) {
                msg = ex.getMessage();
                in = null;
            }
        }
        if(in == null) {
            msg = propertyFile + " cannot be found!";
            throw new IllegalStateException("cannot load " + propertyFile + " :" + msg);
        }
        Properties p = new Properties();
        try {
            p.load(in);
        } catch(Exception ex) {
            throw new IllegalStateException("cannot read " + propertyFile + " :" + ex.getMessage());
        }
        init(p);
    }
    
    
    public void init(Properties p) {
        if(p == null) {
            throw new IllegalArgumentException("null is not allowed");
        }
        theProps = p;
        ArrayList al = new ArrayList();
        Enumeration e = theProps.propertyNames();
        String name;
        String cname;
        Class  c;
        while(e.hasMoreElements()) {
            name = (String)e.nextElement();
            if(name.startsWith(TC)) {
                cname = theProps.getProperty(name);
                try {
                    c = Class.forName(cname);
                    al.add(c);
                } catch(Exception ex) {
                    al.add(cname);
                }
            }
        }
        theClasses = al.toArray();
        initOutputBuffer();
    }
    
    
    private void initOutputBuffer() {
        theBuffer = new StringBuffer(20480);
        theBuffer.append("<html>");
        theBuffer.append("<head>");
        theBuffer.append("<title>EJB-Test-Run ").append(theDate).append("</title>");
        theBuffer.append("</head>");
        theBuffer.append("<body bgcolor=\"white\">");
        theBuffer.append("<center><h2>Results of Test-Run on ");
        theBuffer.append(theDate);
        theBuffer.append("</h2></center>");
        theBuffer.append("<p>");
    }
    
    
    private void reportBeginSection(String name, Throwable t) {
        theBuffer.append("<p><u><b>").append(name).append("</b></u><p>");
        theBuffer.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\">");
        if(t == null) {
            theBuffer.append("<tr>");
            theBuffer.append("<td>").append("Test").append("</td>");
            theBuffer.append("<td>").append("Time in sec.").append("</td>");
            theBuffer.append("<td>").append("result").append("</td>");
            theBuffer.append("</tr>");
        } else {
             theBuffer.append("<tr><td bgcolor=\"red\">");
            String s = formatThrowable(t);
            theBuffer.append(s).append("</td></tr></table>");
        }
        beginTime = System.currentTimeMillis();
        errCount  = 0;
    }
    
    
    private void reportTestCase(String name, long time, Throwable t) {
        theBuffer.append("<tr>");
        theBuffer.append("<td>").append(name).append("</td>");
        theBuffer.append("<td>").append(time/1000).append("</td>");
        if(t == null) {
            theBuffer.append("<td bgcolor=\"green\">").append("success").append("</td>");
        } else {
            errCount++;
            totalErrors++;
            String s = formatThrowable(t);
            theBuffer.append("<td bgcolor=\"red\">").append(s).append("</td>");
        }
        theBuffer.append("</tr>");
    }
    
    
    private void reportEndSection(String name) {
        endTime = System.currentTimeMillis();
        theBuffer.append("</table>");
        theBuffer.append("<i>");
        theBuffer.append(name);
        theBuffer.append("</i>");
        theBuffer.append(" finished after ");
        totalTime += (endTime - beginTime) / 1000;
        theBuffer.append((endTime - beginTime) / 1000);
        theBuffer.append(" second(s) with ");
        theBuffer.append(errCount);
        theBuffer.append(" error(s).");
    }
    
    
    private void closeOutputBuffer() {
        theBuffer.append("<p><p>");
        theBuffer.append("<b>done in ").append(totalTime).append(" second(s).</b>");
        theBuffer.append("<b> ").append(totalErrors).append(" error(s).</b>");
        theBuffer.append("<p></body></html>");
        String path = theProps.getProperty(TO);
        File   f = new File(computeFileName(path));
        String s = theBuffer.toString();
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(s, 0, s.length());
            fw.flush();
            fw.close();
        } catch(IOException ioex) {
            ioex.printStackTrace();
            System.err.println("dumping to screen ...");
            System.out.println(s);
            System.err.println("done.");
        }
    }
    
    
    private String formatThrowable(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter  pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        pw.close();
        StringBuffer sb = sw.getBuffer();
        int index = 0;
        while((index = sb.toString().indexOf(AT, index)) > 0) {
            sb.insert(index, BR);
            index += BR.length() + AT.length() + 1;
        }
        return sb.toString();
    }
    
    
    private String computeFileName(String path) {
        StringBuffer sb = new StringBuffer();
        if(path != null) {
            sb.append(path);
        }
        sb.append(EAP);
        Calendar cal = new GregorianCalendar();
        sb.append(cal.get(Calendar.YEAR));
        sb.append(format(cal.get(Calendar.MONTH) + 1));
        sb.append(format(cal.get(Calendar.DAY_OF_MONTH)));
        sb.append("_");
        sb.append(format(cal.get(Calendar.HOUR_OF_DAY)));
        sb.append(format(cal.get(Calendar.MINUTE)));
        sb.append(".html");
        return sb.toString();
    }


    private String format(int num) {
      String ret;
      if(num < 10) {
        ret  = Integer.toString(0);
        ret += Integer.toString(num);
      } else {
        ret = Integer.toString(num);
      }
      return ret;
    }
    
    
    public void runTests() {
        Class       cl;
        EJBTestCase tc;
        for(int i = 0; i < theClasses.length; i++) {
            if(theClasses[i] instanceof String) {
                try {
                    cl = Class.forName((String)theClasses[i]);
                } catch(Exception ex) {
                    reportBeginSection((String)theClasses[i], ex);
                    continue;
                }
            } else {
                cl = (Class)theClasses[i];
            }
            try {
                tc = (EJBTestCase)cl.newInstance();
                tc.setProperties(theProps);
            } catch(Exception ex) {
                reportBeginSection(cl.getName(), ex);
                continue;
            }
            reportBeginSection(cl.getName(), null);
            runTest(tc);
            reportEndSection(cl.getName());
        }
        closeOutputBuffer();
    }
    
    
    private void runTest(EJBTestCase tc) {
        Class    c  = tc.getClass();
        Method[] ms = c.getMethods();
        String   name;
        Class[]  params;
        try {
            tc.prepareTest();
        } catch(Exception ex) {
            reportTestCase("prepareTest", 0, ex);
            return;
        }
        for(int i = 0; i < ms.length; i++) {
            name   = ms[i].getName();
            params = ms[i].getParameterTypes();
            if(!(name.startsWith(MP) && params.length == 0)) {
                continue;
            }
            try {
                long t1 = System.currentTimeMillis();
                ms[i].invoke(tc, params);
                long t2 = System.currentTimeMillis();
                reportTestCase(name, (t2 - t1), null);
            } catch(Exception ex) {
                reportTestCase(name, 0, ex);
            }
        }
        try {
            tc.finalizeTest();
        } catch(Exception ex) {
            reportTestCase("finalizeTest", 0, ex);
        }
    }
    
    
    public static void main(String[] args) {
        EJBTest et = new EJBTest();
        if(args.length == 1) {
            et.init(args[0]);
        } else {
            et.init(new Properties());
        }
        et.runTests();
    }

}
