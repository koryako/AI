package define.oper.body.req;

import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;


public class MsgCmdQryByDevReq_S {
	byte ucDevType;     // �豸����
	short usDevIdx;  // �豸���ҵ硢��Ӧ��������ͷ�����豸���;��������豸������
	

	
	public MsgCmdQryByDevReq_S() {
		super();
		// TODO Auto-generated constructor stub
	}



	public MsgCmdQryByDevReq_S(byte ucDevType, short usDevIdx) {
		super();
		this.ucDevType = ucDevType;
		this.usDevIdx = usDevIdx;
	}
	
	public static short getSize(){
		return 3;
	}


	public short getUcDevType() {
		return ucDevType;
	}



	public void setUcDevType(byte ucDevType) {
		this.ucDevType = ucDevType;
	}



	public short getUsDevIdx() {
		return usDevIdx;
	}



	public void setUsDevIdx(short usDevIdx) {
		this.usDevIdx = usDevIdx;
	}



	public byte[] getMsgCmdQryByDevReq_S(){
		ByteBuffer bb_Msg=ByteBuffer.allocate(3);
		bb_Msg.put(ucDevType);
		bb_Msg.put(FormatTransfer.toLH(usDevIdx));
		return bb_Msg.array();
	}
	
}
