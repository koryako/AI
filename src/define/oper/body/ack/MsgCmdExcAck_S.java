package define.oper.body.ack;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgCmdExcAck_S {
	short usIdx; // ¶ÔÏóË÷Òý
	byte usError;

	public MsgCmdExcAck_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	private MsgCmdExcAck_S(short usIdx, byte usError) {
		super();
		this.usIdx = usIdx;
		this.usError = usError;
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

	public void setMsgCmdExcAck_S(byte[] b) {
		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		usError = b[2];
	}

}
