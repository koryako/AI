package define.oper;

public enum MsgOperAppl_E {
	MSGOPER_APPL_MAX((byte)5);
	  
    private byte val;
	public byte getVal() {
		return this.val;
	}
	private MsgOperAppl_E(byte val) {
		this.val = val;
	}
}
