package crypt_basics;

import java.util.Arrays;

import crypt_basics.Statics.Algorithm;
import crypt_basics.Statics.CLIMode;

public class CLIParser {
	private CLIMode mode = CLIMode.NONE;
	private Algorithm algorithm;
	private char[] input;
	private String output;
	private String key;
	private Integer bits;
	private String[] args;
	private Integer lBits;
	private Integer nBits;

	public CLIParser(String[] args) {
		this.args = args;

		handleArgs();
	}

	private void handleArgs() {
		while (args.length > 0) {
			switch (args[0]) {
			case "-e":
				this.mode = CLIMode.CRYPT;
				if (mode != CLIMode.NONE) {
					System.out.println("Mode already set");
					System.exit(1);
				}
				break;
			case "-s":
				this.mode = CLIMode.SIGN;
				if (mode != CLIMode.NONE) {
					System.out.println("Mode already set");
					System.exit(1);
				}
				break;
			case "-A":
				handleAlgorithm();
				break;
			case "-b":
				this.bits = handleBits();
				break;
			case "-L":
				this.lBits = handleBits();
				break;
			case "-N":
				this.nBits = handleBits();
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
		this.input = args[0].toCharArray();
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

	private Integer handleBits() {
		Integer locBits = Integer.parseInt(args[1]);
		removeArgs(2);
		return locBits;
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

	public char[] getInput() {
		return input;
	}

	public String getOutput() {
		return output;
	}

	public String getKey() {
		return key;
	}

	public int getBits() {
		if (bits == null) {
			System.out.println("No bits specified, defaulting to 2048");
			return 2048;
		}
		return bits;
	}

	public int getLBits() {
		if (lBits == null) {
			System.out.println("No L bits specified, defaulting to 2048");
			return 2048;
		}
		return lBits;
	}

	public int getNBits() {
		if (nBits == null) {
			System.out.println("No N bits specified, defaulting to 224");
			return 224;
		}
		return nBits;
	}
}