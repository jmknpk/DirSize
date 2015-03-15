package dirSize;

import java.lang.Comparable;

public class DirSizeElement implements Comparable<DirSizeElement> {
	long size;
	String text;
	boolean isDirectory;
	
	public DirSizeElement(long l, String s, boolean i) {
		size = l;
		text = s;
		isDirectory = i;
	}

	public boolean equals(DirSizeElement inD) {
		if (inD == null) {
			throw new NullPointerException();
		} else {
			if (size == inD.getSize() && text.equals(inD.getText()) && isDirectory == inD.isDirectory()) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public int hashCode() {
		return (int) size;
	}
	
	@Override public int compareTo(DirSizeElement inD) {
		if (inD == null) {
			throw new NullPointerException();
		} else {
			if (equals(inD)) {
				return 0;
			} else if (isDirectory == true && inD.isDirectory() == false) {
				return 1;
			} else if (isDirectory == false && inD.isDirectory() == true) {
				return -1;
			} else if (size > inD.getSize()) {
				return 1;
			} else if (size < inD.getSize()) {
				return -1;
			} else if (text.compareTo(inD.getText()) > 1) {
				return 1;
			} else if (text.compareTo(inD.getText()) < 1) {
				return -1;
			} else {
				return -0;
			}
		}
	}
	
	public long getSize() {
		return size;
	};
	
	public String getText() {
		return text;
	}

	public boolean isDirectory() {
		return isDirectory;
	}
}
