package define.oper.body.req;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgQryReq_S {
	 short usIdx=0;       // 对象索引，usIdx = 0查询所有对象

	public MsgQryReq_S(short usIdx) {
		super();
		this.usIdx = usIdx;
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}
	public byte[] getMsgQryReq_S(){
		ByteBuffer bb_Msg=ByteBuffer.allocate(2);
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		return bb_Msg.array();
	}
}
