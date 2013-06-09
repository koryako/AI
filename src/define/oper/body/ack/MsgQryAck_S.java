package define.oper.body.ack;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgQryAck_S {
	private short usIdx;
	private byte usError;
	private short usCnt; // 对象数量
	private byte[] pucData; // 对象结构，多个

	public MsgQryAck_S(short usIdx, byte usError, short usCnt, byte[] pucData) {
		super();
		this.usIdx = usIdx;
		this.usError = usError;
		this.usCnt = usCnt;
		this.pucData = pucData;
	}

	public MsgQryAck_S() {
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

	public short getUsCnt() {
		return usCnt;
	}

	public void setUsCnt(short usCnt) {
		this.usCnt = usCnt;
	}

	public byte[] getPucData() {
		return pucData;
	}

	public void setPucData(byte[] pucData) {
		this.pucData = pucData;
	}

	public void setMsgQryAck_S(byte[] b) {
		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		usError = b[2];

		byte[] usCnt_b = new byte[2];
		System.arraycopy(b, 3, usCnt_b, 0, 2);
		usCnt = FormatTransfer.lBytesToShort(usCnt_b);

		byte[] pucData_b = new byte[b.length - 5];
		System.arraycopy(b, 5, pucData_b, 0, b.length - 5);
		pucData = pucData_b;
	}
}
