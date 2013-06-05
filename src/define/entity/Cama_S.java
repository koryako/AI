package define.entity;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class Cama_S {
	short usIdx;
	short usRgnIdx; // 区域索引
	String szName; // 名称
	int uiIpAddr; // IP地址
	short usPort; // 端口

	public Cama_S(short usIdx, short usRgnIdx, String szName, int uiIpAddr,
			short usPort) {
		super();
		this.usIdx = usIdx;
		this.usRgnIdx = usRgnIdx;
		this.szName = szName;
		this.uiIpAddr = uiIpAddr;
		this.usPort = usPort;
	}

	public Cama_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static short getSize() {
		return 42;
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

	public int getUiIpAddr() {
		return uiIpAddr;
	}

	public void setUiIpAddr(int uiIpAddr) {
		this.uiIpAddr = uiIpAddr;
	}

	public short getUsPort() {
		return usPort;
	}

	public void setUsPort(short usPort) {
		this.usPort = usPort;
	}

	public byte[] getCama_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(42);
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

		bb_Msg.put(FormatTransfer.toLH(uiIpAddr));
		bb_Msg.put(FormatTransfer.toLH(usPort));
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
		// szName = new String(szName_b).trim();
		try {
			szName = new String(szName_b, "gbk").trim();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] uiIpAddr_b = new byte[4];
		System.arraycopy(b, 36, uiIpAddr_b, 0, 4);
		uiIpAddr = FormatTransfer.lBytesToInt(uiIpAddr_b);

		byte[] usPort_b = new byte[2];
		System.arraycopy(b, 40, usPort_b, 0, 2);
		usPort = FormatTransfer.lBytesToShort(usPort_b);

	}
}
