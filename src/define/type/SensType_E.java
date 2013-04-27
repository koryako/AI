package define.type;

public enum SensType_E {
	    SENS_TYPE_NULL((byte)0),
	    SENS_TYPE_GAS((byte)1),    // 煤气感应
	    SENS_TYPE_SMOKE((byte)2);    // 烟雾感应
	    private byte val;
		public byte getVal() {
			return this.val;
		}
		private SensType_E(byte val) {
			this.val = val;
		}
}
