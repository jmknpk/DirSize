import java.io.File;
import java.util.Iterator;
import java.util.TreeSet;

import dirSize.DirSizeElement;
import dirSize.SubDirSize;


public class DirSize {

	public static void main (String[] args) {
		SubDirSize ds = new SubDirSize();
		File abstractPath;
		if (args != null && args.length > 0) {
			abstractPath = new File(args[0]);
		} else {
			abstractPath = new File(".");
		}
		File startPath = abstractPath.getAbsoluteFile();
		long j = ds.getFileList(startPath, 0);
		TreeSet<DirSizeElement> list = ds.getList();
		Iterator<DirSizeElement> iter = list.descendingIterator();
		while (iter.hasNext()) {
			System.out.println(iter.next().getText());
		}
		
	}

}
