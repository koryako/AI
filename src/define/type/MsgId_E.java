package define.type;

public enum MsgId_E {
	MSGID_NULL((short) 0), MSGID_USER((short) 1), // 用户
	MSGID_CTRL((short) 2), // 控制器
	MSGID_RGN((short) 3), // 区域
	MSGID_APPL((short) 4), // 家电
	MSGID_CMD((short) 5), // 指令
	MSGID_SENS((short) 6), // 感应器
	MSGID_SENSLOG((short) 7), // 感应器通知记录
	MSGID_CAMA((short) 8), // 摄像头
	MSGID_MODE((short) 9), // 智能模式
	MSGID_MODECMD((short) 10), // 智能模式指令
	MSGID_ZGB((short) 11); // ZIGBEE

	private short val;

	public short getVal() {
		return this.val;
	}

	private MsgId_E(short val) {
		this.val = val;
	}
	// public static String getMsgId_Name(short msgId){
	// String msgId_Name=null;
	// switch (msgId) {
	// case 1:
	// msgId_Name="user";
	// break;
	// case 2:
	// msgId_Name="ctrl";
	// break;
	// case 3:
	// msgId_Name="rgn";
	// break;
	// case 4:
	// msgId_Name="appl";
	// break;
	// case 5:
	// msgId_Name="cmd";
	// break;
	// case 6:
	// msgId_Name="sens";
	// break;
	// case 7:
	// msgId_Name="cama";
	// break;
	// case 8:
	// msgId_Name="mode";
	// break;
	// case 9:
	// msgId_Name="modecmd";
	// break;
	// case 10:
	// msgId_Name="zgb";
	// break;
	// default:
	// break;
	// }
	//
	// return msgId_Name;
	// }
}
