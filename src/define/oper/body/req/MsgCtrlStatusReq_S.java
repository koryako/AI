package define.oper.body.req;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.mac.smartcontrol.util.FormatTransfer;

import define.entity.CtrlStatus_S;

public class MsgCtrlStatusReq_S {
	short usIdx; // ¶ÔÏóË÷Òý
	short usCnt;
	public List<CtrlStatus_S> pucData = new ArrayList<CtrlStatus_S>();;

	public short getUsIdx() {
		return usIdx;
	}

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}

	public short getUsCnt() {
		return usCnt;
	}

	public short getSize() {
		return (short) (2 + pucData.size() * CtrlStatus_S.getSize());
	}

	public void setUsCnt(short usCnt) {
		this.usCnt = usCnt;
	}

	public byte[] getMsgCtrlStatusReq_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(getSize());
		bb_Msg.put(FormatTransfer.toLH(usIdx));
		bb_Msg.put(FormatTransfer.toLH(usCnt));
		for (int i = 0; i < pucData.size(); i++) {
			bb_Msg.put(pucData.get(i).getCtrlStatus_S());
		}
		return bb_Msg.array();
	}
}
