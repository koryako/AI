package define.type;

public enum ApplType_E {
	APPL_TYPE_NULL((byte) 0), APPL_TYPE_SWITCH((byte) 1), // 电源开关
	APPL_TYPE_TVSET((byte) 2), // 电视和机顶盒
	APPL_TYPE_HDPLAY((byte) 3), // 高清播放器
	APPL_TYPE_AIRCOND((byte) 4), // 空调
	APPL_TYPE_CURTAIN((byte) 5), // 窗帘
	APPL_TYPE_FAN((byte) 6), // 电风扇
	APPL_TYPE_WATER((byte) 7), // 热水器
	APPL_TYPE_CUSTOM((byte) 8); // 自定义

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private ApplType_E(byte val) {
		this.val = val;
	}

}
