package define.oper;

public enum MsgOperCtrl_E {
	MSGOPER_CTRL_STUDY((byte) 5), // ����ѧϰ
	MSGOPER_CTRL_STATUS((byte) 6), // ������״̬

	MSGOPER_CTRL_TEST((byte) 7); // ����ָ��

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private MsgOperCtrl_E(byte val) {
		this.val = val;
	}
}
