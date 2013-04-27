package define.oper.body.req;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgSensQryByRgnReq_S {
short usRgnIdx;       // ¶ÔÏóË÷Òý

	
	public MsgSensQryByRgnReq_S(short usRgnIdx) {
		super();
		this.usRgnIdx = usRgnIdx;
	}
	

	public MsgSensQryByRgnReq_S() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static short getSize(){
		return 2;
	}

	public short getUsIdx() {
		return usRgnIdx;
	}

	public void setUsIdx(short usRgnIdx) {
		this.usRgnIdx = usRgnIdx;
	}
	public byte[] getMsgSensQryByRgnReq_S(){
		ByteBuffer bb_Msg=ByteBuffer.allocate(getSize());
		bb_Msg.put(FormatTransfer.toLH(usRgnIdx));
		return bb_Msg.array();
	}
}
