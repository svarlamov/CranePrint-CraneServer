package org.craneprint.craneserver.tcp;

import java.io.IOException;

import org.craneprint.craneserver.HandShake;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class SendHandShake {
	public static HandShake sendIt() throws IOException, ParseException{
		JSONObject obj = new JSONObject();
	    obj.put("type", RequestType.HAND_SHAKE_CODE);
	    obj.put("password", "password");
	    String resp = TCPThread.sendCommand(obj.toJSONString());
	    return new HandShake(resp);
	}
}
