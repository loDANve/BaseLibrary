package com.wtm.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * ����洢���ļ���
 * 
 * @author wtm
 *
 */
public class ObjStorageTool {
	public static boolean saveObjToFile(Serializable obj, String filePath) {
		File file = new File(filePath);
		FileOutputStream out = null;
		ObjectOutputStream objOut = null;
		try {
            if (!file.exists()) file.createNewFile();
			out = new FileOutputStream(file);
			objOut = new ObjectOutputStream(out);
			objOut.writeObject(obj);
			objOut.flush();
			System.out.println("save object success!");
            return true;
		} catch (IOException e) {
			System.out.println("save object error!");
			e.printStackTrace();
            return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (objOut != null) {
				try {
					objOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * ���ļ���ȡһ����洢�Ķ���
	 * @param filePath
	 * @return
	 */
	public static Serializable readObjFromFile(String filePath) {
		Serializable temp = null;
		File file = new File(filePath);
        if (!file.exists()) return null;
		FileInputStream in = null;
		ObjectInputStream objIn = null;
		try {
			in = new FileInputStream(file);
			objIn = new ObjectInputStream(in);
			temp = (Serializable) objIn.readObject();
			System.out.println("read object success!");
		} catch (Exception e) {
			System.out.println("read object failed");
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (objIn != null) {
				try {
					objIn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return temp;
	}
}
