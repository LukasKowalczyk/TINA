package de.tina.knowledge;

import java.io.File;
import java.io.FilenameFilter;

public class JsonFilenameFilter implements FilenameFilter {
	public static final String JSON_EXTENSION = ".json";

	@Override
	public boolean accept(File arg0, String arg1) {
		return arg1.endsWith(JSON_EXTENSION);
	}

}
