package db.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;


public final class SetupDB {

    public static final String JDBC_URL  = "jdbc.url";
    public static final String JDBC_USER = "jdbc.user";
    public static final String JDBC_PWD  = "jdbc.pwd";

    private File[] files;
    private String dbUrl;
    private String dbUser;
    private String dbPwd;

    public SetupDB(File dir) {
        this.files = dir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".sql");
            }
        });
        Arrays.sort(this.files, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((File)o1).getName().compareTo(((File)o2).getName());
            }
        });
        this.dbUrl = System.getProperty(JDBC_URL);
        if(this.dbUrl == null) {
            System.err.println("System property '"
                               + JDBC_URL + "' not provided!");
        }
        this.dbUser = System.getProperty(JDBC_USER);
        if(this.dbUser == null) {
            System.err.println("System property '"
                               + JDBC_USER + "' not provided!");
        }
        this.dbPwd  = System.getProperty(JDBC_PWD);
        if(this.dbPwd == null) {
            System.err.println("System property '"
                               + JDBC_PWD + "' not provided!");
        }
    }


    public void run() {
        if(this.dbUrl == null)
            return;

        int num = this.files.length;

        for(int i = 0; i < this.files.length; i++) {
            try {
                String stmt = this.getStatement(this.files[i]);
                this.executeStatement(stmt);
            } catch(Exception ex) {
                System.err.println(this.files[i] + ": "
                                  + ex.getMessage());
                num--;
            }
        }
        System.out.println("executed " + this.files.length +
                          " files, " + num + " successfully");
    }


    private void executeStatement(String stmt)
        throws SQLException
    {
        Connection con = null;
        Statement  st  = null;
        boolean    success = false;
        String     current = null;
        try {
            con = DriverManager.getConnection(this.dbUrl,
                                              this.dbUser,
                                              this.dbPwd);
            con.setAutoCommit(false);
            st = con.createStatement();
            java.util.StringTokenizer t = new java.util.StringTokenizer(stmt, ";");
            while(t.hasMoreTokens()) {
                current = t.nextToken();
                if(current.length() < 3) continue;
                st.executeUpdate(current);
            }
            success = true;
        } finally {
            if(success) {
                try { con.commit(); } catch(Exception ex) {}
            } else {
                System.out.println("ERROR executing '" + current + "'");
                try { con.rollback(); } catch(Exception ex) {}
            }
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }


    private String getStatement(File f)
        throws IOException
    {
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        StringBuffer sb = new StringBuffer();
        try {
            int c;
            while((c = br.read()) >= 0) {
                sb.append((char)c);
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("usage: SetupDB <dir>");
            System.err.println("====================");
            System.err.println("... where dir is a directory containing");
            System.err.println("files with the extension sql");
            return;
        }
        File f = new File(args[0]);
        if(!f.exists()) {
            System.err.println(args[0] + " does not exist!");
            return;
        }
        if(!f.isDirectory()) {
            System.err.println(args[0] + " is not a directory!");
            return;
        }
        SetupDB db = new SetupDB(f);
        db.run();
    }

}
