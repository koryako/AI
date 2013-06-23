package define.oper;

public enum MsgOperCtrl_E {
	MSGOPER_CTRL_STUDY((byte) 5), // 红外学习
	MSGOPER_CTRL_STATUS((byte) 6), // 控制器状态

	MSGOPER_CTRL_TEST((byte) 7); // 测试指令

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private MsgOperCtrl_E(byte val) {
		this.val = val;
	}
}
