package com.lianliankan.xiaoyoulei.view;


public interface OnToolsChangeListener{
	public void onRefreshChanged(int count);
	public void onTipChanged(int count);
	public int addTime() ;	// endless add time .
	public int addScore() ;	// 
	public void setScore(int socre); 
}
