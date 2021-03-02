package com.amsa.Hexdecode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HexDecodeDatabase {

    public HexDecodeDatabase() {

    }

    public static String countryCode(String code) {

	String description = "";
	PreparedStatement pstmt = null;
	ResultSet rset = null;

	// 13/12/2004, return UNKNOWN for code "0"
	if (code.equals("0") == true)
	    return "UNKNOWN";

	try {
	    Connection conn = null;

	    conn = DriverManager.getConnection("jdbc:default:connection");

	    // 16/03/2005
	    // This also works for Oracle server side JDBC driver 
	    // (need to call this java from the Oracle packages/subprograms)
	    // conn = DriverManager.getConnection("jdbc:oracle:kprb");	    
	    // OracleDriver ora = new OracleDriver();
	    // conn = ora.defaultConnection(); 

	    /*
	    // Thin client JDBC driver, this driver is not correct!
	    // Load the JDBC driver
	    String driverName = "oracle.jdbc.driver.OracleDriver";
	    Class.forName(driverName);

	    // Create a connection to the database
	    String serverName = "127.0.0.1";
	    String portNumber = "1521";
            
            String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":ausdev";
	    conn = DriverManager.getConnection(url, "ausdev", "ausdev");

	    //conn = DriverManager.getConnection("jdbc:oracle:thin:ausdev/ausdev@amsadev:1521:21");

	    */
	    String sql = "select description from sargis.mid_code where mid_code=?";
	    
	    pstmt = conn.prepareStatement(sql);

	    pstmt.setString(1, code);
	    rset = pstmt.executeQuery();

	    if (rset.next()) {
		description = rset.getString(1);
	    } else {
		description = "Undefined";
	    }
	    
	    rset.close();
	    pstmt.close();

	    
	    return description;

	} 
	catch (Exception e) 
	{
	    //System.out.println("SQLException:" + e.getMessage());
	    return e.getMessage();
	}

    } // End of countryCode

}
