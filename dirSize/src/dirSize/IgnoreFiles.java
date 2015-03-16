package dirSize;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

/* This class is designed to allow certain directories or files to be ignored by the DirSize utility */

public class IgnoreFiles {
	
	ArrayList<String> ignoreList;
	String listFileName;
	
	public IgnoreFiles() {
		listFileName = "C:\\Users\\JimPC\\Documents\\bin\\DirSizeIgnoreList.txt";
		ignoreList = new ArrayList<String>();
		BufferedReader br = null;
		String ioStatus = "Opening ignoreList File";
		try {
			br = new BufferedReader(new FileReader(new File(listFileName)));
			ioStatus = "Reading ignoreList File";
			
			for (String in = br.readLine(); in != null; in = br.readLine()) {
				ignoreList.add(in);
			}
		} catch (IOException e) {
			System.err.println("Error "+ioStatus+" "+listFileName);
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				System.err.println("Error closing: ignoreList "+listFileName);
			}
		}
		
	}

	public boolean contains(String inString) {
		if (ignoreList.contains(inString)) {
			return true;
		} else {
			return false;
		}
	}
	
	public ArrayList<String> getIgnoreList() {
		return ignoreList;
	}
	
}
