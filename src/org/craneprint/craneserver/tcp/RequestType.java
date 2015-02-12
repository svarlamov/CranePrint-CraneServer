package org.craneprint.craneserver.tcp;

public class RequestType {
	public static final int HAND_SHAKE_CODE = 0;
	public static final int FILE_CODE = 1;
	public static final int GET_NEW_JOB = 2;
	public static final int JOB_COMPLETE = 3;
	public static final int QUEUE_EMPTY = 4;
}
