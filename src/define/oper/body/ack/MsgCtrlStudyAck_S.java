package define.oper.body.ack;

public class MsgCtrlStudyAck_S {
	 short usIdx;       // ��������
	 short usCmdIdx;    // ��������Ӧ��ָ������
	 byte  ucOffset;    // ������ƫ����
	 byte  usError;
	 
	private MsgCtrlStudyAck_S(short usIdx, short usCmdIdx, byte ucOffset,
			byte usError) {
		super();
		this.usIdx = usIdx;
		this.usCmdIdx = usCmdIdx;
		this.ucOffset = ucOffset;
		this.usError = usError;
	}
	public short getUsIdx() {
		return usIdx;
	}
	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}
	public short getUsCmdIdx() {
		return usCmdIdx;
	}
	public void setUsCmdIdx(short usCmdIdx) {
		this.usCmdIdx = usCmdIdx;
	}
	public byte getUcOffset() {
		return ucOffset;
	}
	public void setUcOffset(byte ucOffset) {
		this.ucOffset = ucOffset;
	}
	public byte getUsError() {
		return usError;
	}
	public void setUsError(byte usError) {
		this.usError = usError;
	}  
	 
}
