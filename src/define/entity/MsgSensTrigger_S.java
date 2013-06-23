package define.entity;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgSensTrigger_S {
	int uiPara;
	int uiTime;
	short usCnt;
	byte[] pucData;

	public int getUiPara() {
		return uiPara;
	}

	public MsgSensTrigger_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setUiPara(int uiPara) {
		this.uiPara = uiPara;
	}

	public int getUiTime() {
		return uiTime;
	}

	public void setUiTime(int uiTime) {
		this.uiTime = uiTime;
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

	public MsgSensTrigger_S(int uiPara, int uiTime, short usCnt, byte[] pucData) {
		super();
		this.uiPara = uiPara;
		this.uiTime = uiTime;
		this.usCnt = usCnt;
		this.pucData = pucData;
	}

	public void setMsgSensTrigger_S(byte[] b) {

		byte[] uiPara_b = new byte[4];
		System.arraycopy(b, 0, uiPara_b, 0, 4);
		uiPara = FormatTransfer.lBytesToShort(uiPara_b);

		byte[] uiTime_b = new byte[4];
		System.arraycopy(b, 4, uiTime_b, 0, 4);
		uiTime = FormatTransfer.lBytesToShort(uiTime_b);

		byte[] usCnt_b = new byte[2];
		System.arraycopy(b, 8, usCnt_b, 0, 2);
		usCnt = FormatTransfer.lBytesToShort(usCnt_b);

		byte[] pucData_b = new byte[b.length - 10];
		System.arraycopy(b, 10, pucData_b, 0, b.length - 10);
		pucData = pucData_b;
	}
}
