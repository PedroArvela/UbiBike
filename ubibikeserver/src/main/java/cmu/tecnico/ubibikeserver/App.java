package cmu.tecnico.ubibikeserver;

public class App {
	public static void main(String[] args) throws Exception {
		int port = 8080;

		if (args.length < 1) {
			System.err.println("Wrong number of arguments");
			System.err.println("Usage:\tApp <decider> [ port ]");
		}

		if (args.length >= 1) {
			port = Integer.parseInt(args[0]);
		}

		Server s = new Server(port);
		s.serve();
	}
}