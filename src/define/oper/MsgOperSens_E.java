package define.oper;

public enum MsgOperSens_E {
	MSGOPER_SENS_QRY_BYRGN((byte) 5), MSGOPER_SENS_TRIGGER((byte) 5); // 感应触发通知

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private MsgOperSens_E(byte val) {
		this.val = val;
	}
}
