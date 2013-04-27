package define.oper;

public enum MsgOperCama_E {
	MSGOPER_CAMA_MAX((byte)5); // ÷¥––÷∏¡Ó
	   private byte val;
	public byte getVal() {
		return this.val;
	}
	private MsgOperCama_E(byte val) {
		this.val = val;
	}
}
