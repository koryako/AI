package define.oper.body.ack;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgSensQryByRgnAck_S {
	  short usRgnIdx;       // 区域索引
	  byte ucErr;  
	  short usCnt;          // 对象数量
	  byte[]pucData;     // 对象结构，多个
	  public MsgSensQryByRgnAck_S() {
		super();
		// TODO Auto-generated constructor stub
	}
	  public MsgSensQryByRgnAck_S(short usRgnIdx, byte ucErr, short usCnt,
			byte[] pucData) {
		super();
		this.usRgnIdx = usRgnIdx;
		this.ucErr = ucErr;
		this.usCnt = usCnt;
		this.pucData = pucData;
	  }
	public short getUsRgnIdx() {
		return usRgnIdx;
	}
	public void setUsRgnIdx(short usRgnIdx) {
		this.usRgnIdx = usRgnIdx;
	}
	public byte getUcErr() {
		return ucErr;
	}
	public void setUcErr(byte ucErr) {
		this.ucErr = ucErr;
	}
	public short getUsCnt() {
		return usCnt;
	}
	public void setUsCnt(short usCnt) {
		this.usCnt = usCnt;
	}
	public byte[] getPucData() {
		return pucData;
	}
	public void setPucData(byte[] pucData) {
		this.pucData = pucData;
	}
	public void setMsgSensQryByRgnAck_S(byte[] b){
		  
		  	byte [] usRgnIdx_b=new byte[2];
			System.arraycopy(b, 0, usRgnIdx_b, 0, 2);
			usRgnIdx=FormatTransfer.lBytesToShort(usRgnIdx_b);
			
			
			ucErr=b[2];
			
			byte [] usCnt_b=new byte[2];
			System.arraycopy(b, 3, usCnt_b, 0, 2);
			usCnt=FormatTransfer.lBytesToShort(usCnt_b);
			
			byte [] pucData_b=new byte[b.length-6];
			System.arraycopy(b, 5, pucData_b, 0, b.length-6);
			pucData=pucData_b;
			
	}
}
