import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Day4 {
	
	private String input;
	
	public Day4(String input) {
		this.input = input;
	}
	
	public void printSmallestHashNumber() {
		boolean hashFiveFound = false;
		boolean hashSixFound = false;
		int numberToHash = 1;
		
		int numberToHashFiveZeros = 0;
		int numberToHashSixZeros = 0;
		
		while (!hashFiveFound || !hashSixFound) {
			String currentInput = this.input + Integer.toString(numberToHash);
			
			// Convert the MD5 hash to hexadecimal
			String hexHash = this.getMD5Hash(currentInput);
			
			// If the hexadecimal string starts with five zeros, exit the loop
			if (hexHash.startsWith("00000") && !hexHash.startsWith("000000") && !hashFiveFound) {
				numberToHashFiveZeros = numberToHash;
				hashFiveFound = true;
			}
			else if (hexHash.startsWith("000000") && !hashSixFound) {
				numberToHashSixZeros = numberToHash;
				hashSixFound = true;
			}
			
			if (hashFiveFound && hashSixFound) {
				break;
			}
			else {
				numberToHash++;
			}
		}
		
		// Print the smallest number
		System.out.println(numberToHashFiveZeros);
		System.out.println(numberToHashSixZeros);
	}
	
	public String getMD5Hash(String currentInput) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(currentInput.getBytes());
			
			byte[] digest = md.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashText = bigInt.toString(16);
			
			while(hashText.length() < 32) {
				  hashText = "0" + hashText;
			}
			
			return hashText;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		// Get puzzle input from command line
		if (args.length == 1) {
			String input = args[0];
			
			// Create puzzle instance
			Day4 puzzle = new Day4(input);
			
			// Print the smallest number satisfying the hash condition
			puzzle.printSmallestHashNumber();
		}
	}
}
