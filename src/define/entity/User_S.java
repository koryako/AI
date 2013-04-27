package define.entity;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class User_S {
	short usIdx;
	String szName; // 用户名
	String szPass; // 密码
	byte ucType; // 用户类型

	public User_S(short usIdx, String szName, String szPass, byte ucType) {
		super();
		this.usIdx = usIdx;
		this.szName = szName;
		this.szPass = szPass;
		this.ucType = ucType;
	}

	public User_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}

	public static short getSize() {
		return 67;
	}

	public String getSzName() {
		return szName;
	}

	public void setSzName(String szName) {
		this.szName = szName;
	}

	public String getSzPass() {
		return szPass;
	}

	public void setSzPass(String szPass) {
		this.szPass = szPass;
	}

	public byte getUcType() {
		return ucType;
	}

	public void setUcType(byte ucType) {
		this.ucType = ucType;
	}

	public byte[] getUser_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(getSize());
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		int szName_Len = szName.getBytes().length;
		bb_Msg.put(szName.getBytes());
		if (szName_Len < 32) {
			byte[] szName_Sub = new byte[32 - szName_Len];
			bb_Msg.put(szName_Sub);
		}

		int szPass_Len = szPass.getBytes().length;
		bb_Msg.put(szPass.getBytes());
		if (szPass_Len < 32) {
			byte[] szPass_Sub = new byte[32 - szPass_Len];
			bb_Msg.put(szPass_Sub);
		}

		bb_Msg.put(ucType);
		return bb_Msg.array();
	}

	public void setUser_S(byte[] b) {
		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		byte[] szName_b = new byte[32];
		System.arraycopy(b, 2, szName_b, 0, 32);
		szName = new String(szName_b).trim();
		// try {
		// szName=new String(szName_b,"gbk").trim();
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		byte[] szPass_b = new byte[32];
		System.arraycopy(b, 34, szPass_b, 0, 32);
		szPass = new String(szPass_b).trim();

		ucType = b[66];
	}
}
