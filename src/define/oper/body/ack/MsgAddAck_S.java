package define.oper.body.ack;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgAddAck_S {
	short usIdx; // 新增对象索引
	byte usError;

	public MsgAddAck_S(short usIdx, byte usError) {
		super();
		this.usIdx = usIdx;
		this.usError = usError;
	}

	public MsgAddAck_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}

	public byte getUsError() {
		return usError;
	}

	public void setUsError(byte usError) {
		this.usError = usError;
	}

	public void setMsgAddAck_S(byte[] b) {
		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		usError = b[2];
	}
}
