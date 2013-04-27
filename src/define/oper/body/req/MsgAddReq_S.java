package define.oper.body.req;

public class MsgAddReq_S {
	byte[] pucData;

	private MsgAddReq_S(byte[] pucData) {
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
