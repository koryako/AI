package define.type;

public enum MsgId_E {
	MSGID_NULL((short) 0), MSGID_SQL((short) 1), // SQL���
	MSGID_USER((short) 2), // �û�
	MSGID_CTRL((short) 3), // ������
	MSGID_RGN((short) 4), // ����
	MSGID_APPL((short) 5), // �ҵ�
	MSGID_CMD((short) 6), // ָ��
	MSGID_SENS((short) 7), // ��Ӧ��
	MSGID_SENSLOG((short) 8), // ��Ӧ��֪ͨ��¼
	MSGID_CAMA((short) 9), // ����ͷ
	MSGID_MODE((short) 10), // ����ģʽ
	MSGID_MODECMD((short) 11), // ����ģʽָ��
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
