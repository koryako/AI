package define.entity;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class Rgn_S {
	short usIdx;
	String szName; // ¹Ø¼ü×Ö

	public Rgn_S(short usIdx, String szName) {
		super();
		this.usIdx = usIdx;
		this.szName = szName;
	}

	public Rgn_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static short getSize() {
		return 34;
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}

	public String getSzName() {
		return szName;
	}

	public void setSzName(String szName) {
		this.szName = szName;
	}

	public byte[] getRgn_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(34);
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		int szName_Len = szName.getBytes().length;
		System.out.println(szName_Len);
		bb_Msg.put(szName.getBytes());
		if (szName_Len < 32) {
			byte[] szName_Sub = new byte[32 - szName_Len];
			bb_Msg.put(szName_Sub);
		}
		return bb_Msg.array();
	}

	public void setRgn_S(byte[] b) {
		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		byte[] szName_b = new byte[32];
		System.arraycopy(b, 2, szName_b, 0, 32);
		szName = new String(szName_b).trim();
		// try {
		// szName = new String(szName_b, "gbk").trim();
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
