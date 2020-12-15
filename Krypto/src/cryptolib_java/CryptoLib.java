// Compilation (CryptoLibTest contains the main-method):
//   javac CryptoLibTest.java
// Running:
//   java CryptoLibTest
package cryptolib_java;

public class CryptoLib {

	/**
	 * Returns an array "result" with the values "result[0] = gcd",
	 * "result[1] = s" and "result[2] = t" such that "gcd" is the greatest
	 * common divisor of "a" and "b", and "gcd = a * s + b * t".
	 **/
	public static int[] EEA(int a, int b) {
		// Note: as you can see in the test suite,
		// your function should work for any (positive) value of a and b.
		int gcd = -1;
		int s = -1;
		int t = -1;
		int[] result = new int[3];

		int temp = 0;
		int s1 = 1;
		int s2 = 0;
		int t1 = 0;
		int t2 = 1;
		int quotient = 0;
		int remainder = b;
		int remainderPrev = a;

		while(remainder>0){
			quotient = remainderPrev/remainder;
			temp = remainder;
			remainder = remainderPrev-quotient*remainder;
			remainderPrev = temp;
			temp = s2;
			s2 = s1-quotient*s2;
			s1 = temp;
			temp = t2;
			t2 = t1-quotient*t2;
			t1=temp;
		}
		gcd = remainderPrev;
		s = s1;
		t = t1;

		result[0] = gcd;
		result[1] = s;
		result[2] = t;
		return result;
	}

	/**
	 * Returns Euler's Totient for value "n".
	 **/
	public static int EulerPhi(int n) {
		int count = 0;
		for(int i=1; i<n; i++){
			int[] eea = EEA(n, i);
			if(eea[0]==1){
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns the value "v" such that "n*v = 1 (mod m)". Returns 0 if the
	 * modular inverse does not exist.
	 **/
	public static int ModInv(int n, int m) {
		if(n<0){
			n=n+m;
		}
		int[] eea = EEA(n, m);
		if(eea[0] == 1){
			if(eea[1] < 0){
				return eea[1] + m;
			}
			return eea[1];
		}
		return 0;
	}

	/**
	 * Returns 0 if "n" is a Fermat Prime, otherwise it returns the lowest
	 * Fermat Witness. Tests values from 2 (inclusive) to "n/3" (exclusive).
	 **/
	public static int FermatPT(int n) {
		for(int i=2; i<n/3; i++){
			if(EEA(n, i)[0] == 1){
				int remainder = PowerOfWithMod(i, n-1, n);
				if(remainder != 1){
					return i;
				}
			}else{
				return i;
			}
		}
		return 0;
	}

	public static int PowerOfWithMod(int base, int exp, int mod){
		int product = 1;
		for(int i=0; i<exp; i++){
			product = (product*base)%mod;
		}
		return product;
	}

	/**
	 * Returns the probability that calling a perfect hash function with
	 * "n_samples" (uniformly distributed) will give one collision (i.e. that
	 * two samples result in the same hash) -- where "size" is the number of
	 * different output values the hash function can produce.
	 **/
	public static double HashCP(double n_samples, double size) {
		double product = 1;
		for(int i=1; i<n_samples; i++){
			product = product*(1-i/size);
		}
		return (1-product);
	}

}
