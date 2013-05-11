package define.type;

import android.content.Context;
import android.widget.Toast;

public enum ErrCode_E {
	ERRCODE_SUCCESS((byte) 0), // �ɹ�
	ERRCODE_FAILED((byte) 1), // ʧ��
	ERRCODE_TIMEOUT((byte) 2), ERRCODE_INVALID_INDEX((byte) 3), // ��Ч������
	ERRCODE_INVALID_USERIDX((byte) 4), // ��Ч���û�����
	ERRCODE_INVALID_RGNIDX((byte) 5), // ��Ч����������
	ERRCODE_INVALID_CTRLIDX((byte) 6), // ��Ч�Ŀ���������
	ERRCODE_INVALID_APPLIDX((byte) 7), // ��Ч�ļҵ�����
	ERRCODE_INVALID_CAMAIDX((byte) 8), // ��Ч������ͷ����
	ERRCODE_INVALID_SENEIDX((byte) 9), // ��Ч�ĸ�Ӧ����
	ERRCODE_INVALID_CMDIDX((byte) 10), // ��Ч��ָ������
	ERRCODE_INVALID_MODEIDX((byte) 11), // ��Ч������ģʽ����
	ERRCODE_INVALID_MODECMDIDX((byte) 12), // ��Ч������ģʽָ������

	ERRCODE_INVALID_USERTYPE((byte) 13), // ��Ч���û�����
	ERRCODE_INVALID_APPLTYPE((byte) 14), // ��Ч�ļҵ�����
	ERRCODE_INVALID_CMDTYPE((byte) 15), // ��Ч��ָ������
	ERRCODE_INVALID_SENETYPE((byte) 16), // ��Ч�ĸ�Ӧ����
	ERRCODE_INVALID_CTRLADDR((byte) 17), // ��Ч�Ŀ�������ַ
	ERRCODE_INVALID_CMDCODE((byte) 18), // ��Ч��ָ�����

	ERRCODE_NAME_EXIST((byte) 19), // ���ƴ���
	ERRCODE_NAME_NOTEXIST((byte) 20), // ���Ʋ�����
	ERRCODE_NAME_EMPTY((byte) 21), // ����Ϊ��
	ERRCODE_VOICE_EXIST((byte) 22), // ��������
	ERRCODE_VOICE_NOTEXIST((byte) 23), // ����������
	ERRCODE_VOICE_EMPTY((byte) 24), // ����Ϊ��
	ERRCODE_MODECMD_EXIST((byte) 25), // ����ģʽ��ָ�����
	ERRCODE_MODECMD_NOTEXIST((byte) 26), // ����ģʽ��ָ�����
	ERRCODE_ADD_REFUSE((byte) 27), // ���Ӿܾ�
	ERRCODE_DEL_REFUSE((byte) 28), // ɾ���ܾ�
	ERRCODE_PASS_ERROR((byte) 29), // �������
	ERRCODE_IPADDR_ZERO((byte) 30), // IP��ַΪ
	ERRCODE_PORT_ZERO((byte) 31); // �˿�Ϊ

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
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 2:
			Toast.makeText(context, "����ʱ", Toast.LENGTH_LONG).show();
			break;
		case 3:
			Toast.makeText(context, "��Ч������", Toast.LENGTH_LONG).show();
			break;
		case 4:
			Toast.makeText(context, "��Ч���û�����", Toast.LENGTH_LONG).show();
			break;
		case 5:
			Toast.makeText(context, "��Ч����������", Toast.LENGTH_LONG).show();
			break;
		case 6:
			Toast.makeText(context, "��Ч�Ŀ���������", Toast.LENGTH_LONG).show();
			break;
		case 7:
			Toast.makeText(context, "��Ч�ļҵ�����", Toast.LENGTH_LONG).show();
			break;
		case 8:
			Toast.makeText(context, "��Ч������ͷ����", Toast.LENGTH_LONG).show();
			break;
		case 9:
			Toast.makeText(context, "��Ч�ĸ�Ӧ����", Toast.LENGTH_LONG).show();
			break;
		case 10:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 11:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 12:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 13:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 14:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 15:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 16:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 17:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 18:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 19:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 20:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 21:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 22:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;

		case 23:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 24:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 25:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 26:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;
		case 27:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;

		case 28:
			Toast.makeText(context, "ʧ��", Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
	}
}
