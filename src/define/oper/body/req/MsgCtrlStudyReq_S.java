package define.oper.body.req;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgCtrlStudyReq_S {
	short usIdx; // 对象索引
	short usCmdIdx; // 控制器对应的指令索引

	public MsgCtrlStudyReq_S(short usIdx, short usCmdIdx) {
		super();
		this.usIdx = usIdx;
		this.usCmdIdx = usCmdIdx;
	}

	public MsgCtrlStudyReq_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}

	public short getUsCmdIdx() {
		return usCmdIdx;
	}

	public void setUsCmdIdx(short usCmdIdx) {
		this.usCmdIdx = usCmdIdx;
	}

	public byte[] getMsgCtrlStudyReq_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(4);
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		bb_Msg.put(FormatTransfer.toLH(usCmdIdx));
		return bb_Msg.array();
	}
}
