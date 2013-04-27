package define.oper.body.ack;

import java.nio.ByteBuffer;

public class MsgUserLoginAck_S {
	String  szName;  // ÓÃ»§Ãû
	byte ucType;
    byte  usError;
    
    public MsgUserLoginAck_S() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MsgUserLoginAck_S(String szName, byte ucType, byte usError) {
		super();
		this.szName = szName;
		this.ucType = ucType;
		this.usError = usError;
	}
	public static short getSize(){
		return 34;
	}
	public String getSzName() {
		return szName;
	}
	public void setSzName(String szName) {
		this.szName = szName;
	}
	public byte getUsError() {
		return usError;
	}
	public void setUsError(byte usError) {
		this.usError = usError;
	}
	public byte getUcType() {
		return ucType;
	}
	public void setUcType(byte ucType) {
		this.ucType = ucType;
	}  
	
	public byte[] getMsgUserLoginAck_S(){
		ByteBuffer bb_Msg=ByteBuffer.allocate(getSize());
		
		int szName_Len=szName.getBytes().length;
		bb_Msg.put(szName.getBytes());
		if(szName_Len<32){
			byte[] szName_Sub=new byte[32-szName_Len];
			bb_Msg.put(szName_Sub);
		}
		
		bb_Msg.put(ucType);
		bb_Msg.put(usError);
		return bb_Msg.array();
	}
	
	public void setMsgUserLoginAck_S(byte[] b){
		byte [] szName_b=new byte[32];
		System.arraycopy(b, 0, szName_b, 0, 32);
		szName=new String(szName_b).trim();
		
		ucType=b[32];
		
		usError=b[33];
	}
	
    
}
