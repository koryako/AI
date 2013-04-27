package define.type;

public enum MsgType_E {
		MSGTYPE_NULL((byte)0),
	    MSGTYPE_REQ((byte)1),   // 请求消息
	    MSGTYPE_ACK((byte)2),   // 应答消息
	    MSGTYPE_NTF((byte)3),   // 通知消息
	    MSGTYPE_MAX((byte)4);

	 private byte val;
		private MsgType_E(byte val) {
			this.val=val;
		}
		public byte getVal() {
			return val;
		}
}
