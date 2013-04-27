package com.mac.smartcontrol.util;

import java.io.IOException;

import com.mac.smartcontrol.SocketService;

import define.entity.Message;
import define.entity.Message_Header;

public class WriteUtil {
	public static short usSsnId = 0;

	public static void write(short usMsgId, int uiMsgSeq, byte ucMsgType,
			byte ucMsgOper, short usBodySize, byte[] body) throws IOException {
		Message_Header send_message_Header = new Message_Header(usSsnId,
				usMsgId, uiMsgSeq, ucMsgType, ucMsgOper, usBodySize);
		Message send_Message = new Message(send_message_Header, body);
		send_Message.setMessage_Header(send_message_Header);
		SocketService.os.write(send_Message.getMessage(usBodySize));
	}
}
