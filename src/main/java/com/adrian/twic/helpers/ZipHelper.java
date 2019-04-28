package com.adrian.twic.helpers;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

/**
 * Holds utility methods for working with zipped files.
 */
public class ZipHelper {
	private static final int BUFFER_SIZE = 4096;

	public static void unzip(byte[] data, String dirName) throws IOException {
		var destDir = new File(dirName);

		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		try (final var zipIn = new ZipInputStream(new ByteArrayInputStream(data))) {
			var entry = zipIn.getNextEntry();

			while (entry != null) {
				String filePath = dirName + File.separator + entry.getName();

				if (!entry.isDirectory()) {
					// if the entry is a file, extracts it
					extractFile(zipIn, filePath);
				} else {
					// if the entry is a directory, make the directory
					File dir = new File(filePath);
					dir.mkdir();
				}

				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
		}
	}

	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		try (var bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
			byte[] bytesIn = new byte[BUFFER_SIZE];
			var read = 0;
			while ((read = zipIn.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
		}
	}
}
