package define.entity;

import java.nio.ByteBuffer;

public class MsgSensTriggerNty_S {
	String szRgnName; // 区域名称
	String szName; // 感应器名称
	byte ucType; // 感应器类型

	public MsgSensTriggerNty_S(String szRgnName, String szName, byte ucType) {
		super();
		this.szRgnName = szRgnName;
		this.szName = szName;
		this.ucType = ucType;
	}

	public MsgSensTriggerNty_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static short getSise() {
		return 65;
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

	public byte[] getMsgSensTriggerNty_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(65);
		bb_Msg.put(szRgnName.getBytes());
		bb_Msg.put(szName.getBytes());
		bb_Msg.put(ucType);
		return bb_Msg.array();
	}

	public void setMsgSensTriggerNty_S(byte[] b) {
		byte[] szRgnName_b = new byte[32];
		System.arraycopy(b, 0, szRgnName_b, 0, 32);
		szRgnName = new String(szRgnName_b).trim();

		byte[] szName_b = new byte[32];
		System.arraycopy(b, 32, szName_b, 0, 32);
		szName = new String(szName_b).trim();
		// try {
		// szName=new String(szName_b,"gbk").trim();
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		ucType = b[64];
	}
}
