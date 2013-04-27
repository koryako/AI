package define.oper;

public enum MsgOperModeCmd_E {
	MSGOPER_MODECMD_MAX((byte)5);
	 	private byte val;
		public byte getVal() {
			return this.val;
		}
		private MsgOperModeCmd_E(byte val) {
			this.val = val;
		}
}
