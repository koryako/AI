package define.type;

public enum CmdCode_E {
	CMD_CODE_NULL((byte) 0), CMD_CODE_POWER_CTRL((byte) 1), // ��Դ���ƣ�uiPara=1������
	CMD_CODE_INFRARED_CTRL((byte) 2), // ������ƣ�uiPara=������
	CMD_CODE_SENS_CTRL((byte) 3), // ��Ӧ�����ƣ�uiPara=1������
	CMD_CODE_SENS_NOTIFY((byte) 4), // ��Ӧ��������֪ͨ���ͻ��ˣ�uiPara=0
	CMD_CODE_SENS_EXCCMD((byte) 5), // ��Ӧ��������ʱִ��ָ�uiPara=ָ������
	CMD_CODE_SENS_EXCMODECMD((byte) 6); // ��Ӧ��������ʱִ������ģʽ��uiPara=����ģʽ����

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private CmdCode_E(byte val) {
		this.val = val;
	}
}
