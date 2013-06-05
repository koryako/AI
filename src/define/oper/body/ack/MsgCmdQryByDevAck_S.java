package define.oper.body.ack;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgCmdQryByDevAck_S {
	byte ucDevType;
	short usDevIdx; // ¶ÔÏóË÷Òý
	byte ucType;
	byte ucErr;
	short usCnt;
	byte[] pucData;

	public MsgCmdQryByDevAck_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MsgCmdQryByDevAck_S(byte ucDevType, short usDevIdx, byte ucType,
			byte ucErr, short usCnt, byte[] pucData) {
		super();
		this.ucDevType = ucDevType;
		this.usDevIdx = usDevIdx;
		this.ucType = ucType;
		this.ucErr = ucErr;
		this.usCnt = usCnt;
		this.pucData = pucData;
	}

	public byte getUcType() {
		return ucType;
	}

	public void setUcType(byte ucType) {
		this.ucType = ucType;
	}

	public byte getUcDevType() {
		return ucDevType;
	}

	public void setUcDevType(byte ucDevType) {
		this.ucDevType = ucDevType;
	}

	public short getUsDevIdx() {
		return usDevIdx;
	}

	public void setUsDevIdx(short usDevIdx) {
		this.usDevIdx = usDevIdx;
	}

	public byte getUcErr() {
		return ucErr;
	}

	public void setUcErr(byte ucErr) {
		this.ucErr = ucErr;
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

	public void setMsgCmdQryByDevAck_S(byte[] b) {

		ucDevType = b[0];

		byte[] usDevIdx_b = new byte[2];
		System.arraycopy(b, 1, usDevIdx_b, 0, 2);
		usDevIdx = FormatTransfer.lBytesToShort(usDevIdx_b);

		ucType = b[3];

		ucErr = b[4];

		byte[] usCnt_b = new byte[2];
		System.arraycopy(b, 5, usCnt_b, 0, 2);
		usCnt = FormatTransfer.lBytesToShort(usCnt_b);

		byte[] pucData_b = new byte[b.length - 7];
		System.arraycopy(b, 7, pucData_b, 0, b.length - 7);
		pucData = pucData_b;

	}

}
