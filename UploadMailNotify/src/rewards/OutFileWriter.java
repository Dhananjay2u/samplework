/*
 * Created on May 10, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author in_local
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

package rewards;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class OutFileWriter {

	private PrintWriter _fileWriter;

	public OutFileWriter() {

		_fileWriter = null;

	}

	public OutFileWriter(String pSrcFileName, String pFileName)
			throws Exception, Exception {

		if (pSrcFileName == null || pFileName == null) {

			throw new Exception("Invalid file given");

		}

		Calendar lCal = Calendar.getInstance();

		String lOutputFileName = null;

		if (pSrcFileName.lastIndexOf("\\") != -1) {

			lOutputFileName = pSrcFileName.substring(0, (pSrcFileName
					.lastIndexOf("\\") + 1))
					+ pFileName
					+ lCal.get(Calendar.DAY_OF_MONTH)
					+ "-"
					+ lCal.get(Calendar.MONTH)
					+ "-"
					+ lCal.get(Calendar.YEAR)
					+ ".txt";

		} else {

			lOutputFileName = pFileName + lCal.get(Calendar.DAY_OF_MONTH) + "-"
					+ lCal.get(Calendar.MONTH) + "-" + lCal.get(Calendar.YEAR)
					+ ".txt";

		}

		try {

			System.out.println(lOutputFileName);

			_fileWriter = new PrintWriter(new FileOutputStream(lOutputFileName));

		} catch (IOException ie) {

			System.out.println(ie.getMessage());
			throw new Exception("exception");

		} catch (Exception ex) {

			System.out.println(ex.getMessage());
			throw new Exception("exception");

		}

	}

	public OutFileWriter(String pSrcFileName, String pFileName,
			String pDateStr) throws Exception {

		if (pSrcFileName == null || pFileName == null || pDateStr == null) {

			throw new Exception("exception");

		}

		String lOutputFileName = null;

		if (pSrcFileName.lastIndexOf("\\") != -1) {

			lOutputFileName = pSrcFileName.substring(0, (pSrcFileName
					.lastIndexOf("\\") + 1))
					+ pFileName + pDateStr + ".txt";

		} else {

			lOutputFileName = pFileName + pDateStr + ".txt";

		}

		lOutputFileName = lOutputFileName.replace('/', '-');

		try {

			System.out.println(lOutputFileName);

			_fileWriter = new PrintWriter(new FileOutputStream(lOutputFileName));

		} catch (IOException ie) {

			System.out.println(ie.getMessage());
			throw new Exception("exception");

		} catch (Exception ex) {

			System.out.println(ex.getMessage());
			throw new Exception("exception");

		}

	}

	public OutFileWriter(String pSrcFileName, boolean append) throws Exception {
		if (pSrcFileName == null) {
			throw new Exception("exception");
		}
		try {
			_fileWriter = new PrintWriter(new FileOutputStream(pSrcFileName, append));
		} catch (IOException ie) {
			System.out.println(ie.getMessage());
			throw new Exception("exception");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new Exception("exception");
		}
	}

	public void writeLine(String pStr) {

		_fileWriter.println(pStr);

	}

	public void closeFileWriter() throws Exception {

		try {

			if (_fileWriter != null) {
				_fileWriter.close();
			}

		} catch (Exception ex) {

			System.out.println(ex.getMessage());
			throw new Exception("exception");

		}

	}

}

