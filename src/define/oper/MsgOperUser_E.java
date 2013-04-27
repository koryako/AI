package define.oper;

public enum MsgOperUser_E {
	MSGOPER_USER_LOGIN((byte)5);
    private byte val;
	public byte getVal() {
		return this.val;
	}
	private MsgOperUser_E(byte val) {
		this.val = val;
	}
}
