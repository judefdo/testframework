package com.test.selenium.testscript;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.test.selenium.testscript.*;
import org.custommonkey.xmlunit.XMLUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ResponseParserUtil {

	public static ArrayList<String> getAllNodeValues(Node node, String childNodeName) {
		
		ArrayList<String> values = new ArrayList<String>();
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			NodeList targetChildren = element.getElementsByTagName(childNodeName);
			
			for (int i = 0; i < targetChildren.getLength(); i++) {
				Node child = targetChildren.item(i);
				values.add(getNodeValue(child));
			}
		}
		
		return values;
	}
	
	public static String getNodeValue(Node node, String childNodeName) {
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			NodeList targetChildren = element.getElementsByTagName(childNodeName);
			
			return getNodeValue(targetChildren.item(0));
		}
		
		return null;
	}
	
	public static String getNodeValue(Node node) {
		String name ="";
		Node child = node.getFirstChild();
        if(child == null) {
            // null handling
            name = "";
            return name;
         }
        else {
        	return node.getFirstChild().getNodeValue();
        }
	}
	
	public static int getResponseSize(String domString) throws JSONException{
		int size =0;
		JSONArray array = null;
		//System.out.println("Resp = "+domString);
		domString = "{\"source\":"+domString+"}";
		String testString = "{\"source\":[{\"id\":\"66\",\"name\":\"Trilliant\"},{\"id\":\"77\",\"name\":\"CISCO\"}]}";
		//domString = testString;
		if(domString.startsWith("[")){
			array = new JSONArray(domString);
			size = array.length();
		}else if(domString.startsWith("{")){
			String objString = domString.substring(domString.indexOf("["), domString.length());
			//System.out.println("str ="+objString);
			JSONObject myjson = new JSONObject(domString);
			array = myjson.getJSONArray("source"); 
			size = array.length();
		}
		return size;
	}
	
	public static HashMap<String, ArrayList<String>> getRESTJsonResponseData(String dom) {
		HashMap<String, ArrayList<String>> responseData = new HashMap<String,ArrayList<String>>();
		//dom="{\"person\":"+dom+"}";
		String startString="";
		if(dom.startsWith("[")){
			dom="{\"person\":"+dom;
			startString = "person";
		}else{
			startString = "customfield_12481";
		}
		
		try{
			JSONObject myjson = new JSONObject(dom);
			//check whether it has arrays
			if(dom.contains("[")){
				//TO DO
				//JSONArray the_json_array = myjson.getJSONArray("person");
				JSONArray the_json_array = myjson.getJSONArray(startString);
				int size = the_json_array.length();
				System.out.println("size = "+size);
			    ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
			    ArrayList<String> keys = new ArrayList<String>();
			    for (int i = 0; i < size; i++) {
			        JSONObject another_json_object = the_json_array.getJSONObject(i);
			            arrays.add(another_json_object);
			            Iterator it = another_json_object.keys();
			            while(it.hasNext()){
							String key = it.next().toString();
							keys.add(key);
							String status=another_json_object.get(key).toString();
							System.out.println("Key = "+key+" status= "+status);
						}
						
			    }
			    for(String s:keys){
			    	ArrayList<String> values = new ArrayList<String>();
			    	for(JSONObject obj: arrays){
			    		System.out.println("all values = "+obj.get(s).toString());
			    		values.add(obj.get(s).toString());
				    }
			    	responseData.put(s, values);
			    }
			    
			    //Finally
			    JSONObject[] jsons = new JSONObject[arrays.size()];
			    arrays.toArray(jsons);
			}else{
				JSONArray nameArray = myjson.names();
				JSONArray valArray = myjson.toJSONArray(nameArray);
				for(int i=0;i<valArray.length();i++)
	            {
					ArrayList<String> values = new ArrayList<String>();
					String p = nameArray.getString(i) + "," + valArray.getString(i);
	                System.out.println("Value = "+p);
	                values.add(valArray.getString(i));
	                responseData.put(nameArray.getString(i), values);
	            }
			}
		}catch (JSONException e) {
		    // Could not find the database driver
			System.out.println("Failed to Parse JSON response ");
			e.printStackTrace();
		} 
		 
		
		return responseData;
	}
	
	public static HashMap<String, ArrayList<String>> getResponseData(Document dom){
		HashMap<String, ArrayList<String>> responseData = new HashMap<String,ArrayList<String>>();
		NodeList soapBody = dom.getElementsByTagName("soap:Body");
	    System.out.println("child sise ="+soapBody.getLength());
	    NodeList respHeader = soapBody.item(0).getChildNodes();
	    System.out.println("child --> child sise ="+respHeader.getLength());
	    if(respHeader.item(0).getNodeName().startsWith("ns2")){
	    	System.out.println("our node = "+respHeader.item(0).getChildNodes().item(0).getNodeName());
	    	//for(int k=0;k<n.item(0).getChildNodes().getLength();k++){
	    		//NodeList data = n.item(0).getChildNodes().item(k).getChildNodes();
	    	NodeList data = respHeader.item(0).getChildNodes().item(0).getChildNodes();
	    	    for(int i=0;i<data.getLength();i++){
	    	    	//System.out.println("node = "+data.item(i).getNodeName()+ " --- child = "+XmlParserUtil.getNodeValue(data.item(i)));
	    	    	ArrayList<String> dataValues = com.test.selenium.testscript.XmlParserUtil.getAllNodeValues(respHeader.item(0), data.item(i).getNodeName());
	    	    	responseData.put(data.item(i).getNodeName(), dataValues);
	    	    	for(String s:dataValues){
	    	    		System.out.println("Node = "+data.item(i).getNodeName()+ "Value = "+s);
	    	    	}
	    	    }
	    	//}
	    }
		return responseData;
	}
	
	public static HashMap<String, ArrayList<String>> getRESTResponseData(String dom) throws SAXException, IOException{
		HashMap<String, ArrayList<String>> responseData = new HashMap<String,ArrayList<String>>();
		//dom ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
		//		"<people><person><email><p>test9@hascode.com</p><o>test1@hascode.com</o></email><x>value0</x></person>"+
		//	 		  "<person><email><p>test8@hascode.com</p><o>test2@hascode.com</o></email><x>value</x></person>"+
		//	 		  "<person><email><p>test7@hascode.com</p><o>test3@hascode.com</o></email><x>value1</x></person></people>";
		//"<people><person><email>test@hascode.com</email><address>value</address></person>"+
		//"<person><email>test@hascode.com</email><address>value</address></person>"+
		 // "<person><email>test@hascode.com</email><address>value</address></person></people>";
		System.out.println("DOM = "+dom);
		//Document myTestDocument = XMLUnit.buildTestDocument(str);
		Document myTestDocument = XMLUnit.buildTestDocument(dom);
		
		NodeList nodeList = myTestDocument.getElementsByTagName("*");
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node node = nodeList.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	            // do something with the current element
	            System.out.println(node.getNodeName());
	            if(node.getFirstChild()!=null && node.getFirstChild().getNodeType()==Node.TEXT_NODE){
	            	ArrayList<String> dataValues = new ArrayList<String>();
		    		//dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(cn).getNodeName());
		    		dataValues = com.test.selenium.testscript.XmlParserUtil.getAllNodeValues(nodeList.item(0),node.getNodeName());
		    		responseData.put(node.getNodeName(), dataValues);
		    		for(String s:dataValues){
		            	System.out.println("Data == "+s);
		            }
	            }    
	        }
	    }
	    
	    
		//responseData = getLastNode(nodes);
		/*
		ArrayList<String> dataValues = new ArrayList<String>();
		//dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(cn).getNodeName());
		dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),n.getNodeName());
		responseData.put(n.getNodeName(), dataValues);
		*/
		return responseData;
	}
	
	public static HashMap<String, ArrayList<String>> getLastNode(NodeList nodeList){
		Node lastNode=null;
		HashMap<String, ArrayList<String>> responseData = new HashMap<String,ArrayList<String>>();
		for(int i=0; i<nodeList.getLength(); i++){
				System.out.println("i = "+i);
		      //print current node & values
		      Node childNode = nodeList.item(i);
		      if(childNode.getNodeType()==Node.ELEMENT_NODE){
		          System.out.print("  " + childNode.getNodeName());
		          if(childNode.getFirstChild()!=null && childNode.getFirstChild().getNodeType()==Node.TEXT_NODE){
		              System.out.print(" = " + childNode.getFirstChild().getNodeValue());
		              
		              lastNode= childNode;
		          }
			          System.out.println();
		              //return lastNode;
		      }

		      //recursively iterate through child nodes
		      NodeList children = childNode.getChildNodes();
		      //if (children != null)
		      if (children.getLength()>1)
		      {
		    	  ArrayList<String> dataValues = new ArrayList<String>();
	      		  dataValues = com.test.selenium.testscript.XmlParserUtil.getAllNodeValues(childNode,childNode.getFirstChild().getNodeName());
	      		for(String s:dataValues){
	            	System.out.println("Data == "+s);
	            }
		    	  System.out.println("i am in recursion = ");
		    	  responseData=getLastNode(children);
		      }
		     
		    }
		
			return responseData;
	}

	public static void extra(String dom) throws SAXException, IOException{
		HashMap<String, ArrayList<String>> responseData = new HashMap<String,ArrayList<String>>();
		int ind = 0;
		String str = dom.substring(ind+1, dom.length());
		int ind1 = str.indexOf(">");
		String str1 = str.substring(1, ind1);
		Document myTestDocument = XMLUnit.buildTestDocument(str);
		NodeList nodes = myTestDocument.getElementsByTagName(str1);
		System.out.println("Parent = "+nodes.item(0).getNodeName());
		NodeList childNodes = nodes.item(0).getChildNodes();
		NodeList ourNode = childNodes;
		for(int n=0;n<childNodes.getLength();n++){
			System.out.println("ITEM ="+childNodes.item(n).getNodeName());
			if(childNodes.item(n).getChildNodes().getLength()>1){
				for(int j=0;j<childNodes.item(n).getChildNodes().getLength();j++){
					System.out.println("  CHILD ITEM ="+childNodes.item(n).getChildNodes().item(j).getNodeName());
					if(childNodes.item(n).getChildNodes().item(j).getChildNodes().getLength()>1){
						for(int cn=0;cn<childNodes.item(n).getChildNodes().item(j).getChildNodes().getLength();cn++){
							System.out.println("      GRAND CHILD ITEM ="+childNodes.item(n).getChildNodes().item(j).getChildNodes().item(cn).getNodeName());
							if(childNodes.item(n).getChildNodes().item(j).getChildNodes().item(cn).getChildNodes().getLength()>1){
								System.out.println("yes childs");
								if(childNodes.item(n).getChildNodes().item(cn).getChildNodes().item(cn).getChildNodes().item(0).hasChildNodes()){
									System.out.println("yes childs");
								}else{
									ourNode = childNodes.item(n).getChildNodes().item(j).getChildNodes().item(cn).getChildNodes();
								}
							}else{
								System.out.println("No childs -- hello");
								ourNode = childNodes.item(n).getChildNodes().item(j).getChildNodes();
								System.out.println("Our node ="+ourNode.item(cn).getNodeName());
								ArrayList<String> dataValues = new ArrayList<String>();
								//dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(cn).getNodeName());
								dataValues = com.test.selenium.testscript.XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(cn).getNodeName());
								responseData.put(ourNode.item(cn).getNodeName(), dataValues);
								/*
								for(String s:dataValues){
						    		System.out.println("Node = "+ourNode.item(cn).getNodeName()+ "Value = "+s);
						    	}
						    	*/
							}
						}
						
					}else{
						System.out.println("No childs");
						ourNode = childNodes.item(j).getChildNodes();
						//ourNode = childNodes;
						System.out.println("Our node ="+ourNode.item(j).getNodeName());
						ArrayList<String> dataValues = new ArrayList<String>();
						//dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(cn).getNodeName());
						dataValues = com.test.selenium.testscript.XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(j).getNodeName());
						responseData.put(ourNode.item(j).getNodeName(), dataValues);
						/*
						for(String s:dataValues){
				    		System.out.println("Node = "+ourNode.item(j).getNodeName()+ "Value = "+s);
				    	}*/
					}
				}
			}else{
				System.out.println("No childs");
				ourNode = childNodes;
				System.out.println("Our node ="+ourNode.item(n).getNodeName());
				ArrayList<String> dataValues = new ArrayList<String>();
				//dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(cn).getNodeName());
				dataValues = com.test.selenium.testscript.XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(n).getNodeName());
				responseData.put(ourNode.item(n).getNodeName(), dataValues);
				/*
				for(String s:dataValues){
		    		System.out.println("Node = "+ourNode.item(j).getNodeName()+ "Value = "+s);
		    	}*/
			}
		}
	}

}
