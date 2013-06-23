package define.oper;

public enum Ctrl_StatType_E {
	CTRL_STATTYPE_NULL((byte) 0), CTRL_STATTYPE_SWITCH((byte) 1), //
	CTRL_STATTYPE_TEMP((byte) 2), // 梁業
	CTRL_STATTYPE_MOIS((byte) 3), // 物業
	CTRL_STATTYPE_LIGHT((byte) 4), // 高業
	CTRL_STATTYPE_MAX((byte) 5);
	private byte val;

	public byte getVal() {
		return this.val;
	}

	private Ctrl_StatType_E(byte val) {
		this.val = val;
	}
}
