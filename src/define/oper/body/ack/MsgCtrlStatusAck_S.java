package define.oper.body.ack;

import java.util.ArrayList;
import java.util.List;

import com.mac.smartcontrol.util.FormatTransfer;

import define.entity.CtrlStatus_S;

public class MsgCtrlStatusAck_S {
	short usIdx; // 对象索引
	byte ucErr; // 控制器对应的指令索引
	short usCnt; // 红外码偏移码
	public List<CtrlStatus_S> pucData = new ArrayList<CtrlStatus_S>();

	public MsgCtrlStatusAck_S(short usIdx, byte ucErr, short usCnt,
			List<CtrlStatus_S> pucData) {
		super();
		this.usIdx = usIdx;
		this.ucErr = ucErr;
		this.usCnt = usCnt;
		this.pucData = pucData;
	}

	public MsgCtrlStatusAck_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public short getUsIdx() {
		return usIdx;
	}

	public short getSize() {
		return 6;
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

	public void setUsIdx(short usIdx) {
		this.usIdx = usIdx;
	}

	public void setMsgCtrlStatusAck_S(byte[] b) {

		byte[] usIdx_b = new byte[2];
		System.arraycopy(b, 0, usIdx_b, 0, 2);
		usIdx = FormatTransfer.lBytesToShort(usIdx_b);

		ucErr = b[2];

		byte[] usCnt_b = new byte[2];
		System.arraycopy(b, 3, usCnt_b, 0, 2);
		usCnt = FormatTransfer.lBytesToShort(usCnt_b);

		for (int i = 0; i < usCnt; i++) {
			byte[] c_s_b = new byte[CtrlStatus_S.getSize()];
			System.arraycopy(b, (5 + i * CtrlStatus_S.getSize()), c_s_b, 0,
					CtrlStatus_S.getSize());
			CtrlStatus_S ctrlStatus_S = new CtrlStatus_S();
			ctrlStatus_S.setCtrlStatus_S(c_s_b);
			pucData.add(ctrlStatus_S);
		}
	}
}
