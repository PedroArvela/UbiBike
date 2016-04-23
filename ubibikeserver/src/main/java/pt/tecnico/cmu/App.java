package pt.tecnico.cmu;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class App {

	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/", new WebHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
	}

	static class WebHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange t) throws IOException {
			String query = t.getRequestURI().getQuery();
			// TODO: process response according to requested query
			String response = "Response";

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
		}

	}

}
