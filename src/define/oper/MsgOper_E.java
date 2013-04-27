package define.oper;

public enum MsgOper_E {
		MSGOPER_NULL((byte)0),
	    MSGOPER_ADD((byte)1),
	    MSGOPER_DEL((byte)2),
	    MSGOPER_MOD((byte)3),
	    MSGOPER_QRY((byte)4),
		MSGOPER_MAX((byte)5);
		
		private byte val;
		public byte getVal() {
			return this.val;
		}
		private MsgOper_E(byte val) {
			this.val = val;
		}
//		public static String getMsgOper_Name(byte msgOper){
//			String msgOper_Name=null;
//			switch (msgOper) {
//			case 1:
//				msgOper_Name="add";
//				break;
//			case 2:
//				msgOper_Name="del";
//				break;
//			case 3:
//				msgOper_Name="mod";
//				break;
//			case 4:
//				msgOper_Name="qry";
//				break;
//			case 5:
//				msgOper_Name="max";
//				break;
//			default:
//				break;
//			}
//			
//			return msgOper_Name;
//		}
}
