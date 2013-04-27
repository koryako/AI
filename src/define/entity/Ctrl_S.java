package define.entity;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class Ctrl_S {
		short usIdx;
	  	String  szName; // Ãû³Æ
	    long ulAddr;             // ¿ØÖÆÆ÷µØÖ·
		byte  ucStatus;           // ¿ØÖÆÆ÷×´Ì¬
		public Ctrl_S(short usIdx, String szName, long ulAddr, byte ucStatus) {
			super();
			this.usIdx = usIdx;
			this.szName = szName;
			this.ulAddr = ulAddr;
			this.ucStatus = ucStatus;
		}
		
		public Ctrl_S() {
			super();
			// TODO Auto-generated constructor stub
		}

		public static short getSize(){
			return 43;
		}
		public short getUsIdx() {
			return usIdx;
		}
		public void setUsIdx(short usIdx) {
			this.usIdx = usIdx;
		}
		public String getSzName() {
			return szName;
		}
		public void setSzName(String szName) {
			this.szName = szName;
		}
		public long getUlAddr() {
			return ulAddr;
		}
		public void setUlAddr(long ulAddr) {
			this.ulAddr = ulAddr;
		}
		public byte getUcStatus() {
			return ucStatus;
		}
		public void setUcStatus(byte ucStatus) {
			this.ucStatus = ucStatus;
		}
		public byte[] getCtrl_S(){
			ByteBuffer bb_Msg=ByteBuffer.allocate(43);
			bb_Msg.put(FormatTransfer.toLH(usIdx));
			int szName_Len=szName.getBytes().length;
			bb_Msg.put(szName.getBytes());
			if(szName_Len<32){
				byte[] szName_Sub=new byte[32-szName_Len];
				bb_Msg.put(szName_Sub);
			}
			bb_Msg.put(FormatTransfer.toLH((ulAddr)));
			bb_Msg.put(ucStatus);
			return bb_Msg.array();
		}
		
		public void setCtrl_S(byte[] b){
			byte [] usIdx_b=new byte[2];
			System.arraycopy(b, 0, usIdx_b, 0, 2);
			usIdx=FormatTransfer.lBytesToShort(usIdx_b);
			
			byte [] szName_b=new byte[32];
			System.arraycopy(b, 2, szName_b, 0, 32);
			try {
				szName=new String(szName_b,"gbk").trim();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			byte [] ulAddr_b=new byte[32];
			System.arraycopy(b, 34, ulAddr_b, 0, 8);
			ulAddr=FormatTransfer.lBytesToLong(ulAddr_b);
			
			ucStatus=b[42];
		}
}
