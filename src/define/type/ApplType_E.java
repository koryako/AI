package define.type;

public enum ApplType_E {
	APPL_TYPE_NULL((byte) 0), APPL_TYPE_LIGHT((byte) 1), // 灯
	APPL_TYPE_TVSET((byte) 2), // 电视
	APPL_TYPE_STB((byte) 3), // 机顶盒
	APPL_TYPE_AIRCOND((byte) 4), // 空调
	APPL_TYPE_CURTAIN((byte) 5), // 窗帘
	APPL_TYPE_CUSTOM((byte) 6); // 自定义

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private ApplType_E(byte val) {
		this.val = val;
	}

}
