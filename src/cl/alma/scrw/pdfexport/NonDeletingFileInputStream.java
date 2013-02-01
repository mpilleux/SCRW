package cl.alma.scrw.pdfexport;

import java.io.File; 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This input stream does not delete the given file when the InputStream is closed;
 * intended to be used with temporary files.
 * 
 */
class NonDeletingFileInputStream extends FileInputStream {

	/** The file. */
	protected File file = null;

	/**
	 * Instantiates a new deleting file input stream.
	 * 
	 * @param file
	 *            the file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public NonDeletingFileInputStream(final File file)
			throws FileNotFoundException {
		super(file);
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FileInputStream#close()
	 */
	@Override
	public void close() throws IOException {
		super.close();
		//file.delete();
	}
}