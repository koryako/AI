package define.oper.body.ack;

public class MsgCtrlTestAck_S {
	short usIdx;       // 对象索引
    byte  ucCode;      // 指令码
    int uiPara;      // 指令参数
    byte  usError;
    
	private MsgCtrlTestAck_S(short usIdx, byte ucCode, int uiPara, byte usError) {
		super();
		this.usIdx = usIdx;
		this.ucCode = ucCode;
		this.uiPara = uiPara;
		this.usError = usError;
	}
	public short getUsIdx() {
		return usIdx;
	}
	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}
	public byte getUcCode() {
		return ucCode;
	}
	public void setUcCode(byte ucCode) {
		this.ucCode = ucCode;
	}
	public int getUiPara() {
		return uiPara;
	}
	public void setUiPara(int uiPara) {
		this.uiPara = uiPara;
	}
	public byte getUsError() {
		return usError;
	}
	public void setUsError(byte usError) {
		this.usError = usError;
	}  
    
    
}
