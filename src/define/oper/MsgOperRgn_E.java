package define.oper;

public enum MsgOperRgn_E {
	 MSGOPER_RGN_MAX((byte)5);

	 byte val;
	 
	public byte getVal() {
		return val;
	}

	private MsgOperRgn_E(byte val) {
		this.val=val;
	}
}
