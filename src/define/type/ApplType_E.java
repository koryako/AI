package define.type;

public enum ApplType_E {
	APPL_TYPE_NULL((byte) 0), APPL_TYPE_LIGHT((byte) 1), // ��
	APPL_TYPE_TVSET((byte) 2), // ����
	APPL_TYPE_STB((byte) 3), // ������
	APPL_TYPE_AIRCOND((byte) 4), // �յ�
	APPL_TYPE_CURTAIN((byte) 5), // ����
	APPL_TYPE_CUSTOM((byte) 6); // �Զ���

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private ApplType_E(byte val) {
		this.val = val;
	}

}
