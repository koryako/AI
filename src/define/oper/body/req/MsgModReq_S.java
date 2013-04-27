package define.oper.body.req;

public class MsgModReq_S {
	byte[] pucData;

	private MsgModReq_S(byte[] pucData) {
		super();
		this.pucData = pucData;
	}

	public byte[] getPucData() {
		return pucData;
	}

	public void setPucData(byte[] pucData) {
		this.pucData = pucData;
	}
	
}
