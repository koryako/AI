package define.entity;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class Message_Header {
	short usSsnId;
	short usMsgId;
	int uiMsgSeq;
	byte ucMsgType;
	byte ucMsgOper;
	short usBodySize;
	
	
	public Message_Header(short usSsnId, short usMsgId, int uiMsgSeq,
			byte ucMsgType, byte ucMsgOper, short usBodySize) {
		super();
		this.usSsnId = usSsnId;
		this.usMsgId = usMsgId;
		this.uiMsgSeq = uiMsgSeq;
		this.ucMsgType = ucMsgType;
		this.ucMsgOper = ucMsgOper;
		this.usBodySize = usBodySize;
	}

	public Message_Header() {
		super();
		// TODO Auto-generated constructor stub
	}

	public short getUsMsgId() {
		return usMsgId;
	}
	public void setUsMsgId(short usMsgId) {
		this.usMsgId = usMsgId;
	}
	public byte getUcMsgType() {
		return ucMsgType;
	}
	public void setUcMsgType(byte ucMsgType) {
		this.ucMsgType = ucMsgType;
	}
	public byte getUcMsgOper() {
		return ucMsgOper;
	}
	public void setUcMsgOper(byte ucMsgOper) {
		this.ucMsgOper = ucMsgOper;
	}
	public short getUsBodySize() {
		return usBodySize;
	}
	public void setUsBodySize(short usBodySize) {
		this.usBodySize = usBodySize;
	}
	
	
	public short getUsSsnId() {
		return usSsnId;
	}

	public void setUsSsnId(short usSsnId) {
		this.usSsnId = usSsnId;
	}

	public int getUiMsgSeq() {
		return uiMsgSeq;
	}

	public void setUiMsgSeq(int uiMsgSeq) {
		this.uiMsgSeq = uiMsgSeq;
	}

	public byte[] getMessage_Header(){
		ByteBuffer bb_Msg=ByteBuffer.allocate(12);
		bb_Msg.put(FormatTransfer.toLH(usSsnId));
		bb_Msg.put(FormatTransfer.toLH(usMsgId));
		bb_Msg.put(FormatTransfer.toLH(uiMsgSeq));
		bb_Msg.put(ucMsgType);
		bb_Msg.put(ucMsgOper);
		bb_Msg.put(FormatTransfer.toLH((usBodySize)));
		return bb_Msg.array();
	}	
	
	public void setMessage_Header(byte[] b){
		byte [] usSsnId_b=new byte[2];
		System.arraycopy(b, 0, usSsnId_b, 0, 2);
		usSsnId=FormatTransfer.lBytesToShort(usSsnId_b);
		
		
		byte [] usMsgId_b=new byte[2];
		System.arraycopy(b, 2, usMsgId_b, 0, 2);
		usMsgId=FormatTransfer.lBytesToShort(usMsgId_b);
		
		byte [] uiMsgSeq_b=new byte[4];
		System.arraycopy(b,4, uiMsgSeq_b, 0, 4);
		uiMsgSeq=FormatTransfer.lBytesToShort(uiMsgSeq_b);
		
		
		ucMsgType=b[8];
		
		ucMsgOper=b[9];
		
		byte [] usBodySize_b=new byte[2];
		System.arraycopy(b, 10, usBodySize_b, 0, 2);
		usBodySize=FormatTransfer.lBytesToShort(usBodySize_b);
		
		
	}
}
