import java.io.Serializable;

public class ClientMessage implements Serializable{
	private String name;
	private String uuid;

	public ClientMessage() {
		// TODO �Զ����ɵĹ��캯�����
	}
	public ClientMessage(String uuid,String name) {
		// TODO �Զ����ɵĹ��캯�����
		this.name = name;
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
