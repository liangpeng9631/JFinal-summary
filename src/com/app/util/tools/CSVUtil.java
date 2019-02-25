package com.app.util.file;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * 操作csv文件工具类
 * @author liangpeng
 *
 */
public class CSVUtil {
	
	/**
	 * 下载csv文件
	 * @param rowList
	 * @param fileName
	 * @param response
	 * @throws IOException
	 */
	public static void downloadCSV(List<List<Object>> rowList, String fileName, HttpServletResponse response)throws IOException{
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO_8859_1"));
		response.setContentType("application/csv charset=utf-8");
		
		OutputStream os = response.getOutputStream();
		try {
			writeCSVData(rowList, os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}
	
	public static void writeCSVData(List<List<Object>> rowList, OutputStream os)throws IOException{
		StringBuffer sb = new StringBuffer();
		for (List<Object> row : rowList) {
			for (Object data : row) {
				sb.append("\"").append(data).append("\",");
			}
			sb.append("\n");
		}
		os.write(sb.toString().getBytes("utf-8"));
	}
}
