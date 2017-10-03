package com.test.selenium.utils;



import au.com.bytecode.opencsv.CSVReader;
import com.ge.selenium.testscript.Constants;
import org.apache.commons.lang.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
 
/**
 * 
 * @author viralpatel.net
 * 
 */
public class CSVLoader {
 
    private static final
        String SQL_INSERT = "INSERT INTO ${table}(${keys}) VALUES(${values})";
    private static final String TABLE_REGEX = "\\$\\{table\\}";
    private static final String KEYS_REGEX = "\\$\\{keys\\}";
    private static final String VALUES_REGEX = "\\$\\{values\\}";
 
    private Connection connection;
    private char seprator;
 
    /**
     * Public constructor to build CSVLoader object with
     * Connection details. The connection is closed on success
     * or failure.
     * @param connection
     */
    public CSVLoader(Connection connection) {
        this.connection = connection;
        //Set default separator
        this.seprator = ',';
    }
    public CSVLoader(){
    	this.seprator = ',';
    }
    
    private  Connection getDBConnection() {
    	String DB_CONNECTION = "jdbc:oracle:thin:@3.39.73.57:1534:DEEVSEDB";
		try {
 			//Class.forName(DB_DRIVER);
			Class.forName("oracle.jdbc.driver.OracleDriver");
 		} catch (ClassNotFoundException e) {
 			System.out.println(e.getMessage());
 			System.out.println("Oracle JDBC Driver not found. Include it in your library path!");
			e.printStackTrace();
			return null;
 		}
 		      
 		try {
 			//return DriverManager.getConnection(DB_CONNECTION,CONFIG.getProperty("DB_USER"),CONFIG.getProperty("DB_PWD"));
 			return DriverManager.getConnection(DB_CONNECTION,"sreddy","D1g1talEnergy");
 			//dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,DB_PASSWORD);
 			
 		} catch (SQLException e) {
 			System.out.println(e.getMessage());
 			return null;
 		}
 	}
    /**
     * Parse CSV file using OpenCSV library and load in 
     * given database table. 
     * @param csvFile Input CSV file
     * @param tableName Database table name to import data
     * @param truncateBeforeLoad Truncate the table before inserting 
     *          new records.
     * @throws Exception
     */
    //public void loadCSV(String csvFile, String tableName,String project,boolean truncateBeforeLoad) throws Exception {
    public void loadCSV(String csvFile, String tableName,String project,boolean truncateBeforeLoad, String sprint) throws Exception {
 
	        CSVReader csvReader = null;
	        if(null == this.connection) {
	        	connection = getDBConnection();
	        	if(null == connection)
	            throw new Exception("Not a valid connection.");
	        }
	        try {
	            csvReader = new CSVReader(new FileReader(csvFile), this.seprator);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("Error occured while executing file. "
	                    + e.getMessage());
	        }
	        
	        String[] headerRow = csvReader.readNext();
	        
	        if (null == headerRow) {
	            throw new FileNotFoundException(
	                    "No columns defined in given CSV file." +
	                    "Please check the CSV file format.");
	        }
	       
	        List<String> lst = new ArrayList<String>();
	        lst.add(headerRow[0]);
	        lst.add(headerRow[1]);
	        lst.add(headerRow[2]);
	        lst.add(headerRow[3]);
	        lst.add(headerRow[4]);
	        lst.add(headerRow[5]);
	        lst.add(headerRow[6]);
	        lst.add(headerRow[7]);
	        lst.add(headerRow[8]);
	        lst.add(headerRow[9]);
	        lst.add(headerRow[11]);
	        lst.add(headerRow[15].split(" ")[1]);
	        lst.add("Release");
	        
	        //String questionmarks = StringUtils.repeat("?,", headerRow.length);
	        String questionmarks = StringUtils.repeat("?,", lst.size());
	        questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);
	 
	        String query = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
	        query = query.replaceFirst(KEYS_REGEX, StringUtils.join(lst, ","));
	        query = query.replaceFirst(VALUES_REGEX, questionmarks);
	 
	        System.out.println("Query: " + query);
	 		
	        String version="";
	        String release="";
	 		
	        String[] nextLine;
	        Connection con = null;
	        
	        PreparedStatement ps = null;
	        try {
	            con = this.connection;
	            con.setAutoCommit(false);
	            ps = con.prepareStatement(query);
	            
	            if(truncateBeforeLoad) {
	                //delete data from table before loading csv
	            	//con.createStatement().execute("DELETE FROM " + tableName +" where project = '"+project+"'");
	                con.createStatement().execute("DELETE FROM " + tableName +" where project = '"+project+"' and versions= '"+sprint+"'");
	            }
	 
	            final int batchSize = 1000;
	            int count = 0;
	            Date date = null;
	            while ((nextLine = csvReader.readNext()) != null) {
	 
	                if (null != nextLine) {
	                    int index = 1;
	                    //for (String string : nextLine) {
	                    //for(int j=0;j<12;j++){
	                    for(int j=0;j<16;j++){
	                    	if(j!=10 && j!=12 && j!=13 && j!=14){
	                    		date = DateUtil.convertToDate(nextLine[j]);
	                            if (null != date) {
	                                ps.setDate(index, new java.sql.Date(date.getTime()));
	                            //	//ps.setDate(j++, new java.sql.Date(date.getTime()));
	                            } else {
                            	if(j==15){
//	                            		//'Release 1.0' (17042), 'Sprint 3' (17850)
//	                            		if(nextLine[j].contains(",")){
//	                            			version = nextLine[j].split(",")[1].split("'")[1];
//	                            			release = nextLine[j].split(",")[0].split("'")[1];
//		                            		//version = (String) version.subSequence(1, version.length());
//	                            		}else{
//	                            			version = nextLine[j].split("'")[1];
//	                            			release = version;
//	                            			//version = (String) version.subSequence(1, version.length());
//	                            		}
//
                            			version = sprint;
	                            		ps.setString(index, version);
	                            		ps.setString(index+1, release);
	                            	}else{
	                            		ps.setString(index, nextLine[j]);
	                            	}
	                            	
	                            	//ps.setString(j++, nextLine[j]);
	                            }
	                            System.out.println("index vlaue "+index);
	                            System.out.println("vlaue "+nextLine[j]);
	                            index++;
	                    	}
	                    }
	                    
	                    ps.addBatch();
	                  
	                }
	                if (++count % batchSize == 0) {
	                    ps.executeBatch();
	                }
	            }
	            ps.executeBatch(); // insert remaining records
	            con.commit();
	        } catch (Exception e) {
	            con.rollback();
	            e.printStackTrace();
	            throw new Exception(
	                    "Error occured while loading data from file to database."
	                            + e.getMessage());
	        } finally {
	            if (null != ps)
	                ps.close();
	            if (null != con)
	                con.close();
	 
	            csvReader.close();
	        }
    }

    
    //public void loadUserStory(String csvFile, String tableName,String project,boolean truncateBeforeLoad) throws Exception {
    public void loadUserStory(String project, String sprint,boolean truncateBeforeLoad) throws Exception {	
    	//loader.loadUserStory("C:\\AST\\JIRA_DATA\\"+project+"_"+sprint+".csv", "USER_STORY", project,true);
    	String csvFile = "C:\\AST\\JIRA_DATA\\"+project+"_"+sprint+".csv";
    	String tableName = "USER_STORY";
        CSVReader csvReader = null;
        if(null == this.connection) {
        	connection = getDBConnection();
        	if(null == connection)
            throw new Exception("Not a valid connection.");
        }
        try {
            csvReader = new CSVReader(new FileReader(csvFile), this.seprator);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error occured while executing file. "
                    + e.getMessage());
        }
        
        String[] headerRow = csvReader.readNext();
        
        if (null == headerRow) {
            throw new FileNotFoundException(
                    "No columns defined in given CSV file." +
                    "Please check the CSV file format.");
        }
       
        List<String> lst = new ArrayList<String>();
        lst.add("PROJECT");
        lst.add("KEY");
        lst.add("Summary");
        lst.add("VERSIONS");
//        lst.add(headerRow[3]);
//        lst.add(headerRow[4]);
//        lst.add(headerRow[5]);
//        
//        lst.add(headerRow[0]);
//        lst.add(headerRow[1]);
//        lst.add(headerRow[2]);
//        lst.add(headerRow[3]);
//        lst.add(headerRow[4]);
//        lst.add(headerRow[5]);
//               
        //String questionmarks = StringUtils.repeat("?,", headerRow.length);
        String questionmarks = StringUtils.repeat("?,", lst.size());
        questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);
 
        String query = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
        query = query.replaceFirst(KEYS_REGEX, StringUtils.join(lst, ","));
        query = query.replaceFirst(VALUES_REGEX, questionmarks);
 
        System.out.println("Query: " + query);
 		
        String version="";
        String release="";
 		
        String[] nextLine;
        Connection con = null;
        
        PreparedStatement ps = null;
        try {
            con = this.connection;
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            
            if(truncateBeforeLoad) {
                //delete data from table before loading csv
                con.createStatement().execute("DELETE FROM " + tableName +" where project = '"+project+"' and versions= '"+sprint+"'");
            }
 
            final int batchSize = 1000;
            int count = 0;
            Date date = null;
            while ((nextLine = csvReader.readNext()) != null) {
 
                if (null != nextLine) {
                    int index = 1;
                    //for (String string : nextLine) {
                    //for(int j=0;j<12;j++){
                    if(nextLine[4].equalsIgnoreCase("Y")){
                    	for(int j=0;j<lst.size();j++){
                        	ps.setString(index, nextLine[j]);
                        	System.out.println("value "+nextLine[j]);
                            index++;
                        }
                        
                        ps.addBatch();
                    }
                }
                if (++count % batchSize == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch(); // insert remaining records
            con.commit();
        } catch (Exception e) {
            con.rollback();
            e.printStackTrace();
            throw new Exception(
                    "Error occured while loading data from file to database."
                            + e.getMessage());
        } finally {
            if (null != ps)
                ps.close();
            if (null != con)
                con.close();
 
            csvReader.close();
        }
    }

    
    public void loadActiveSprints(String csvFile, boolean truncateBeforeLoad) throws Exception {
   	 	String tableName = "ACTIVE_SPRINTS";
        CSVReader csvReader = null;
        if(null == this.connection) {
        	connection = getDBConnection();
        	if(null == connection)
            throw new Exception("Not a valid connection.");
        }
        try {
            csvReader = new CSVReader(new FileReader(csvFile), this.seprator);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error occured while executing file. "
                    + e.getMessage());
        }
        
        String[] headerRow = csvReader.readNext();
        
        if (null == headerRow) {
            throw new FileNotFoundException(
                    "No columns defined in given CSV file." +
                    "Please check the CSV file format.");
        }
       
        List<String> lst = new ArrayList<String>();
        lst.add("PROJECT");
        lst.add("ACTIVE_SPRINT");
        String questionmarks = StringUtils.repeat("?,", lst.size());
        questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);
 
        String query = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
        query = query.replaceFirst(KEYS_REGEX, StringUtils.join(lst, ","));
        query = query.replaceFirst(VALUES_REGEX, questionmarks);
 
        System.out.println("Query: " + query);
 		
        String[] nextLine;
        Connection con = null;
        
        PreparedStatement ps = null;
        try {
            con = this.connection;
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            
            if(truncateBeforeLoad) {
                //delete data from table before loading csv
                con.createStatement().execute("DELETE FROM " + tableName);
            }
 
            final int batchSize = 1000;
            int count = 0;
            Date date = null;
            while ((nextLine = csvReader.readNext()) != null) {
 
                if (null != nextLine) {
                    int index = 1;
                    //if(nextLine[4].equalsIgnoreCase("Y")){
                    	for(int j=0;j<lst.size();j++){
                        	ps.setString(index, nextLine[j]);
                        	System.out.println("value "+nextLine[j]);
                            index++;
                        }
                        
                        ps.addBatch();
                    //}
                }
                if (++count % batchSize == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch(); // insert remaining records
            con.commit();
        } catch (Exception e) {
            con.rollback();
            e.printStackTrace();
            throw new Exception(
                    "Error occured while loading data from file to database."
                            + e.getMessage());
        } finally {
            if (null != ps)
                ps.close();
            if (null != con)
                con.close();
 
            csvReader.close();
        }
    }
    
    public char getSeprator() {
        return seprator;
    }
 
    public void setSeprator(char seprator) {
        this.seprator = seprator;
    }
    /*
     * Load Sprint[version] details
     * 
     */
    public void loadVersions(String csvFile, String tableName,String project,boolean truncateBeforeLoad) throws Exception {
    	 
        CSVReader csvReader = null;
        String isCurrent="";
        if(null == this.connection) {
            throw new Exception("Not a valid connection.");
        }
        try {
            csvReader = new CSVReader(new FileReader(csvFile), this.seprator);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error occured while executing file. "
                    + e.getMessage());
        }
        
        String[] headerRow = csvReader.readNext();
        
        if (null == headerRow) {
            throw new FileNotFoundException(
                    "No columns defined in given CSV file." +
                    "Please check the CSV file format.");
        }
       
        List<String> lst = new ArrayList<String>();
        lst.add(headerRow[0]);
        lst.add(headerRow[1]);
        lst.add(headerRow[2]);
        lst.add(headerRow[3]);
        lst.add(headerRow[4].replace(" ", "_"));
        lst.add("CURRENT_SPRINT");
        lst.add("Project");
        
        //String questionmarks = StringUtils.repeat("?,", headerRow.length);
        String questionmarks = StringUtils.repeat("?,", lst.size());
        questionmarks = (String) questionmarks.subSequence(0, questionmarks.length() - 1);
 
        String query = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
        query = query.replaceFirst(KEYS_REGEX, StringUtils.join(lst, ","));
        query = query.replaceFirst(VALUES_REGEX, questionmarks);
 
        System.out.println("Query: " + query);
 		
        String version="";
        String release="";
 		
        String[] nextLine;
        Connection con = null;
        
        PreparedStatement ps = null;
        try {
            con = this.connection;
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            
            if(truncateBeforeLoad) {
                //delete data from table before loading csv
                con.createStatement().execute("DELETE FROM " + tableName +" where project = '"+project+"'");
            }
 
            final int batchSize = 1000;
            int count = 0;
            Date date = null;
            while ((nextLine = csvReader.readNext()) != null) {
                if (null != nextLine) {
                    int index = 1;
                    //for (String string : nextLine) {
                    //for(int j=0;j<12;j++){
                    for(int j=0;j<lst.size()-2;j++){
                    	
                    		date = DateUtil.convertToDate(nextLine[j]);
                            if (null != date) {
                                ps.setDate(index, new java.sql.Date(date.getTime()));
                                isCurrent=isCurrentSprint(date);
                            //	//ps.setDate(j++, new java.sql.Date(date.getTime()));
                            } else {
                            	ps.setString(index, nextLine[j]);
                            	isCurrent="N";
                            }
                            index++;
                    	
                    }
                    ps.setString(index, isCurrent);
                    ps.setString(index+1, project);
                    ps.addBatch();
                }
                if (++count % batchSize == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch(); // insert remaining records
            con.commit();
        } catch (Exception e) {
            con.rollback();
            e.printStackTrace();
            throw new Exception(
                    "Error occured while loading data from file to database."
                            + e.getMessage());
        } finally {
            if (null != ps)
                ps.close();
            if (null != con)
                con.close();
 
            csvReader.close();
        }
    }
    
    public String isCurrentSprint(Date endDate){
    	String isCurrent="";
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    	Date today = new Date(); 
    	Calendar c = Calendar.getInstance();
    	c.setTime(endDate);
    	c.add(Calendar.DATE, -14);
    	Date startDate = DateUtil.convertToDate(sdf.format(c.getTime()));
    	
    	if (today.compareTo(endDate)<0 && today.compareTo(startDate)>0){
    		isCurrent="Y";
    	}else{
    		isCurrent="N";
    	}
    	System.out.println("Start DAte ="+startDate+ " End date = "+endDate+" Is Current "+isCurrent);
    	return isCurrent;
    }
    
    public String executeQuery(String qry){
    	Connection dbConnection = null;
		Statement statement = null;
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
    public ArrayList<ArrayList<String>> getJMeterResults(String csvFile) throws Exception{
    	 CSVReader csvReader = null;
         String[] nextLine;
         ArrayList<ArrayList<String>> testSteps = new ArrayList<ArrayList<String>>();
 		 String result="";
         try {
             csvReader = new CSVReader(new FileReader(csvFile), this.seprator);
         } catch (Exception e) {
        	 return testSteps;
//             e.printStackTrace();
//             throw new Exception("Error occured while executing file. "
//                     + e.getMessage());
         }
         
         String[] headerRow = csvReader.readNext();
         List<String> headerList = new ArrayList<String>();
         if (null == headerRow) {
             throw new FileNotFoundException(
                     "No columns defined in given CSV file." +
                     "Please check the CSV file format.");
         }else{
        	for (int i = 0; i < headerRow.length; i++) {
        		headerList.add(headerRow[i].trim());
     		}
         }
         while ((nextLine = csvReader.readNext()) != null) {
             if (null != nextLine) {
            	 ArrayList<String> data = new ArrayList<String>();
     			data.add(nextLine[headerList.indexOf("label")]);
     			data.add(nextLine[headerList.indexOf("responseCode")]);
     			data.add(nextLine[headerList.indexOf("responseMessage")]);
     			data.add(nextLine[headerList.indexOf("threadName")].trim().split(" ")[0]);
     			if(nextLine[headerList.indexOf("success")].trim().equalsIgnoreCase("TRUE")){
     				result = Constants.KEYWORD_PASS;
     			}else{
     				result = Constants.KEYWORD_FAIL;
     			}
     			data.add(result);
     			testSteps.add(data);
            	 //System.out.println("TC "+nextLine[2]+" Status "+nextLine[4]);
            	// System.out.println("TC "+nextLine[headerList.indexOf("label")]+" Status "+nextLine[headerList.indexOf("success")]);
            	 
             }
         }
         return testSteps;
    }
    
    public void loadQMData(String csvFile) throws Exception{
   	 CSVReader csvReader = null;
        String[] nextLine;
        ArrayList<ArrayList<String>> testSteps = new ArrayList<ArrayList<String>>();
		 String result="";
        try {
            csvReader = new CSVReader(new FileReader(csvFile), this.seprator);
        } catch (Exception e) {
       	 //return testSteps;
//            e.printStackTrace();
//            throw new Exception("Error occured while executing file. "
//                    + e.getMessage());
        }
        
        String[] headerRow = csvReader.readNext();
        List<String> headerList = new ArrayList<String>();
        if (null == headerRow) {
            throw new FileNotFoundException(
                    "No columns defined in given CSV file." +
                    "Please check the CSV file format.");
        }else{
       	for (int i = 0; i < headerRow.length; i++) {
       		headerList.add(headerRow[i].trim());
    		}
        }
        ArrayList<String> data = new ArrayList<String>();
        String ts = TestUtil.now("dd.MMM.yyyy_hh_mm_ss");
        System.out.println("date "+ts);
        //to_timestamp('"+start+"','yyyy-mm-dd')
        while ((nextLine = csvReader.readNext()) != null) {
            if (null != nextLine) {
           	 
    			data.add(nextLine[headerList.indexOf("Sprint")]);
    			data.add(nextLine[headerList.indexOf("Project")]);
    			data.add(nextLine[headerList.indexOf("Backlog Grooming")]);
    			data.add(nextLine[headerList.indexOf("Acceptance Criteria")]);
    			data.add(nextLine[headerList.indexOf("QA Env. Available")]);
    			data.add(nextLine[headerList.indexOf("Ontime Story Delivery")]);
    			data.add(nextLine[headerList.indexOf("Test Coverage")]);
    			data.add(nextLine[headerList.indexOf("Automation")]);
    			data.add(nextLine[headerList.indexOf("Unit Tests")]);
    			data.add(nextLine[headerList.indexOf("Defect Count")]);
    			//data.add(result);
    			//testSteps.add(data);
           	}
        }
        String query = "insert into QUALITY_SCORECARD (project,backlog_grooming,acceptance_criteria,qa_env_available,ontime_story_delivery,test_coverage,automation,unit_tests,defect_count,sprint)"+ 
			"values('"+data.get(1)+"','"+data.get(2)+"','"+data.get(3)+"','"+data.get(4)+"','"+data.get(5)+"','"+data.get(6)+"','"+data.get(7)+"','"+data.get(8)+"','"+data.get(9)+"','"+data.get(0)+"')";
        //executeQuery(query);
        for(int i=0;i<data.size();i++){
        	System.out.println("value = "+data.get(i));
        }
     //   return testSteps;
   }
    
    
    public static void main(String[] args) {
    	String DB_CONNECTION = "jdbc:oracle:thin:@3.39.73.57:1534:DEEVSEDB";
        try {
        	/*
        	Connection connection = null;
        	try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection(DB_CONNECTION,"sreddy","D1g1talEnergy");
     
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            */
            //CSVLoader loader = new CSVLoader(connection);
        	CSVLoader loader = new CSVLoader();
            //D:\Tools\jira-cli-3.3.0-SNAPSHOT\test.csv 
            //loader.loadCSV("D:\\Tools\\jira-cli-3.3.0-SNAPSHOT\\test.csv", "USER_STORY", "ALSPA",true);
            //loader.loadCSV("D:\\Tools\\jira-cli-3.3.0-SNAPSHOT\\ICSP.csv", "USER_STORY", "ICSP",true);
            //loader.loadUserStory("C:\\AST\\JIRA_DATA\\PAM_{{ Sprint Nine}}.csv", "USER_STORY", "PAM",true);
            //loader.loadActiveSprints("C:\\AST\\JIRA_DATA\\ACTIVE_SPRINTS.csv",  true);
          //loader.loadCSV("D:\\Tools\\jira-cli-3.3.0-SNAPSHOT\\OVD.csv", "USER_STORY", "OVDIAG",true);
        	//loader.loadCSV("D:\\Tools\\jira-cli-3.3.0-SNAPSHOT\\OVD_TEST_sprint22.csv", "USER_STORY", "OVDIAG",true,"Sprint 22");
        	//latest to load stories
        	//loader.loadCSV("D:\\Tools\\jira-cli-3.3.0-SNAPSHOT\\vk_sp18.csv", "USER_STORY", "VK",true,"Sprint 18");
        	loader.loadQMData("C:\\QM_Reports\\NDT_Sprint5.csv");
        	//loader.loadCSV("D:\\Tools\\jira-cli-3.3.0-SNAPSHOT\\ovd_sp23.csv", "USER_STORY", "OVDIAG",true,"Sprint 23");
        	//loader.getJMeterResults("C:\\reports\\test.csv");
            //loader.loadVersions("D:\\Tools\\jira-cli-3.3.0-SNAPSHOT\\ICSP-Sprints.csv", "SPRINT_DETAILS", "ICSP",true); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}