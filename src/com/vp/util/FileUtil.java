package com.vp.util;

import java.io.File;
import java.io.FileWriter;

import javax.servlet.http.HttpServletRequest;

/**
 * �����ļ�ʵ����
 * @author admin
 *
 */
public class FileUtil {
	/**
	 * ���ݶ�ȡ���ļ����������ַ�������Ҫ���ɵ��ļ���
	 * @param dataStr
	 * @param writeName
	 */
	public static void writeFileByDataAndFileName(HttpServletRequest request, String dataStr, String writeName){
		try {
			File file = new File(request.getSession().getServletContext().getRealPath("/") + "operationFiles\\" + writeName);
			FileWriter fw = new FileWriter(file);
			fw.write(dataStr);
			fw.close();
			System.out.println(writeName + " �ļ����ɳɹ���");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
