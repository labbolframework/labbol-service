/**
 * 
 */
package com.labbol.service.convert;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 彭飞
 * @date 2019年7月25日下午3:20:13
 */
public class RequestPayBodyConvertFile extends AbstractRequestBodyConvertObject<File> {

	private String filePath;
	private String fileName;

	/**
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 */
	public RequestPayBodyConvertFile(String filePath, String fileName) {
		this.fileName = fileName;
		this.filePath = filePath;
	}

	@Override
	public File jsonToObject(String json) throws Exception {
		return base64ToFile(json, filePath, fileName);
	}

	/**
	 * 将base64类型的字符串转换为文件
	 * 
	 * @author PengFei
	 * @date 2019年7月25日下午3:50:31
	 * @param base64   base64
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @return 转换后的文件
	 */
	public static File base64ToFile(String base64, String filePath, String fileName) throws IOException {
		if (base64.contains("data:image")) {
			base64 = base64.substring(base64.indexOf(",") + 1);
		} else if (base64.contains("data:application")) {
			base64 = base64.substring(base64.indexOf(",") + 1);
		} else {
			base64 = base64.substring(base64.indexOf(",") + 1);
		}
		base64 = base64.toString().replace("\r\n", "");
		File file = null;
		// 创建文件目录
		File dir = new File(filePath);
		if (!dir.exists() && !dir.isDirectory()) {
			dir.mkdirs();
		}
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			byte[] bytes = java.util.Base64.getDecoder().decode(base64);
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return file;
	}

}
