package reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.io.IOUtils; 

import org.json.*;

public class JsonReader {

    private String filename = null;
    private JSONObject jsonObject = null;

    public JsonReader(String fname) {
	filename = fname;
	InputStream is;
	try {
	    is = new FileInputStream(fname);
	    String jsonTxt = IOUtils.toString(is, "UTF-8");
	    jsonObject = new JSONObject(jsonTxt);
	    Iterator<String> keyIt= jsonObject.keys();
	    System.out.println("wut wut");
	    while(keyIt.hasNext()) {
		    String key = keyIt.next();
		    System.out.println(key);
		    // we have a list of keys 
	}

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
        
    }

    public void getNextDocumentId() {
	if (filename == null) {
	    throw new IllegalStateException("You haven't specified which JSON file to read\n");
	}
    }
}
