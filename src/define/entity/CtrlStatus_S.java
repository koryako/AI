package define.entity;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class CtrlStatus_S {

	byte ucType;
	int uiValue;

	public byte getUcType() {
		return ucType;
	}

	public void setUcType(byte ucType) {
		this.ucType = ucType;
	}

	public static short getSize() {
		return 5;
	}

	public int getUiValue() {
		return uiValue;
	}

	public void setUiValue(int uiValue) {
		this.uiValue = uiValue;
	}

	public byte[] getCtrlStatus_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(getSize());
		bb_Msg.put(ucType);
		bb_Msg.put(FormatTransfer.toLH(uiValue));
		return bb_Msg.array();
	}

	public void setCtrlStatus_S(byte[] b) {
		ucType = b[0];
		byte[] uiValue_b = new byte[4];
		System.arraycopy(b, 1, uiValue_b, 0, 4);
		uiValue = FormatTransfer.lBytesToShort(uiValue_b);
	}

}
