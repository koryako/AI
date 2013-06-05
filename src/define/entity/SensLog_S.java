package define.entity;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class SensLog_S {
	short usIdx;
	String szRgnName; // 区域名称
	String szName; // 感应器名称
	byte ucType; // 感应器类型
	int uiTime;

	public SensLog_S(short usIdx, String szRgnName, String szName, byte ucType,
			int uiTime) {
		super();
		this.usIdx = usIdx;
		this.szRgnName = szRgnName;
		this.szName = szName;
		this.ucType = ucType;
		this.uiTime = uiTime;
	}

	public SensLog_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static short getSize() {
		return 71;
	}

	public String getSzRgnName() {
		return szRgnName;
	}

	public void setSzRgnName(String szRgnName) {
		this.szRgnName = szRgnName;
	}

	public String getSzName() {
		return szName;
	}

	public void setSzName(String szName) {
		this.szName = szName;
	}

	public byte getUcType() {
		return ucType;
	}

	public void setUcType(byte ucType) {
		this.ucType = ucType;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}

	public void setUiTime(int uiTime) {
		this.uiTime = uiTime;
	}

	public byte[] getMsgSensTriggerNty_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(65);
		bb_Msg.put(szRgnName.getBytes());
		bb_Msg.put(szName.getBytes());
		bb_Msg.put(ucType);
		bb_Msg.put(FormatTransfer.toLH(uiTime));
		return bb_Msg.array();
	}

	public short getUsIdx() {
		return usIdx;
	}

	public int getUiTime() {
		return uiTime;
	}

	public void setSensLog_S(byte[] b) {
		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		try {
			byte[] szRgnName_b = new byte[32];
			System.arraycopy(b, 2, szRgnName_b, 0, 32);
			szRgnName = new String(szRgnName_b, "gbk").trim();

			byte[] szName_b = new byte[32];
			System.arraycopy(b, 34, szName_b, 0, 32);
			szName = new String(szName_b, "gbk").trim();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ucType = b[66];

		byte[] uiTime_b = new byte[4];
		System.arraycopy(b, 67, uiTime_b, 0, 4);
		uiTime = FormatTransfer.lBytesToInt(uiTime_b);

	}
}
