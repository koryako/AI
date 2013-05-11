package define.type;

public enum CmdCode_E {
		CMD_CODE_NULL((byte)0),
	    CMD_CODE_POWER_CTRL((byte)1),      // ��Դ���ƣ�uiPara=1������
	    CMD_CODE_LIGHT_CTRL((byte)2),      // �ƾ߿��ƣ�uiPara=1������
	    CMD_CODE_INFRARED_CTRL((byte)3),   // ������ƣ�uiPara=������
	    CMD_CODE_SENS_CTRL((byte)4),       // ��Ӧ�����ƣ�uiPara=1������
	    CMD_CODE_SENS_NOTIFY((byte)5),     // ��Ӧ��������֪ͨ���ͻ��ˣ�uiPara=0
	    CMD_CODE_SENS_EXCCMD((byte)6),     // ��Ӧ��������ʱִ��ָ�uiPara=ָ������
	    CMD_CODE_SENS_EXCMODECMD((byte)7); // ��Ӧ��������ʱִ������ģʽ��uiPara=����ģʽ����
		
    private byte val;
	public byte getVal() {
		return this.val;
	}
	private CmdCode_E(byte val) {
		this.val = val;
	}
}
