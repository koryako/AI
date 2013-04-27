package define.oper.body.req;

import java.nio.ByteBuffer;

public class MsgUserLoginReq_S {
	String  szName;  // ”√ªß√˚
    String 	szPass;  // √‹¬Î
    
    public MsgUserLoginReq_S(String szName, String szPass) {
		super();
		this.szName = szName;
		this.szPass = szPass;
	}
    
	public String getSzName() {
		return szName;
	}
	public static short getSize(){
		return 64;
	}
	public MsgUserLoginReq_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setSzName(String szName) {
		this.szName = szName;
	}
	public String getSzPass() {
		return szPass;
	}
	public void setSzPass(String szPass) {
		this.szPass = szPass;
	}
	public byte[] getMsgUserLoginReq_S(){
		ByteBuffer bb_Msg=ByteBuffer.allocate(64);
		
		int szName_Len=szName.getBytes().length;
		bb_Msg.put(szName.getBytes());
		if(szName_Len<32){
			byte[] szName_Sub=new byte[32-szName_Len];
			bb_Msg.put(szName_Sub);
		}
		
		int szPass_Len=szPass.getBytes().length;
		bb_Msg.put(szPass.getBytes());
		if(szPass_Len<32){
			byte[] szPass_Sub=new byte[32-szPass_Len];
			bb_Msg.put(szPass_Sub);
		}
		return bb_Msg.array();
	}
    
}
