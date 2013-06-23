package define.entity;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class Cama_S {
	short usIdx;
	short usRgnIdx; // ÇøÓòË÷Òý
	String szName; // Ãû³Æ
	String szUid; // UID
	String szUser; //
	String szPass; // ÃÜÂë

	public static short getSize() {
		return 132;
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}

	public short getUsRgnIdx() {
		return usRgnIdx;
	}

	public void setUsRgnIdx(short usRgnIdx) {
		this.usRgnIdx = usRgnIdx;
	}

	public String getSzName() {
		return szName;
	}

	public void setSzName(String szName) {
		this.szName = szName;
	}

	public String getSzUid() {
		return szUid;
	}

	public void setSzUid(String szUid) {
		this.szUid = szUid;
	}

	public String getSzUser() {
		return szUser;
	}

	public void setSzUser(String szUser) {
		this.szUser = szUser;
	}

	public String getSzPass() {
		return szPass;
	}

	public void setSzPass(String szPass) {
		this.szPass = szPass;
	}

	public byte[] getCama_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(getSize());
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		bb_Msg.put(FormatTransfer.toLH(usRgnIdx));
		try {
			int szName_Len = szName.getBytes("gbk").length;
			bb_Msg.put(szName.getBytes("gbk"));
			if (szName_Len < 32) {
				byte[] szName_Sub = new byte[32 - szName_Len];
				bb_Msg.put(new String(szName_Sub).getBytes("gbk"));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			int szUid_Len = szUid.getBytes("gbk").length;
			bb_Msg.put(szUid.getBytes("gbk"));
			if (szUid_Len < 32) {
				byte[] szUid_Sub = new byte[32 - szUid_Len];
				bb_Msg.put(new String(szUid_Sub).getBytes("gbk"));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			int szUser_Len = szUser.getBytes("gbk").length;
			bb_Msg.put(szUser.getBytes("gbk"));
			if (szUser_Len < 32) {
				byte[] szUser_Sub = new byte[32 - szUser_Len];
				bb_Msg.put(new String(szUser_Sub).getBytes("gbk"));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			int szPass_Len = szPass.getBytes("gbk").length;
			bb_Msg.put(szPass.getBytes("gbk"));
			if (szPass_Len < 32) {
				byte[] szPass_Sub = new byte[32 - szPass_Len];
				bb_Msg.put(new String(szPass_Sub).getBytes("gbk"));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bb_Msg.array();
	}

	public void setCama_S(byte[] b) {

		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		byte[] usRgnIdx_b = new byte[2];
		System.arraycopy(b, 2, usRgnIdx_b, 0, 2);
		usRgnIdx = FormatTransfer.lBytesToShort(usRgnIdx_b);

		byte[] szName_b = new byte[32];
		System.arraycopy(b, 4, szName_b, 0, 32);
		try {
			szName = new String(szName_b, "gbk").trim();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] szUid_b = new byte[32];
		System.arraycopy(b, 36, szUid_b, 0, 32);
		try {
			szUid = new String(szUid_b, "gbk").trim();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] szUser_b = new byte[32];
		System.arraycopy(b, 68, szUser_b, 0, 32);
		try {
			szUser = new String(szUser_b, "gbk").trim();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] szPass_b = new byte[32];
		System.arraycopy(b, 100, szPass_b, 0, 32);
		try {
			szPass = new String(szPass_b, "gbk").trim();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
