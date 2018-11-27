import java.net.Socket;

public interface ServerStrategy {
	public void service(Socket socket,FileServer fs);
}
