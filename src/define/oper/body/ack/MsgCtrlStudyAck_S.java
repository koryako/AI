package define.oper.body.ack;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgCtrlStudyAck_S {
	short usIdx; // 对象索引
	short usCmdIdx; // 控制器对应的指令索引
	byte ucOffset; // 红外码偏移码
	byte usError;

	public MsgCtrlStudyAck_S(short usIdx, short usCmdIdx, byte ucOffset,
			byte usError) {
		super();
		this.usIdx = usIdx;
		this.usCmdIdx = usCmdIdx;
		this.ucOffset = ucOffset;
		this.usError = usError;
	}

	public MsgCtrlStudyAck_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public short getUsIdx() {
		return usIdx;
	}

	public static short getSize() {
		return 6;
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

	public byte getUcOffset() {
		return ucOffset;
	}

	public void setUcOffset(byte ucOffset) {
		this.ucOffset = ucOffset;
	}

	public byte getUsError() {
		return usError;
	}

	public void setUsError(byte usError) {
		this.usError = usError;
	}

	public void setMsgCtrlStudyAck_S(byte[] b) {

		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		byte[] usCmdIdx_b = new byte[2];
		System.arraycopy(b, 2, usCmdIdx_b, 0, 2);
		usCmdIdx = FormatTransfer.lBytesToShort(usCmdIdx_b);

		ucOffset = b[4];

		usError = b[5];

	}
}
