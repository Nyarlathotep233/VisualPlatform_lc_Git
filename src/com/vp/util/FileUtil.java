package com.vp.util;

import java.io.File;
import java.io.FileWriter;

import javax.servlet.http.HttpServletRequest;

/**
 * 常用文件实用类
 * @author admin
 *
 */
public class FileUtil {
	/**
	 * 根据读取的文件名，数据字符串和想要生成的文件名
	 * @param dataStr
	 * @param writeName
	 */
	public static void writeFileByDataAndFileName(HttpServletRequest request, String dataStr, String writeName){
		try {
			File file = new File(request.getSession().getServletContext().getRealPath("/") + "operationFiles\\" + writeName);
			FileWriter fw = new FileWriter(file);
			fw.write(dataStr);
			fw.close();
			System.out.println(writeName + " 文件生成成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
