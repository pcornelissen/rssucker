package net.rssucker;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

class UrlDownload {
	private final static int size = 1024;

	private static void fileUrl(String fAddress, String
			localFileName, String destinationDir) {
		OutputStream outStream = null;
		URLConnection uCon;

		InputStream is = null;
		try {
			URL Url;
			byte[] buf;
			int ByteRead, ByteWritten = 0;
			Url = new URL(fAddress);
			outStream = new BufferedOutputStream(new
					FileOutputStream(destinationDir + File.separatorChar + localFileName));

			uCon = Url.openConnection();
			is = uCon.getInputStream();
			buf = new byte[size];
			while ((ByteRead = is.read(buf)) != -1) {
				outStream.write(buf, 0, ByteRead);
				ByteWritten += ByteRead;
			}
			System.out.println
					("Downloaded file! Name:\"" + localFileName + "\"\nNo ofbytes :" + ByteWritten);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) is.close();
				if (outStream != null) outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void fileDownload(String fAddress, String destinationDir) {

		int slashIndex = fAddress.lastIndexOf('/');
		int periodIndex = fAddress.lastIndexOf('.');

		String fileName = fAddress.substring(slashIndex + 1);

		if (periodIndex >= 1 && slashIndex >= 0
				&& slashIndex < fAddress.length() - 1) {
			fileUrl(fAddress, fileName, destinationDir);
		} else {
			System.err.println("path or file name.");
		}
	}
}
