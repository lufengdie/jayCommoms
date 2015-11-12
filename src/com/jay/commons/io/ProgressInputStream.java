package com.jay.commons.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProgressInputStream extends FilterInputStream {

	private final ProgressListener listener;
    private long transferred = 0;
	
	public ProgressInputStream(final InputStream in, final ProgressListener listener) {
        super(in);
        this.listener = listener;
        this.transferred = 0;
    }
	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		int nread = in.read();
		readCount(nread);
		return nread;
	}
	@Override
	public int read(byte[] buffer) throws IOException {
		// TODO Auto-generated method stub
		int read = in.read(buffer);
        readCount(read);
        return read;
	}
	@Override
	public int read(byte[] buffer, int byteOffset, int byteCount)
			throws IOException {
		// TODO Auto-generated method stub
		int read = in.read(buffer, byteOffset, byteCount);
        readCount(read);
        return read;
	}
	@Override
	public long skip(long byteCount) throws IOException {
		// TODO Auto-generated method stub
		long skip = in.skip(byteCount);
        readCount(skip);
        return skip;
	}
	private void readCount(long read) {
        if (read > 0) {
            this.transferred += read;
            this.listener.transferred(this.transferred);
        }
    }

}