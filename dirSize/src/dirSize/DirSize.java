package dirSize;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.TreeSet;
import java.util.Iterator;

public class DirSize {

	TreeSet<DirSizeElement> list;

	public DirSize() {
		list = new TreeSet<DirSizeElement>();
	}
	
	public TreeSet<DirSizeElement> getList() {
		return list;
	}
	
	public static void main(String[]args)
	{
		DirSize ds = new DirSize();
		File abstractPath = new File(".");
		File startPath = abstractPath.getAbsoluteFile();
		long j = ds.getFileList(startPath, 0);
		TreeSet<DirSizeElement> list = ds.getList();
		Iterator<DirSizeElement> iter = list.descendingIterator();
		while (iter.hasNext()) {
			System.out.println(iter.next().getText());
		}
		
	}
	
	public long getFileList(File curDir, long inSize) {
		long accumulate = inSize;
		try {
			if (curDir.isDirectory()) {
				File[] fileList = curDir.listFiles();
				for(File f : fileList){
					if(f.isDirectory()) {
						long dirSize = getFileList(f, accumulate);
						list.add(new DirSizeElement(dirSize,"directory: "+Long.toString(dirSize)+" "+f.getCanonicalPath(),true));
						accumulate = accumulate + dirSize;
					} else if(f.isFile()){
						list.add(new DirSizeElement(Files.size(f.toPath()),"file: "+Long.toString(Files.size(f.toPath()))+" "+f.getCanonicalPath(),false));
						accumulate = accumulate + Files.size(f.toPath());
						
					}
				}
			} else {
				list.add(new DirSizeElement(Files.size(curDir.toPath()),"file: "+Long.toString(Files.size(curDir.toPath()))+" "+curDir.getCanonicalPath(),false));
				accumulate = accumulate + Files.size(curDir.toPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accumulate;
	}
}
