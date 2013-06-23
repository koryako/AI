package define.entity;

import java.nio.ByteBuffer;

public class CtrlStatus_S {

	byte ucType;
	byte uiValue;

	public byte getUcType() {
		return ucType;
	}

	public void setUcType(byte ucType) {
		this.ucType = ucType;
	}

	public static short getSize() {
		return 2;
	}

	public byte getUiValue() {
		return uiValue;
	}

	public void setUiValue(byte uiValue) {
		this.uiValue = uiValue;
	}

	public byte[] getCtrlStatus_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(getSize());
		bb_Msg.put(ucType);
		bb_Msg.put(uiValue);
		return bb_Msg.array();
	}

	public void setCtrlStatus_S(byte[] b) {
		ucType = b[0];
		uiValue = b[1];
	}

}
