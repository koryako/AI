package define.type;

public enum CmdType_E {
	CMD_TYPE_NULL((byte) 0), CMD_TYPE_PREDEF((byte) 1), // ϵͳԤ����
	CMD_TYPE_CUSTOM((byte) 2); // �û��Զ���

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private CmdType_E(byte val) {
		this.val = val;
	}
}
