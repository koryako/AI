package define.type;

public enum CmdDevType_E {
		CMD_DEV_NULL((byte)0),
		CMD_DEV_APPL((byte)1),     // �ҵ�
	    CMD_DEV_SENS((byte)2),     // ��Ӧ��
	    CMD_DEV_CAMA((byte)3);    // ����ͷ
	    
	    
	    private byte val;
		public byte getVal() {
			return this.val;
		}
		private CmdDevType_E(byte val) {
			this.val = val;
		}
}
