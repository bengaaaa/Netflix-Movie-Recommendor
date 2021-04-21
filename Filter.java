package movieApp;

import java.io.File;
import java.io.FilenameFilter;

public class Filter implements FilenameFilter{
		@Override
		public boolean accept(File f, String name) {
			return name.endsWith(".txt");
		}
}
