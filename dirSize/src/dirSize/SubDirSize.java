package dirSize;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.TreeSet;
import java.nio.file.LinkOption;
import java.text.DecimalFormat;
import java.lang.Math;
import java.util.ArrayList;

public class SubDirSize {

	TreeSet<DirSizeElement> list;
	IgnoreFiles ig;

	public SubDirSize() {
		list = new TreeSet<DirSizeElement>();
		ig = new IgnoreFiles();
	}
	
	public TreeSet<DirSizeElement> getList() {
		return list;
	}
	
	public long getFileList(File curDir, long inSize) {
		ArrayList<String> ignoreList = ig.getIgnoreList();
		long accumulate = inSize;
		try {
			if (curDir == null) {
				throw new NullPointerException();
			} else {
				if (ignoreList.contains(curDir.getCanonicalPath())) {
					System.out.println("ignoring file "+curDir.getCanonicalPath());
				} else {
						if (curDir.isDirectory()) {
							File[] fileList = curDir.listFiles();
							if (fileList == null) {
								// no files to iterate on in this directory
							} else {
								for(File f : fileList){
									if (ignoreList.contains(f.getCanonicalPath())) {
										System.out.println("ignoring file "+f.getCanonicalPath());
									} else {
										boolean symbolic = (boolean) Files.getAttribute(f.toPath(),"basic:isSymbolicLink",LinkOption.NOFOLLOW_LINKS);
										if (symbolic) {
											System.out.println("ignoring symbolic link "+f.getCanonicalPath());
										} else if (!symbolic) {
											if(f.isDirectory()) {
												long dirSize = getFileList(f, 0);
												list.add(new DirSizeElement(dirSize,"directory: "+SubDirSize.toPrettyString(dirSize)+" "+f.getCanonicalPath(),true));
												accumulate = Math.addExact(accumulate, dirSize);
											} else if(f.isFile()){
												list.add(new DirSizeElement(Files.size(f.toPath()),"file: "+SubDirSize.toPrettyString(Files.size(f.toPath()))+" "+f.getCanonicalPath(),false));
												accumulate = Math.addExact(accumulate,Files.size(f.toPath()));
												
											}
										}
									}
								}
							}
						} else {
							list.add(new DirSizeElement(Files.size(curDir.toPath()),"file: "+SubDirSize.toPrettyString(Files.size(curDir.toPath()))+" "+curDir.getCanonicalPath(),false));
							accumulate = Math.addExact(accumulate,Files.size(curDir.toPath()));
						}
				}
			}
		} catch (IOException e) {
			System.err.println("Error in SubDirSize");
			e.printStackTrace();
		}
		return accumulate;
	}
	
	public static String toPrettyString(long in) {
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###,###");
		return formatter.format(in);
	}
}
