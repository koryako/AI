package define.type;

public enum MsgId_E {
	MSGID_NULL((short) 0), MSGID_USER((short) 1), // �û�
	MSGID_CTRL((short) 2), // ������
	MSGID_RGN((short) 3), // ����
	MSGID_APPL((short) 4), // �ҵ�
	MSGID_CMD((short) 5), // ָ��
	MSGID_SENS((short) 6), // ��Ӧ��
	MSGID_SENSLOG((short) 7), // ��Ӧ��֪ͨ��¼
	MSGID_CAMA((short) 8), // ����ͷ
	MSGID_MODE((short) 9), // ����ģʽ
	MSGID_MODECMD((short) 10), // ����ģʽָ��
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
