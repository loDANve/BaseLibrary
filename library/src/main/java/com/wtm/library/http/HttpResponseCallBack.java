package com.wtm.library.http;

public interface HttpResponseCallBack {

	/**
	 * �������,���ֽ����鷵������
	 * 
	 * @param obj
	 */
	public void complete(byte[] data);

	/**
	 * ����ʱ��,��ȡ���صĲ���
	 * 
	 * @param count
	 *            Ҫ�������ݵ����ֽڳ���
	 * @param current
	 *            �Ѿ����ص��ֽڳ���
	 * @param currentDownLoadScale
	 *            �Ѿ����صİٷֱȿ̶�
	 */
	public void onLoad(long count, long current, float currentDownLoadScale);

	/**
	 * 
	 * @param e ������쳣����
	 * @param msg ������Ϣ
	 * @param responseCode ��Ӧ��
	 */
	public void error(Exception e, Object msg, int responseCode);
	
	public void onStart();

}