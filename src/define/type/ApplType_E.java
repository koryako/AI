package define.type;

public enum ApplType_E {
	APPL_TYPE_NULL((byte) 0), APPL_TYPE_SWITCH((byte) 1), // ��Դ����
	APPL_TYPE_TVSET((byte) 2), // ���Ӻͻ�����
	APPL_TYPE_HDPLAY((byte) 3), // ���岥����
	APPL_TYPE_AIRCOND((byte) 4), // �յ�
	APPL_TYPE_CURTAIN((byte) 5), // ����
	APPL_TYPE_FAN((byte) 6), // �����
	APPL_TYPE_WATER((byte) 7), // ��ˮ��
	APPL_TYPE_CUSTOM((byte) 8); // �Զ���

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private ApplType_E(byte val) {
		this.val = val;
	}

}
