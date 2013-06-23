package define.type;

public enum SensType_E {
	SENS_TYPE_NULL((byte) 0), SENS_TYPE_GAS((byte) 1), // ú����Ӧ
	SENS_TYPE_SMOKE((byte) 2), // �����Ӧ
	SENS_TYPE_TEMP((byte) 3), // �¶ȸ�Ӧ
	SENS_TYPE_MOIS((byte) 4), // ʪ�ȸ�Ӧ
	SENS_TYPE_LIGHT((byte) 5);// ��ȸ�Ӧ

	private byte val;

	public byte getVal() {
		return this.val;
	}

	private SensType_E(byte val) {
		this.val = val;
	}

}
