package generics;

import java.io.FileInputStream;
import java.util.Properties;

public class Property {
public static String getProperyValue(String filepath,String key)
{
	String value="";
	Properties ppt=new Properties();
	try{
	ppt.load(new FileInputStream(filepath));
	value=ppt.getProperty(key);
	}
	catch(Exception e){}
	return value;
	
}
}
