package define.type;

public enum CmdType_E {
	CMD_TYPE_NULL((byte) 0), CMD_TYPE_PREDEF((byte) 1), // 系统预定义
	CMD_TYPE_CUSTOM((byte) 2); // 用户自定义

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private CmdType_E(byte val) {
		this.val = val;
	}
}
