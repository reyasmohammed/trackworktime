/*
 * This file is part of TrackWorkTime (TWT).
 * 
 * TWT is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * TWT is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with TWT. If not, see <http://www.gnu.org/licenses/>.
 */
package org.zephyrsoft.trackworktime.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

import org.zephyrsoft.trackworktime.util.DateTimeUtil;
import org.zephyrsoft.trackworktime.util.Logger;

/**
 * Can write files on external storage.
 * 
 * @author Mathis Dirksen-Thedens
 */
public class FileStorage {

	public static File writeFile(String subDirectory, String fileNamePrefix, String fileNameSuffix, byte[] fileContent) {
		File externalStorageDirectory = Environment.getExternalStorageDirectory();
		if (!isExternalStorageWritable()) {
			Logger.error("external storage {0} is not writable", externalStorageDirectory);
			return null;
		}
		File targetDirectory;
		if (subDirectory != null && subDirectory.length() > 0) {
			targetDirectory = new File(externalStorageDirectory, subDirectory);
		} else {
			targetDirectory = externalStorageDirectory;
		}
		if (!targetDirectory.isDirectory() && !targetDirectory.mkdirs()) {
			Logger.error("directory {0} could not be created", targetDirectory);
			return null;
		}
		String timeStamp = DateTimeUtil.getCurrentDateTime().format("YYYY-MM-DD-hh-mm-ss");
		String fileName = fileNamePrefix + "-generated-at-" + timeStamp + fileNameSuffix;
		File file = new File(targetDirectory, fileName);
		return writeFile(fileContent, file);
	}

	private static File writeFile(byte[] fileContent, File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(fileContent);
		} catch (Exception e) {
			Logger.error("file {0} could not be written", file);
			return null;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
		return file;
	}

	private static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

}
