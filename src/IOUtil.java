

import java.io.Closeable;
import java.io.IOException;

/**
 * IO娴佸伐鍏风被
 * 
 * @author liujiduo
 * 
 */
public class IOUtil {
	/**
	 * 鍏抽棴涓�涓垨澶氫釜娴佸璞�
	 * 
	 * @param closeables
	 *            鍙叧闂殑娴佸璞″垪琛�
	 * @throws IOException
	 */
	public static void close(Closeable... closeables) throws IOException {
		if (closeables != null) {
			for (Closeable closeable : closeables) {
				if (closeable != null) {
					closeable.close();
				}
			}
		}
	}

	/**
	 * 鍏抽棴涓�涓垨澶氫釜娴佸璞�
	 * 
	 * @param closeables
	 *            鍙叧闂殑娴佸璞″垪琛�
	 */
	public static void closeQuietly(Closeable... closeables) {
		try {
			close(closeables);
		} catch (IOException e) {
			// do nothing
		}
	}

}
