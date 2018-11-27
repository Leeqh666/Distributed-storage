import java.io.Serializable;

public class ClientMessage implements Serializable{
	private String name;
	private String uuid;

	public ClientMessage() {
		// TODO 自动生成的构造函数存根
	}
	public ClientMessage(String uuid,String name) {
		// TODO 自动生成的构造函数存根
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
