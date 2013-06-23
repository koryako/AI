package com.mac.smartcontrol.thread;

import java.io.IOException;
import java.util.Arrays;

import android.app.Service;
import android.content.Intent;

import com.mac.smartcontrol.SocketService;
import com.mac.smartcontrol.util.FormatTransfer;

import define.entity.Message;
import define.entity.MessageQueue;
import define.entity.Message_Header;

public class ReadRunnable implements Runnable {

	public boolean isRun = true;
	Service service;

	public ReadRunnable(Service service) {
		super();
		this.service = service;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (isRun) {
			byte[] ack_body_b = null;
			byte[] message_Header = new byte[12];
			try {

				try {
					SocketService.is.read(message_Header);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					isRun = false;
				}
				Message_Header ack_message_Header = new Message_Header();
				ack_message_Header
						.setUsSsnId(FormatTransfer.lBytesToShort(Arrays
								.copyOfRange(message_Header, 0, 2)));
				ack_message_Header
						.setUsMsgId(FormatTransfer.lBytesToShort(Arrays
								.copyOfRange(message_Header, 2, 4)));
				ack_message_Header
						.setUiMsgSeq(FormatTransfer.lBytesToShort(Arrays
								.copyOfRange(message_Header, 4, 8)));
				ack_message_Header.setUcMsgType(message_Header[8]);
				ack_message_Header.setUcMsgOper(message_Header[9]);
				ack_message_Header.setUsBodySize(FormatTransfer
						.lBytesToShort(Arrays.copyOfRange(message_Header, 10,
								12)));
				ack_body_b = new byte[ack_message_Header.getUsBodySize()];
				SocketService.is.read(ack_body_b);
				Message ack_message = new Message(ack_message_Header,
						ack_body_b);
				synchronized (MessageQueue.getList()) {
					MessageQueue.add(ack_message);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				isRun = true;
				service.sendBroadcast(new Intent("IO_Exception"));
			}
		}
	}

}
