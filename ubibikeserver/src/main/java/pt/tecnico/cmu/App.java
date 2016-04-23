package pt.tecnico.cmu;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
			String pathString = t.getRequestURI().getPath();
			String queryString = t.getRequestURI().getQuery();

			List<String> path = processPath(pathString);
			Map<String, String> queries = processQueries(queryString);

			String response = "";

			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

		public static List<String> processPath(String pathString) {
			List<String> components = new ArrayList<String>();

			if (pathString == null) {
				pathString = "";
			}

			for (String component : pathString.split("/")) {
				if (!component.isEmpty()) {
					components.add(component);
				}
			}

			return components;
		}

		public static Map<String, String> processQueries(String queryString) {
			Map<String, String> queries = new HashMap<String, String>();

			if (queryString == null) {
				queryString = "";
			}

			for (String query : queryString.split("&")) {
				if (!query.isEmpty()) {
					String[] queryArgs = query.split("=", 2);
					if (queryArgs.length == 2) {
						queries.put(queryArgs[0], queryArgs[1]);
					} else {
						queries.put(queryArgs[0], "true");
					}
				}
			}

			return queries;
		}

	}

}
