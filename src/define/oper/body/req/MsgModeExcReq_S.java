package define.oper.body.req;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgModeExcReq_S {
	short usIdx;

	public MsgModeExcReq_S(short usIdx) {
		super();
		this.usIdx = usIdx;
	}

	public static short getSize() {
		return 2;
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}

	public byte[] getMsgModeExcReq_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(2);
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		return bb_Msg.array();
	}
}
