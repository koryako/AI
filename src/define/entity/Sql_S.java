package define.entity;

import com.mac.smartcontrol.util.FormatTransfer;

public class Sql_S {
	short usLen;
	String szField;

	// static short size;

	public Sql_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Sql_S(short usLen, String szField) {
		super();
		this.usLen = usLen;
		this.szField = szField;
		// size = (short) (usLen + 2);
	}

	public short getUsLen() {
		return usLen;
	}

	public void setUsLen(short usLen) {
		this.usLen = usLen;
	}

	// public static short getSize() {
	// return size;
	// }

	public String getSzField() {
		return szField;
	}

	public void setSzField(String szField) {
		this.szField = szField;
	}

	// public byte[] getSql_S() {
	// ByteBuffer bb_Msg = ByteBuffer.allocate(getSize());
	// bb_Msg.put(FormatTransfer.toLH(usLen));
	// bb_Msg.put(szField.getBytes());
	// return bb_Msg.array();
	// }

	public void setSql_S(byte[] b) {
		byte[] usLen_b = new byte[2];
		System.arraycopy(b, 0, usLen_b, 0, 2);
		usLen = FormatTransfer.lBytesToShort(usLen_b);

		byte[] szField_b = new byte[usLen];
		System.arraycopy(b, 2, szField_b, 0, usLen);
		szField = new String(szField_b).trim();
	}
}
