package define.oper.body.req;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.mac.smartcontrol.util.FormatTransfer;

public class MsgSqlExcReq_S {
	short usLen; // 对象索引，usIdx = 0查询所有对象
	String szSql;

	public MsgSqlExcReq_S(short usLen, String szSql) {
		super();
		this.usLen = usLen;
		this.szSql = szSql;
	}

	public MsgSqlExcReq_S() {
		super();
		// TODO Auto-generated constructor stub
	}

	public short getUsLen() {
		return usLen;
	}

	public void setUsLen(short usLen) {
		this.usLen = usLen;
	}

	public String getSzSql() {
		return szSql;
	}

	public void setSzSql(String szSql) {
		this.szSql = szSql;
	}

	public byte[] getMsgSqlExcReq_S() {
		ByteBuffer bb_Msg = ByteBuffer.allocate(usLen + 2);
		bb_Msg.put(FormatTransfer.toLH(usLen));
		try {
			bb_Msg.put(szSql.getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bb_Msg.array();
	}
}
