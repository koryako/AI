package define.entity;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class Mode_S {
	short usIdx;
	short usRgnIdx; // 区域索引
	String szName; // 名称
	String szVoice; // 语音文本，用于匹配语音指令，仅支持

	public Mode_S(short usIdx, short usRgnIdx, String szName, String szVoice) {
		super();
		this.usIdx = usIdx;
		this.usRgnIdx = usRgnIdx;
		this.szName = szName;
		this.szVoice = szVoice;
	}

	public Mode_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static short getSize() {
		return 100;
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

	public String getSzVoice() {
		return szVoice;
	}

	public void setSzVoice(String szVoice) {
		this.szVoice = szVoice;
	}

	public byte[] getMode_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(100);
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		bb_Msg.put(FormatTransfer.toLH(usRgnIdx));
		int szName_Len = szName.getBytes().length;
		bb_Msg.put(szName.getBytes());
		if (szName_Len < 32) {
			byte[] szName_Sub = new byte[32 - szName_Len];
			bb_Msg.put(szName_Sub);
		}
		int szVoice_Len = szVoice.getBytes().length;
		bb_Msg.put(szVoice.getBytes());
		if (szVoice_Len < 64) {
			byte[] szName_Sub = new byte[32 - szName_Len];
			bb_Msg.put(szName_Sub);
		}
		return bb_Msg.array();
	}

	public void setMode_S(byte[] b) {
		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		byte[] usRgnIdx_b = new byte[2];
		System.arraycopy(b, 2, usRgnIdx_b, 0, 2);
		usRgnIdx = FormatTransfer.lBytesToShort(usRgnIdx_b);

		byte[] szName_b = new byte[32];
		System.arraycopy(b, 4, szName_b, 0, 32);
		szName = new String(szName_b).trim();
		// try {
		// szName=new String(szName_b,"gbk").trim();
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		byte[] szVoice_b = new byte[64];
		System.arraycopy(b, 36, szVoice_b, 0, 64);
		szVoice = new String(szVoice_b).trim();
		// try {
		// szVoice=new String(szVoice_b,"gbk").trim();
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
