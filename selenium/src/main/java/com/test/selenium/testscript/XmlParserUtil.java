package com.test.selenium.testscript;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParserUtil {

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
	    	    	ArrayList<String> dataValues = XmlParserUtil.getAllNodeValues(respHeader.item(0), data.item(i).getNodeName());
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
		//			"<people><person><email><p>test@hascode.com</p><o>test1@hascode.com</o></email><x>value0</x></person>"+
		//	 		  "<person><email><p>test@hascode.com</p><o>test2@hascode.com</o></email><x>value</x></person>"+
		//	 		  "<person><email><p>test@hascode.com</p><o>test3@hascode.com</o></email><x>value1</x></person></people>";
		//"<people><person><email>test@hascode.com</email><address>value</address></person>"+
		//"<person><email>test@hascode.com</email><address>value</address></person>"+
		 // "<person><email>test@hascode.com</email><address>value</address></person></people>";
		System.out.println("DOM = "+dom);
		int ind = dom.indexOf(">");
		String str = dom.substring(ind+1, dom.length());
		int ind1 = str.indexOf(">");
		String str1 = str.substring(1, ind1);
		System.out.println("index ="+ind+" SubC "+str1);
		Document myTestDocument = XMLUnit.buildTestDocument(str);
		NodeList nodes = myTestDocument.getElementsByTagName(str1);
		System.out.println("Parent = "+nodes.item(0).getNodeName());
		NodeList childNodes = nodes.item(0).getChildNodes();
		NodeList ourNode = childNodes;
		System.out.println("Child count = "+childNodes.getLength());
		System.out.println("Child Node = "+childNodes.item(0).getNodeName());
		System.out.println("Grand Child Node = "+childNodes.item(1).getChildNodes().item(0).getNodeName());
		System.out.println("Grand Child count = "+childNodes.item(1).getChildNodes().getLength());
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
								dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(cn).getNodeName());
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
						dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(j).getNodeName());
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
				dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(n).getNodeName());
				responseData.put(ourNode.item(n).getNodeName(), dataValues);
				/*
				for(String s:dataValues){
		    		System.out.println("Node = "+ourNode.item(j).getNodeName()+ "Value = "+s);
		    	}*/
			}
			
			
		}
		
		/*
		
		for(int cn=0;cn<ourNode.getLength();cn++){
			ArrayList<String> dataValues = new ArrayList<String>();
			//dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(cn).getNodeName());
			dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),ourNode.item(cn).getNodeName());
			responseData.put(ourNode.item(cn).getNodeName(), dataValues);
			for(String s:dataValues){
	    		System.out.println("Node = "+ourNode.item(cn).getNodeName()+ "Value = "+s);
	    	}
		}
		*/
		//for(int n=0;n<nodes.getLength();n++){
			/*
			for(int cn=0;cn<childNodes.getLength();cn++){
				ArrayList<String> dataValues = new ArrayList<String>();
				//ArrayList<String> dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0), childNodes.item(cn).getNodeName());
				//for(int ccn=0;ccn<childNodes.item(cn).getChildNodes().getLength();ccn++){
				System.out.println("childe child nodes "+childNodes.item(0).getChildNodes().item(0).getChildNodes().getLength());
				if(childNodes.item(0).getChildNodes().getLength()>1){
					
					if(childNodes.item(0).getChildNodes().item(0).getChildNodes().getLength()>0){
						System.out.println("child child Node "+childNodes.item(0).getChildNodes().item(0).getChildNodes().item(cn).getNodeName());
						dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),childNodes.item(0).getChildNodes().item(0).getChildNodes().item(cn).getNodeName());
						responseData.put(childNodes.item(0).getChildNodes().item(0).getChildNodes().item(cn).getNodeName(), dataValues);
						for(String s:dataValues){
				    		System.out.println("Node = "+childNodes.item(0).getChildNodes().item(0).getChildNodes().item(cn).getNodeName()+ "Value = "+s);
				    	}
					}else{
						dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),childNodes.item(0).getChildNodes().item(cn).getNodeName());
						responseData.put(childNodes.item(0).getChildNodes().item(cn).getNodeName(), dataValues);
						for(String s:dataValues){
				    		System.out.println("Node = "+childNodes.item(0).getChildNodes().item(cn).getNodeName()+ "Value = "+s);
				    	}
					}
					
				}else{
					dataValues = XmlParserUtil.getAllNodeValues(nodes.item(0),childNodes.item(cn).getNodeName());
					responseData.put(childNodes.item(cn).getNodeName(), dataValues);
					for(String s:dataValues){
			    		System.out.println("Node = "+childNodes.item(cn).getNodeName()+ "Value = "+s);
			    	}
				}
					
					
					
				//}
				
			}
			*/
		//}
		
		/*
		
	    System.out.println("child sise ="+soapBody.getLength());
	    NodeList respHeader = soapBody.item(0).getChildNodes();
	    System.out.println("child --> child sise ="+respHeader.getLength());
	    //if(respHeader.item(0).getNodeName().startsWith("ns2")){
	    	System.out.println("our node = "+respHeader.item(0).getChildNodes().item(0).getNodeName());
	    	//for(int k=0;k<n.item(0).getChildNodes().getLength();k++){
	    		//NodeList data = n.item(0).getChildNodes().item(k).getChildNodes();
	    	NodeList data = respHeader.item(0).getChildNodes().item(0).getChildNodes();
	    	    for(int i=0;i<respHeader.getLength();i++){
	    	    	//System.out.println("node = "+data.item(i).getNodeName()+ " --- child = "+XmlParserUtil.getNodeValue(data.item(i)));
	    	    	ArrayList<String> dataValues = XmlParserUtil.getAllNodeValues(soapBody.item(0), respHeader.item(i).getNodeName());
	    	    	responseData.put(data.item(i).getNodeName(), dataValues);
	    	    	for(String s:dataValues){
	    	    		System.out.println("Node = "+respHeader.item(i).getNodeName()+ "Value = "+s);
	    	    	}
	    	    }
	    	//}
	    //}
	     * 
	     */
		return responseData;
	}
}
