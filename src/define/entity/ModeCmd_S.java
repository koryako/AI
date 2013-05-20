package define.entity;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class ModeCmd_S {
	short usIdx;
	short usModeIdx;
	short usCmdIdx;

	public ModeCmd_S(short usIdx, short usModeIdx, short usCmdIdx) {
		super();
		this.usIdx = usIdx;
		this.usModeIdx = usModeIdx;
		this.usCmdIdx = usCmdIdx;
	}

	public ModeCmd_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static short getSize() {
		return 6;
	}

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}

	public short getUsModeIdx() {
		return usModeIdx;
	}

	public void setUsModeIdx(short usModeIdx) {
		this.usModeIdx = usModeIdx;
	}

	public short getUsCmdIdx() {
		return usCmdIdx;
	}

	public void setUsCmdIdx(short usCmdIdx) {
		this.usCmdIdx = usCmdIdx;
	}

	public byte[] getModeCmd_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(6);
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		bb_Msg.put(FormatTransfer.toLH(usModeIdx));
		bb_Msg.put(FormatTransfer.toLH(usCmdIdx));
		return bb_Msg.array();
	}

	public void setModeCmd_S(byte[] b) {
		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		byte[] usModeIdx_b = new byte[2];
		System.arraycopy(b, 2, usModeIdx_b, 0, 2);
		usModeIdx = FormatTransfer.lBytesToShort(usModeIdx_b);

		byte[] usCmdIdx_b = new byte[2];
		System.arraycopy(b, 4, usCmdIdx_b, 0, 2);
		usCmdIdx = FormatTransfer.lBytesToShort(usCmdIdx_b);
	}
}
