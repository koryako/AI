package define.type;

public enum CmdCode_E {
	CMD_CODE_NULL((byte) 0), CMD_CODE_POWER_CTRL((byte) 1), // 电源控制，uiPara=1开，关
	CMD_CODE_INFRARED_CTRL((byte) 2), // 红外控制，uiPara=红外码
	CMD_CODE_SENS_CTRL((byte) 3), // 感应器控制，uiPara=1开，关
	CMD_CODE_SENS_NOTIFY((byte) 4), // 感应器被触发通知到客户端，uiPara=0
	CMD_CODE_SENS_EXCCMD((byte) 5), // 感应器被触发时执行指令，uiPara=指令索引
	CMD_CODE_SENS_EXCMODECMD((byte) 6); // 感应器被触发时执行智能模式，uiPara=智能模式索引

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private CmdCode_E(byte val) {
		this.val = val;
	}
}
