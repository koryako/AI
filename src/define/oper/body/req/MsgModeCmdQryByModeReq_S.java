package define.oper.body.req;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgModeCmdQryByModeReq_S {
	short usModeIdx = 0; // 对象索引，usIdx = 0查询所有对象

	public MsgModeCmdQryByModeReq_S(short usModeIdx) {
		super();
		this.usModeIdx = usModeIdx;
	}

	public MsgModeCmdQryByModeReq_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public byte[] getMsgQryReq_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(2);
		bb_Msg.put(FormatTransfer.toLH(usModeIdx));
		return bb_Msg.array();
	}
}
