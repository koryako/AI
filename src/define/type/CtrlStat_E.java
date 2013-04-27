package define.type;

public enum CtrlStat_E {
	  	CTRL_STAT_NULL((byte)0),
	    CTRL_STAT_ONLINE((byte)1),    // ‘⁄œﬂ
	    CTRL_STAT_OFFLINE((byte)2);   // ¿Îœﬂ
	  
	  private byte val;
		public byte getVal() {
			return this.val;
		}
		private CtrlStat_E(byte val) {
			this.val = val;
		}
}
