package com.test.selenium.utils;

import static com.ge.selenium.testscript.TestDriver.CONFIG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.ge.selenium.testscript.Constants;

public class DBUtil {

	//private static  String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static  String DB_DRIVER = "";
	//public static  String DB_CONNECTION = "jdbc:oracle:thin:@"+dbHost+":"+dbPort+":"+SID;
	private static String DB_CONNECTION = CONFIG.getProperty("DB_CONNECTION");
	private static String DB_USER = CONFIG.getProperty("DB_USER");
	private static String DB_PASSWORD = CONFIG.getProperty("DB_PWD");
	private String DB_HOST = "";
	private String DB_PORT = "";
	private String DB_SID = "";
	private String DB_ENV = "";
	
	public DBUtil(String user,String password,String host,String port,String sid,String env){
		DB_USER = user;
		DB_PASSWORD = password;
		DB_HOST = host;
		DB_PORT = port;
		DB_SID = sid;
		DB_ENV=env;
	}
	public DBUtil(String dbconnection,String user,String password){
		DB_USER = user;
		DB_PASSWORD = password;
		DB_CONNECTION=dbconnection;
	}
	
	public DBUtil(String dbconnection,String user,String password,String dbDriver){
		DB_USER = user;
		DB_PASSWORD = password;
		DB_CONNECTION=dbconnection;
		DB_DRIVER=dbDriver;
	}
	
	public DBUtil(){
		DB_USER = CONFIG.getProperty("DB_USER");
		DB_PASSWORD = CONFIG.getProperty("DB_PWD");
		DB_CONNECTION = CONFIG.getProperty("DB_CONNECTION");
	}
	/*
	private static Connection getDBConnection() {
		
		try {
 			//Class.forName(DB_DRIVER);
			if(DB_DRIVER.equals("")){
				Class.forName(CONFIG.getProperty("DB_DRIVER"));
			}
			
 		} catch (ClassNotFoundException e) {
 			System.out.println(e.getMessage());
 			System.out.println("Oracle/PostgreSQL JDBC Driver not found. Include it in your library path!");
			e.printStackTrace();
			return null;
 		}
 		try {
 			//return DriverManager.getConnection(DB_CONNECTION,CONFIG.getProperty("DB_USER"),CONFIG.getProperty("DB_PWD"));
 			return DriverManager.getConnection(DB_CONNECTION,DB_USER,DB_PASSWORD);
 			//dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,DB_PASSWORD);
 			
 		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 			return null;
 		}
 	}
	*/
	
	private static Connection getDBConnection() {
        
        try {
              //Class.forName(DB_DRIVER);
               if(DB_DRIVER.equals("")){
                     Class.forName(CONFIG.getProperty("DB_DRIVER"));
               }
               
       } catch (ClassNotFoundException e) {
              System.out.println(e.getMessage());
              System.out.println("Oracle/PostgreSQL JDBC Driver not found. Include it in your library path!");
               e.printStackTrace();
               return null;
       }
       try {
              //(Modified the return statement)
              //return DriverManager.getConnection(DB_CONNECTION,CONFIG.getProperty("DB_USER"),CONFIG.getProperty("DB_PWD"));
              return DriverManager.getConnection(
                           CONFIG.getProperty("DB_CONNECTION"), 
                            CONFIG.getProperty("DB_USER"),
                           CONFIG.getProperty("DB_PWD")
                    );
       } catch (SQLException e) {
              return null;
       }
}

	public static String runUpdateSQL(String qry){
		
		String[] usage = {"INSERT", "DELETE", "UPDATE"};
		boolean found = false;
		Connection dbConnection = null;
		Statement statement = null;
		
		for(String s : usage){
			if(found = qry.toUpperCase().startsWith(s)) break;
		}
		if(!found){
			return Constants.KEYWORD_FAIL + "Error: use executeUpdateStmt() for INSERT, UPDATE or DELETE";
		}
		
		try {
			dbConnection = getDBConnection();
			if(dbConnection!=null){
				statement = dbConnection.createStatement();
	 			statement.executeUpdate(qry);
	 			return Constants.KEYWORD_PASS;
			}else{
				return Constants.KEYWORD_FAIL + "Io exception: The Network Adapter could not establish the connection";
			}
			
		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 			return Constants.KEYWORD_FAIL + e.toString();
 		} finally {
 			try{
 				if (statement != null) {
 					statement.close();
 				}
 			}catch(SQLException se){
 		      }// do nothing
			try{
				if (dbConnection != null) {
					dbConnection.close();
				}
			}catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
 		}
	}
	
	
	
	
	public static ArrayList<ArrayList<String>> executeSelectStmt(String query) throws SQLException
	{
		Connection connection 	= null;
		ResultSet rs 			= null;
		Statement stmt 			= null;
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		
		try {
			//connection = getDBConnect();
			connection = getDBConnection();
			stmt = connection.createStatement();
 			rs = stmt.executeQuery(query);
 			ResultSetMetaData rsmd = rs.getMetaData();
 			
 			int NumOfCol=rsmd.getColumnCount();
 			
 			while (rs.next()) {
 				ArrayList<String> record = new ArrayList<String>();				
 				for(int i=1;i<=NumOfCol;i++){
 					String value = rs.getString(i);
 					if(value==null){
 						value="";
 					}
                    record.add(value);
 				}
 				result.add(record);
 			}
 			rs.close();
 			return result;
 		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 		} finally {
 			if (stmt != null) {
 				stmt.close();
			}
 			if (connection != null) {
 				connection.close();
			}
 		}
 		return result;
		
	}
	
	public static String getDBData(String query) throws SQLException
	{
		Connection connection 	= null;
		ResultSet rs 			= null;
		Statement stmt 			= null;
		//ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();\
		String result="";
		
		try {
			//connection = getDBConnect();
			connection = getDBConnection();
			stmt = connection.createStatement();
 			rs = stmt.executeQuery(query);
 			ResultSetMetaData rsmd = rs.getMetaData();
 			
 			int NumOfCol=rsmd.getColumnCount();
 			
 			while (rs.next()) {
 				for(int i=1;i<=NumOfCol;i++){
 					String value = rs.getString(i);
 					if(value==null){
 						value="";
 					}
                    result=result+value+",";
 				}
 				
 			}
 			if(!result.equals("")){
 				result=result.substring(0, result.length()-1);
 			}
 			rs.close();
 			return result;
 		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 		} finally {
 			if (stmt != null) {
 				stmt.close();
			}
 			if (connection != null) {
 				connection.close();
			}
 		}
 		return result;
		
	}
	
	public static String runSQLFile(String file) throws SQLException
    {
        String s            = new String();
        StringBuffer sb = new StringBuffer();

        try
        {
            //FileReader fr = new FileReader(new File("C:/ALTestSuites/SQL/test.sql"));
        	File sqlFile = new File(file);
        	if(!sqlFile.exists()){
        		return Constants.KEYWORD_FAIL + " File "+file+" Does not Exist Please check the PATH";
        	}
        	//FileReader fr = new FileReader(new File(file));
        	ArrayList<String> listOfQueries =	getQueriesFromFile(sqlFile);;
        	if(listOfQueries.size()==0){
        		return Constants.KEYWORD_FAIL + "No Queries found in the SQL File? -- Something went wrong in processing SQL file";
        	}
        	
            Connection dbConnection = getDBConnection();
			if(dbConnection!=null){
				Statement st = dbConnection.createStatement();
				for(int i = 0; i<listOfQueries.size(); i++)
	            {
					st.addBatch(listOfQueries.get(i));
	            }
				st.executeBatch();
	 			return Constants.KEYWORD_PASS;
			}else{
				return Constants.KEYWORD_FAIL + "Io exception: The Network Adapter could not establish the connection";
			}
        }
        catch(Exception e)
        {
            return Constants.KEYWORD_FAIL + e.toString();
        }

    }
	 /**
     * @param 	path	Path to the SQL file
     * @return			List of query strings 
     */
	public static ArrayList<String> getQueriesFromFile(File file){
		String queryLine = 		new String();
        StringBuffer sBuffer = 	new StringBuffer();
        ArrayList<String> listOfQueries =	new ArrayList<String>();
    	
	    try  
	    {  
	        FileReader fr = 	new FileReader(file); 	  
	        BufferedReader br = new BufferedReader(fr);  
	  
	        //read the SQL file line by line
	        while((queryLine = br.readLine()) != null)  
	        {  
	        	// ignore comments beginning with #
	        	int indexOfCommentSign = queryLine.indexOf('#');
	        	if(indexOfCommentSign != -1)
	        	{
	        		if(queryLine.startsWith("#"))
	        		{
	        			queryLine = new String("");
	        		}
	        		else 
	        			queryLine = new String(queryLine.substring(0, indexOfCommentSign-1));
	        	}
	        	// ignore comments beginning with --
	        	indexOfCommentSign = queryLine.indexOf("--");
	        	if(indexOfCommentSign != -1)
	        	{
	        		if(queryLine.startsWith("--"))
	        		{
	        			queryLine = new String("");
	        		}
	        		else 
	        			queryLine = new String(queryLine.substring(0, indexOfCommentSign-1));
	        	}
	        	// ignore comments surrounded by /* */
	        	indexOfCommentSign = queryLine.indexOf("/*");
	        	if(indexOfCommentSign != -1)
	        	{
	        		if(queryLine.startsWith("/*"))
	        		{
	        			queryLine = new String("");
	        		}
	        		else
	        			queryLine = new String(queryLine.substring(0, indexOfCommentSign-1));
	        		
	        		sBuffer.append(queryLine + " "); 
	        		// ignore all characters within the comment
	        		do
	        		{
	        			queryLine = br.readLine();
	        		}
	        		while(queryLine != null && !queryLine.contains("*/"));
	        		
	        		indexOfCommentSign = queryLine.indexOf("*/");
	        		if(indexOfCommentSign != -1)
	        		{
	        			if(queryLine.endsWith("*/"))
	        			{
	        				queryLine = new String("");
	        			}
	        			else
	        				queryLine = new String(queryLine.substring(indexOfCommentSign+2, queryLine.length()-1));
	        		}
	        		
	        	}
	        	
	        	//  the + " " is necessary, because otherwise the content before and after a line break are concatenated
	        	// like e.g. a.xyz FROM becomes a.xyzFROM otherwise and can not be executed 
	        	if(queryLine != null)
	        		sBuffer.append(queryLine + " ");  
	        }  
	        br.close();
	        
	        // here is our splitter ! We use ";" as a delimiter for each request 
	        String[] splittedQueries = sBuffer.toString().split(";");
	        
	        // filter out empty statements
	        for(int i = 0; i<splittedQueries.length; i++)  
            {
	        	if(!splittedQueries[i].trim().equals("") && !splittedQueries[i].trim().equals("\t"))  
                {
	        		listOfQueries.add(new String(splittedQueries[i]));
                }
            }
	    }  
	    catch(Exception e)  
	    {  
	        System.out.println("*** Error : "+e.toString());  
	        System.out.println("*** ");  
	        System.out.println("*** Error : ");  
	        e.printStackTrace();  
	        System.out.println("################################################");  
	        System.out.println(sBuffer.toString());  
	    }
	    return listOfQueries;
	}
}
