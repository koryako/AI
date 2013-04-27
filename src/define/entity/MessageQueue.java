package define.entity;

import java.util.ArrayList;
import java.util.List;

public class MessageQueue {
	private static List<Message> list=new ArrayList<Message>();
	public static List<Message> getList(){
		return list;
	}
	public static void add(Message message){
		list.add(message);
	}
	public static void remove(int index){
		list.remove(index);
	}
	public static void remove(Message message){
		list.remove(message);
	}
}
