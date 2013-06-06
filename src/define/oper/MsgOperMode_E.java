package define.oper;

public enum MsgOperMode_E {
	MSGOPER_MODE_QRY_BYRGN((byte) 5), MSGOPER_MODE_EXC((byte) 6);

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private MsgOperMode_E(byte val) {
		this.val = val;
	}
}
