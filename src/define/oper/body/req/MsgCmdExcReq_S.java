package define.oper.body.req;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgCmdExcReq_S {
	short usIdx;       // ¶ÔÏóË÷Òý

	
	private MsgCmdExcReq_S(short usIdx) {
		super();
		this.usIdx = usIdx;
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}
	public byte[] getMsgCmdExcReq_S(){
		ByteBuffer bb_Msg=ByteBuffer.allocate(2);
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		return bb_Msg.array();
	}
	
}
