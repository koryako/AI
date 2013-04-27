package define.oper.body.ack;

public class MsgModeExcAck_S {
	short usIdx;       // ¶ÔÏóË÷Òý
    byte  usError;
    
	private MsgModeExcAck_S(short usIdx, byte usError) {
		super();
		this.usIdx = usIdx;
		this.usError = usError;
	}
	public short getUsIdx() {
		return usIdx;
	}
	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}
	public byte getUsError() {
		return usError;
	}
	public void setUsError(byte usError) {
		this.usError = usError;
	}  
    
    
}
