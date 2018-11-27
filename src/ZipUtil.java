
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 閫氳繃Java鐨刏ip杈撳叆杈撳嚭娴佸疄鐜板帇缂╁拰瑙ｅ帇鏂囦欢
 * 
 * @author liujiduo
 * 
 */
public final class ZipUtil {

	private ZipUtil() {
		// empty
	}

	/**
	 * 鍘嬬缉鏂囦欢
	 * 
	 * @param filePath
	 *            寰呭帇缂╃殑鏂囦欢璺緞
	 * @return 鍘嬬缉鍚庣殑鏂囦欢
	 */
	public static File zip(String filePath) {
		File target = null;
		File source = new File(filePath);
		if (source.exists()) {
			// 鍘嬬缉鏂囦欢鍚�=婧愭枃浠跺悕.zip
			String zipName = source.getName() + ".zip";
			target = new File(source.getParent(), zipName);
			if (target.exists()) {
				target.delete(); // 鍒犻櫎鏃х殑鏂囦欢
			}
			FileOutputStream fos = null;
			ZipOutputStream zos = null;
			try {
				fos = new FileOutputStream(target);
				zos = new ZipOutputStream(new BufferedOutputStream(fos));
				// 娣诲姞瀵瑰簲鐨勬枃浠禘ntry
				addEntry("/", source, zos);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOUtil.closeQuietly(zos, fos);
			}
		}
		return target;
	}

	/**
	 * 鎵弿娣诲姞鏂囦欢Entry
	 * 
	 * @param base
	 *            鍩鸿矾寰�
	 * 
	 * @param source
	 *            婧愭枃浠�
	 * @param zos
	 *            Zip鏂囦欢杈撳嚭娴�
	 * @throws IOException
	 */
	private static void addEntry(String base, File source, ZipOutputStream zos)
			throws IOException {
		// 鎸夌洰褰曞垎绾э紝褰㈠锛�/aaa/bbb.txt
		String entry = base + source.getName();
		if (source.isDirectory()) {
			for (File file : source.listFiles()) {
				// 閫掑綊鍒楀嚭鐩綍涓嬬殑鎵�鏈夋枃浠讹紝娣诲姞鏂囦欢Entry
				addEntry(entry + "/", file, zos);
			}
		} else {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				byte[] buffer = new byte[1024 * 10];
				fis = new FileInputStream(source);
				bis = new BufferedInputStream(fis, buffer.length);
				int read = 0;
				zos.putNextEntry(new ZipEntry(entry));
				while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
					zos.write(buffer, 0, read);
				}
				zos.closeEntry();
			} finally {
				IOUtil.closeQuietly(bis, fis);
			}
		}
	}

	/**
	 * 瑙ｅ帇鏂囦欢
	 * 
	 * @param filePath
	 *            鍘嬬缉鏂囦欢璺緞
	 */
	public static void unzip(String filePath) {
		File source = new File(filePath);
		if (source.exists()) {
			ZipInputStream zis = null;
			BufferedOutputStream bos = null;
			try {
				zis = new ZipInputStream(new FileInputStream(source));
				ZipEntry entry = null;
				while ((entry = zis.getNextEntry()) != null
						&& !entry.isDirectory()) {
					File target = new File(source.getParent(), entry.getName());
					if (!target.getParentFile().exists()) {
						// 鍒涘缓鏂囦欢鐖剁洰褰�
						target.getParentFile().mkdirs();
					}
					// 鍐欏叆鏂囦欢
					bos = new BufferedOutputStream(new FileOutputStream(target));
					int read = 0;
					byte[] buffer = new byte[1024 * 10];
					while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
						bos.write(buffer, 0, read);
					}
					bos.flush();
				}
				zis.closeEntry();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOUtil.closeQuietly(zis, bos);
			}
		}
	}

	public static void main(String[] args) {
		String targetPath = "E:\\Win7澹佺焊";
		File file = ZipUtil.zip(targetPath);
		System.out.println(file);
		ZipUtil.unzip("F:\\Win7澹佺焊.zip");
	}
}