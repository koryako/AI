package define.type;

public enum UserType_E {
	USER_TYPE_NULL((byte)0),
	USER_TYPE_USER((byte)1),    // ��ͨ�û�
	USER_TYPE_ADMIN((byte)2);   // ϵͳ����Ա
	
	private byte val;
	public byte getVal() {
		return this.val;
	}
	private UserType_E(byte val) {
		this.val = val;
	}
}
