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
		File curCanonicalPath = null;
		boolean isDirectory = false;
		boolean isFile = false;
		String errString = "";
		long accumulate = inSize;
		boolean skipFile = false;
		boolean ignore;
		String canonPath;
		boolean symbolic;
		File[] fileList = null;
		symbolic = false;
		Long size = 0L;
		if (curDir == null) {
			throw new NullPointerException();
		} else {
			try {
				errString="getting Canonical File";
				curCanonicalPath = curDir.getCanonicalFile();
				errString="getting isDirectory()";
				isDirectory = curDir.isDirectory();
			} catch (IOException e) {
				System.err.println("Error DirSize.SubDirSize.getFileList(): "+errString);
				System.out.println("Skipping problem file "+curDir);
				skipFile = true;
			}
			if (!skipFile) {
				skipFile = false;
				if (ignoreList.contains(curCanonicalPath)) {
					System.out.println("Ignoring file in Ignore List "+curCanonicalPath);
				} else {
					if (isDirectory) {
						skipFile = false;
						try {
							fileList = curDir.listFiles();
						} catch (Exception e) {
							System.err.println("Error DirSize.subDirSize.getFileList(): Getting list of files for directory.");
							System.out.println("Skipping problem file "+curDir);
							skipFile = true;
						}
						if (skipFile || fileList == null) {
							skipFile = false;
							// ignore this directory
						} else {
							canonPath = "";
							for(File f : fileList) {
								System.out.println(f);
								try {
									errString = "Getting CanonicalPath";
									canonPath = f.getCanonicalPath();
								} catch (IOException e) {
									System.err.println("Error DirSize.subDirSize.getFileList(): "+errString);
									System.out.println("Skipping problem file "+f);
									skipFile = true;
								}
								if (skipFile) {
									skipFile = false;
									// do nothing
								} else if (ignoreList.contains(canonPath)) {
									System.out.println("Ignoring file in ignore List "+canonPath);
								} else {
									try {
										errString = "getting isSymbolic attribute";
										symbolic = (boolean) Files.getAttribute(f.toPath(),"basic:isSymbolicLink",LinkOption.NOFOLLOW_LINKS);
									} catch (IOException e) {
										System.err.println("Error DirSize.subDirSize.getFileList(): "+errString);
										System.out.println("Skipping problem file "+canonPath);
										skipFile = true;
									}
									if (skipFile) {
										skipFile = false;
										// do nothing
									} else if (symbolic) {
										System.out.println("ignoring symbolic link "+canonPath);
									} else {
										try {
											errString = "getting isDirectory()";
											isDirectory = f.isDirectory();
											isFile = f.isFile();
										} catch (Exception e) {
											System.err.println("Error DirSize.subDirSize.getFileList(): "+errString);
											System.out.println("Skipping problem file"+canonPath);
											skipFile = true;
										}
										if (skipFile) {
											// do nothing
										} else if(isDirectory) {
											long dirSize = getFileList(f, 0);
											list.add(new DirSizeElement(dirSize,"directory: "+SubDirSize.toPrettyString(dirSize)+" "+canonPath,true));
											accumulate = Math.addExact(accumulate, dirSize);
										} else if(isFile){
											try {
												errString = "f.toPath()";
												size = Files.size(f.toPath());
											} catch (IOException e) {
												System.err.println("Error DirSize.subDirSize.getFileList(): "+errString);
												System.out.println("Skipping problem file"+canonPath);
												skipFile = true;
											}
											if (skipFile) {
												
											} else {
												list.add(new DirSizeElement(size,"file: "+SubDirSize.toPrettyString(size)+" "+canonPath,false));
												accumulate = Math.addExact(accumulate,size);
											}
										}
									}
								}
							}
						}
					} else {
						try {
							list.add(new DirSizeElement(Files.size(curDir.toPath()),"file: "+SubDirSize.toPrettyString(Files.size(curDir.toPath()))+" "+curDir.getCanonicalPath(),false));
							accumulate = Math.addExact(accumulate,Files.size(curDir.toPath()));
						} catch (IOException e) {
							errString = "adding non directory high file";
							System.err.println("Error DirSize.subDirSize.getFileList(): "+errString);
							System.out.println("Skipping problem file"+curDir);
						}
					}
				}
			}
		}
		
		return accumulate;
	}
	
	public static String toPrettyString(long in) {
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###,###");
		return formatter.format(in);
	}
}
