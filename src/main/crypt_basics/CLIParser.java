package crypt_basics;

import java.util.Arrays;

import crypt_basics.Statics.Algorithm;
import crypt_basics.Statics.CLIMode;

public class CLIParser {
	private CLIMode mode;
	private Algorithm algorithm;
	private String input;
	private String output;
	private String key;
	private int bits;
	private String[] args;

	public CLIParser(String[] args) {
		this.args = args;

		handleArgs();
	}

	private void handleArgs() {
		while (args.length > 0) {
			switch (args[0]) {
			case "-A":
				handleAlgorithm();
				break;
			case "-b":
				handleBits();
				break;
			

			default:
				// normal input
				if (!args[0].startsWith("-")) {
					handleInput();
				} else {
					System.out.println("Invalid argument " + args[0]);
					System.exit(1);
				}
			}
		}

	}

	private void handleInput() {
		this.input = args[0];
		removeArgs(1);
	}

	private void handleAlgorithm() {
		switch (args[1]) {
		case "RSA":
			this.algorithm = Algorithm.RSA;
			break;

		default:
			System.out.println("Invalid algorithm");
			System.exit(1);
		}

		removeArgs(2);
	}

	private void handleBits() {
		this.bits = Integer.parseInt(args[1]);
		removeArgs(2);
	}

	private void removeArgs(int i) {
		args = Arrays.copyOfRange(args, i, args.length);
	}

	public CLIMode getMode() {
		return mode;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public String getInput() {
		return input;
	}

	public String getOutput() {
		return output;
	}

	public String getKey() {
		return key;
	}

	public int getBits() {
		return bits;
	}

}