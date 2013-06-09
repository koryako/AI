package define.type;

import android.content.Context;
import android.widget.Toast;

public enum ErrCode_E {
	ERRCODE_SUCCESS((byte) 0), // 成功
	ERRCODE_FAILED((byte) 1), // 失败
	ERRCODE_TIMEOUT((byte) 2), ERRCODE_INVALID_INDEX((byte) 3), // 无效主索引
	ERRCODE_INVALID_USERIDX((byte) 4), // 无效的用户索引
	ERRCODE_INVALID_RGNIDX((byte) 5), // 无效的区域索引
	ERRCODE_INVALID_CTRLIDX((byte) 6), // 无效的控制器索引
	ERRCODE_INVALID_APPLIDX((byte) 7), // 无效的家电索引
	ERRCODE_INVALID_CAMAIDX((byte) 8), // 无效的摄像头索引
	ERRCODE_INVALID_SENEIDX((byte) 9), // 无效的感应类型
	ERRCODE_INVALID_DEVIDX((byte) 10), // 无效的设备类型
	ERRCODE_INVALID_CMDIDX((byte) 11), // 无效的指令索引
	ERRCODE_INVALID_MODEIDX((byte) 12), // 无效的智能模式索引
	ERRCODE_INVALID_MODECMDIDX((byte) 13), // 无效的智能模式指令索引

	ERRCODE_INVALID_USERTYPE((byte) 14), // 无效的用户类型
	ERRCODE_INVALID_APPLTYPE((byte) 15), // 无效的家电类型
	ERRCODE_INVALID_DEVTYPE((byte) 16), // 无效的设备类型
	ERRCODE_INVALID_SENSTYPE((byte) 17), // 无效的感应类型
	ERRCODE_INVALID_CTRLADDR((byte) 18), // 无效的控制器地址
	ERRCODE_INVALID_CMDTYPE((byte) 19), // 无效的指令类型
	ERRCODE_INVALID_CMDCODE((byte) 20), // 无效的指令代码
	ERRCODE_INVALID_REDOFFSET((byte) 21), // 无效的红外偏移码

	ERRCODE_NAME_EXIST((byte) 22), // 名称存在
	ERRCODE_NAME_NOTEXIST((byte) 23), // 名称不存在
	ERRCODE_NAME_EMPTY((byte) 24), // 名称为空
	ERRCODE_VOICE_EXIST((byte) 25), // 语音存在
	ERRCODE_VOICE_NOTEXIST((byte) 26), // 语音不存在
	ERRCODE_VOICE_EMPTY((byte) 27), // 语音为空
	ERRCODE_MODECMD_EXIST((byte) 28), // 智能模式下指令存在
	ERRCODE_MODECMD_NOTEXIST((byte) 29), // 智能模式下指令不存在
	ERRCODE_ADD_REFUSE((byte) 30), // 增加拒绝
	ERRCODE_DEL_REFUSE((byte) 31), // 删除拒绝
	ERRCODE_PASS_ERROR((byte) 32), // 密码错误
	ERRCODE_IPADDR_ZERO((byte) 33), // IP地址为
	ERRCODE_PORT_ZERO((byte) 34); // 端口为

	private byte val;

	public byte getVal() {
		return val;
	}

	private ErrCode_E(byte val) {
		this.val = val;
	}

	public static void showError(Context context, byte errorCode) {
		switch (errorCode) {
		case 1:
			Toast.makeText(context, "失败", Toast.LENGTH_LONG).show();
			break;
		case 2:
			Toast.makeText(context, "处理超时", Toast.LENGTH_LONG).show();
			break;
		case 3:
			Toast.makeText(context, "无效主索引", Toast.LENGTH_LONG).show();
			break;
		case 4:
			Toast.makeText(context, "无效的用户索引", Toast.LENGTH_LONG).show();
			break;
		case 5:
			Toast.makeText(context, "无效的区域索引", Toast.LENGTH_LONG).show();
			break;
		case 6:
			Toast.makeText(context, "无效的控制器索引", Toast.LENGTH_LONG).show();
			break;
		case 7:
			Toast.makeText(context, "无效的家电索引", Toast.LENGTH_LONG).show();
			break;
		case 8:
			Toast.makeText(context, "无效的摄像头索引", Toast.LENGTH_LONG).show();
			break;
		case 9:
			Toast.makeText(context, "无效的感应类型", Toast.LENGTH_LONG).show();
			break;
		case 10:
			Toast.makeText(context, "无效的设备类型", Toast.LENGTH_LONG).show();
			break;
		case 11:
			Toast.makeText(context, "无效的指令索引", Toast.LENGTH_LONG).show();
			break;
		case 12:
			Toast.makeText(context, "无效的智能模式索引", Toast.LENGTH_LONG).show();
			break;
		case 13:
			Toast.makeText(context, "无效的智能模式指令索引", Toast.LENGTH_LONG).show();
			break;
		case 14:
			Toast.makeText(context, "无效的用户类型", Toast.LENGTH_LONG).show();
			break;
		case 15:
			Toast.makeText(context, "无效的家电类型", Toast.LENGTH_LONG).show();
			break;
		case 16:
			Toast.makeText(context, "无效的设备类型", Toast.LENGTH_LONG).show();
			break;
		case 17:
			Toast.makeText(context, "无效的感应类型", Toast.LENGTH_LONG).show();
			break;
		case 18:
			Toast.makeText(context, "无效的控制器地址", Toast.LENGTH_LONG).show();
			break;
		case 19:
			Toast.makeText(context, "无效的指令类型", Toast.LENGTH_LONG).show();
			break;
		case 20:
			Toast.makeText(context, "无效的指令代码", Toast.LENGTH_LONG).show();
			break;
		case 21:
			Toast.makeText(context, "无效的红外偏移码", Toast.LENGTH_LONG).show();
			break;
		case 22:
			Toast.makeText(context, "名称存在", Toast.LENGTH_LONG).show();
			break;

		case 23:
			Toast.makeText(context, "名称不存在", Toast.LENGTH_LONG).show();
			break;
		case 24:
			Toast.makeText(context, "名称为空", Toast.LENGTH_LONG).show();
			break;
		case 25:
			Toast.makeText(context, "语音存在", Toast.LENGTH_LONG).show();
			break;
		case 26:
			Toast.makeText(context, "语音不存在", Toast.LENGTH_LONG).show();
			break;
		case 27:
			Toast.makeText(context, "语音为空", Toast.LENGTH_LONG).show();
			break;

		case 28:
			Toast.makeText(context, "智能模式下指令存在", Toast.LENGTH_LONG).show();
			break;
		case 29:
			Toast.makeText(context, "智能模式下指令不存在", Toast.LENGTH_LONG).show();
			break;
		case 30:
			Toast.makeText(context, "增加拒绝", Toast.LENGTH_LONG).show();
			break;
		case 31:
			Toast.makeText(context, "删除拒绝", Toast.LENGTH_LONG).show();
			break;
		case 32:
			Toast.makeText(context, "密码错误", Toast.LENGTH_LONG).show();
			break;
		case 33:
			Toast.makeText(context, "IP地址为", Toast.LENGTH_LONG).show();
			break;
		case 34:
			Toast.makeText(context, "智能模式下指令存在", Toast.LENGTH_LONG).show();
			break;
		case 35:
			Toast.makeText(context, "端口为", Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
	}
}
