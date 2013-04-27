package define.oper;

public enum MsgOperMode_E {
	MSGOPER_MODE_EXC((byte)5);
	 
    private byte val;
	public byte getVal() {
		return this.val;
	}
	private MsgOperMode_E(byte val) {
		this.val = val;
	}
}
