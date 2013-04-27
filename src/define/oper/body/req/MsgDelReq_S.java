package define.oper.body.req;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgDelReq_S {
	short usIdx;       // 删除对象索引，usIdx = 0删除所有对象

	public MsgDelReq_S(short usIdx) {
		super();
		this.usIdx = usIdx;
	}
	

	public MsgDelReq_S() {
		super();
	}
	
	public static short getSize(){
		return 2;
	}


	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}
	public byte[] getMsgDelReq_S(){
		ByteBuffer bb_Msg=ByteBuffer.allocate(2);
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		return bb_Msg.array();
	}
}
