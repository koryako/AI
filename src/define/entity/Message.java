package define.entity;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class Message {
	private Message_Header message_Header;
	private byte[] usBody;
	
	public Message(Message_Header message_Header, byte[] usBody) {
		super();
		this.message_Header = message_Header;
		this.usBody = usBody;
	}

	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Message_Header getMessage_Header() {
		return message_Header;
	}

//	public void setMessage_Header(byte[] b) {
//		byte [] usMsgId_b=new byte[2];
//		System.arraycopy(b, 0, usMsgId_b, 0, 2);
//		message_Header.usMsgId=FormatTransfer.lBytesToShort(usMsgId_b);
//		message_Header.ucMsgType=b[2];
//		message_Header.ucMsgOper=b[3];
//	}

	
	public byte[] getUsBody() {
		return usBody;
	}

	public void setMessage_Header(Message_Header message_Header) {
		this.message_Header = message_Header;
	}

	public void setUsBody(byte[] usBody) {
		this.usBody = usBody;
	}

	public byte[] getMessage(int body_Size){
		ByteBuffer bb_Msg=ByteBuffer.allocate(12+body_Size);
		bb_Msg.put(FormatTransfer.toLH(message_Header.getUsSsnId()));
		bb_Msg.put(FormatTransfer.toLH(message_Header.getUsMsgId()));
		bb_Msg.put(FormatTransfer.toLH(message_Header.getUiMsgSeq()));
		bb_Msg.put(message_Header.getUcMsgType());
		bb_Msg.put(message_Header.getUcMsgOper());
		bb_Msg.put(FormatTransfer.toLH(message_Header.getUsBodySize()));
		bb_Msg.put(this.usBody);
		return bb_Msg.array();
	}
	
	public void setMessage_Body(byte[] body){
		usBody=body;
	}
}
