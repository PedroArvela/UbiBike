package cmu.tecnico.ubibikeserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import cmu.tecnico.ubibikeserver.domain.Manager;
import cmu.tecnico.ubibikeserver.domain.Trajectory;
import cmu.tecnico.ubibikeserver.domain.User;
import cmu.tecnico.ubibikeserver.domain.exceptions.NotFoundException;

public class Server {
	private int port;
	ExecutorService threadPool;

	Manager manager;

	public Server(int port) {
		this.port = port;
		this.threadPool = Executors.newCachedThreadPool();

		this.manager = Manager.getInstance();
	}

	private Pair<Integer, List<String>> processUser(List<String> path, Map<String, String> queries) {
		int code = 200;
		List<String> result = new ArrayList<String>();

		try {
			if (path.size() < 3) {
				code = 400;
				result.add("Missing arguments");
			} else {
				User user = manager.getUser(path.get(1));

				switch (path.get(2)) {
				case "password":
					result.add(user.password);
					break;
				case "username":
					result.add(user.username);
					break;
				case "name":
					result.add(user.displayName);
					break;
				case "points":
					result.add(new Integer(user.points).toString());
					break;
				case "trajectories":
					if (path.size() == 3) {
						for (String date : user.trajectories.keySet()) {
							result.add(date);
						}
					} else {
						Trajectory t = user.trajectories.get(path.get(3));
						result.add(t.date);
						for(String coord : t.coordinates) {
							result.add(coord);
						}
					}
					break;
				default:
					throw new NotFoundException(path.get(2));
				}
			}
		} catch (NotFoundException e) {
			code = 404;
			result.clear();
			result.add("User not found");
		}

		return new ImmutablePair<Integer, List<String>>(code, result);
	}

	private Pair<Integer, List<String>> registerUser(List<String> path, Map<String, String> queries) {
		int code = 200;
		List<String> result = new ArrayList<String>();

		if (queries.containsKey("username") && queries.containsKey("password") && queries.containsKey("name")) {
			String username = queries.get("username");
			String password = queries.get("password");
			String displayName = queries.get("name");

			if (!manager.users.containsKey(username)) {
				User user = new User(username, password, displayName);

				manager.users.put(username, user);
				result.add("OK");
			} else {
				code = 301;
				result.add("Forbidden");
			}
		} else {
			code = 400;
			result.add("Malformed request");
		}

		return new ImmutablePair<Integer, List<String>>(code, result);

	}

	private Pair<Integer, List<String>> processStation(List<String> path, Map<String, String> queries) {
		int code = 200;
		List<String> result = new ArrayList<String>();

		return new ImmutablePair<Integer, List<String>>(code, result);
	}

	public void process(Socket s) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));

		// Parse client request
		String request = in.readLine();
		String[] requestParams = request.split(" ");
		String pathQueryString = requestParams[1];
		String[] pathQuery = pathQueryString.split("\\?", 2);

		String pathString = pathQuery[0];
		String queryString = "";

		if (pathQuery.length == 2) {
			queryString = pathQuery[1];
		}

		List<String> path = processPath(pathString);
		Map<String, String> queries = processQueries(queryString);

		Pair<Integer, List<String>> response;

		System.out.println(path);
		System.out.println(queries);

		if (path.size() >= 2) {
			switch (path.get(0)) {
			case "user":
				response = processUser(path, queries);
				break;
			case "station":
				response = processStation(path, queries);
				break;
			default:
				response = new ImmutablePair<Integer, List<String>>(404, new ArrayList<String>());
				response.getRight().add("Resource not found");
			}
		} else if (path.size() == 1) {
			switch (path.get(0)) {
			case "register":
				response = registerUser(path, queries);
				break;
			default:
				response = new ImmutablePair<Integer, List<String>>(404, new ArrayList<String>());
				response.getRight().add("Resource not found");
			}
		} else {
			response = new ImmutablePair<Integer, List<String>>(400, new ArrayList<String>());
			response.getRight().add("Malformed request");
		}

		System.out.println("Response: [" + response.getLeft() + "]");

		out.print("HTTP/1.1 " + response.getLeft() + " \r\n");
		out.print("Content-Type: text/plain; charset=utf-8\r\n");
		out.print("Connection: close\r\n");
		out.print("\r\n");

		for (String responseLine : response.getRight()) {
			out.println(responseLine);
		}

		out.close();
		in.close();
		s.close();
	}

	public void serve() throws IOException {
		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("Listening on port " + port + "...");

			while (true) {
				final Socket socket = server.accept();
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							process(socket);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
		}
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