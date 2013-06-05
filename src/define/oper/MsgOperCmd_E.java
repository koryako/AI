package define.oper;

public enum MsgOperCmd_E {
	MSGOPER_CMD_QRY_BYDEV((byte) 5), // ÷¥––÷∏¡Ó
	MSGOPER_CMD_EXC((byte) 6);
	private byte val;

	public byte getVal() {
		return this.val;
	}

	private MsgOperCmd_E(byte val) {
		this.val = val;
	}
}
