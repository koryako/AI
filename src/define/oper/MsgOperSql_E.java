package define.oper;

public enum MsgOperSql_E {
	MSGOPER_MAX((byte) 5);

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private MsgOperSql_E(byte val) {
		this.val = val;
	}
}
