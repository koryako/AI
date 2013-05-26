package define.oper.body.ack;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgSqlExcAck_S {
	byte usError;
	short usRow;
	short usCol;
	byte[] pucData;

	public MsgSqlExcAck_S(byte usError, short usRow, short usCol, byte[] pucData) {
		super();
		this.usError = usError;
		this.usRow = usRow;
		this.usCol = usCol;
		this.pucData = pucData;
	}

	public MsgSqlExcAck_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public byte getUsError() {
		return usError;
	}

	public void setUsError(byte usError) {
		this.usError = usError;
	}

	public short getUsRow() {
		return usRow;
	}

	public void setUsRow(short usRow) {
		this.usRow = usRow;
	}

	public short getUsCol() {
		return usCol;
	}

	public void setUsCol(short usCol) {
		this.usCol = usCol;
	}

	public byte[] getPucData() {
		return pucData;
	}

	public void setPucData(byte[] pucData) {
		this.pucData = pucData;
	}

	public void setMsgSqlExcAck_S(byte[] b) {
		b[0] = usError;

		byte[] usRow_b = new byte[2];
		System.arraycopy(b, 1, usRow_b, 0, 2);
		usRow = FormatTransfer.lBytesToShort(usRow_b);

		byte[] usCol_b = new byte[2];
		System.arraycopy(b, 3, usCol_b, 0, 2);
		usCol = FormatTransfer.lBytesToShort(usCol_b);

		byte[] pucData_b = new byte[b.length - 5];
		System.arraycopy(b, 5, pucData_b, 0, b.length - 5);
		pucData = pucData_b;
	}
}
