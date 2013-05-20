package define.oper.body.req;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgCtrlTestReq_S {
	short usIdx; // 对象索引
	byte ucCode; // 指令码
	int uiPara; // 指令参数

	public MsgCtrlTestReq_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MsgCtrlTestReq_S(short usIdx, byte ucCode, int uiPara) {
		super();
		this.usIdx = usIdx;
		this.ucCode = ucCode;
		this.uiPara = uiPara;
	}

	public static short getSize() {
		return 7;
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
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

	public byte[] getMsgCtrlTestReq_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(7);
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		bb_Msg.put(ucCode);
		bb_Msg.put(FormatTransfer.toLH(uiPara));
		return bb_Msg.array();
	}
}
