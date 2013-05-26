package define.type;

public enum MsgId_E {
	MSGID_NULL((short) 0), MSGID_SQL((short) 1), // SQL语句
	MSGID_USER((short) 2), // 用户
	MSGID_CTRL((short) 3), // 控制器
	MSGID_RGN((short) 4), // 区域
	MSGID_APPL((short) 5), // 家电
	MSGID_CMD((short) 6), // 指令
	MSGID_SENS((short) 7), // 感应器
	MSGID_SENSLOG((short) 8), // 感应器通知记录
	MSGID_CAMA((short) 9), // 摄像头
	MSGID_MODE((short) 10), // 智能模式
	MSGID_MODECMD((short) 11), // 智能模式指令
	MSGID_ZGB((short) 12); // ZIGBEE

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
