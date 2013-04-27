package define.entity;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class Cmd_S {
	short usIdx;
	byte ucDevType; // 设备类型
	short usDevIdx; // 设备（家电、感应器、摄像头，由设备类型决定哪类设备）索引
	String szName; // 名称
	String szVoice; // 语音文本，用于匹配语音指令，仅支持汉字
	short usCtrlIdx; // 指令执行的控制器
	byte ucCode; // 指令码
	int uiPara; // 指令参数

	public Cmd_S(short usIdx, byte ucDevType, short usDevIdx, String szName,
			String szVoice, short usCtrlIdx, byte ucCode, int uiPara) {
		super();
		this.usIdx = usIdx;
		this.ucDevType = ucDevType;
		this.usDevIdx = usDevIdx;
		this.szName = szName;
		this.szVoice = szVoice;
		this.usCtrlIdx = usCtrlIdx;
		this.ucCode = ucCode;
		this.uiPara = uiPara;
	}

	public Cmd_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static short getSize() {
		return 108;
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
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

	public short getUsCtrlIdx() {
		return usCtrlIdx;
	}

	public void setUsCtrlIdx(short usCtrlIdx) {
		this.usCtrlIdx = usCtrlIdx;
	}

	public byte getUcCode() {
		return ucCode;
	}

	public void setUcCode(byte ucCode) {
		this.ucCode = ucCode;
	}

	public int getUiPara() {
		return uiPara;
	}

	public void setUiPara(int uiPara) {
		this.uiPara = uiPara;
	}

	public byte[] getCmd_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(108);
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		bb_Msg.put(ucDevType);
		bb_Msg.put(FormatTransfer.toLH(usDevIdx));
		int szName_Len = szName.getBytes().length;
		bb_Msg.put(szName.getBytes());
		if (szName_Len < 32) {
			byte[] szName_Sub = new byte[32 - szName_Len];
			bb_Msg.put(szName_Sub);
		}
		int szVoice_Len = szVoice.getBytes().length;
		bb_Msg.put(szVoice.getBytes());
		if (szVoice_Len < 64) {
			byte[] szVoice_Sub = new byte[64 - szVoice_Len];
			bb_Msg.put(szVoice_Sub);
		}

		bb_Msg.put(FormatTransfer.toLH(usCtrlIdx));
		bb_Msg.put(ucCode);
		bb_Msg.put(FormatTransfer.toLH(uiPara));
		return bb_Msg.array();
	}

	public void setCmd_S(byte[] b) {
		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		ucDevType = b[2];

		byte[] usDevIdx_b = new byte[2];
		System.arraycopy(b, 3, usDevIdx_b, 0, 2);
		usDevIdx = FormatTransfer.lBytesToShort(usDevIdx_b);

		byte[] szName_b = new byte[32];
		System.arraycopy(b, 5, szName_b, 0, 32);
		szName = new String(szName_b).trim();
		// try {
		// szName = new String(szName_b, "gbk").trim();
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		byte[] szVoice_b = new byte[64];
		System.arraycopy(b, 37, szVoice_b, 0, 64);
		szVoice = new String(szVoice_b).trim();
		// try {
		// szVoice = new String(szVoice_b, "gbk").trim();
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		byte[] usCtrlIdx_b = new byte[2];
		System.arraycopy(b, 101, usCtrlIdx_b, 0, 2);
		usCtrlIdx = FormatTransfer.lBytesToShort(usCtrlIdx_b);

		ucCode = b[103];

		byte[] uiPara_b = new byte[4];
		System.arraycopy(b, 104, uiPara_b, 0, 4);
		uiPara = FormatTransfer.lBytesToInt(uiPara_b);

	}
}
