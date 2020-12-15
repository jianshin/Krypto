package cryptolib_java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Struct;

import javax.xml.bind.DatatypeConverter;

public class CBCXor {

    public static void main(String[] args) {
        String filename = "src/cryptolib_java/input2.txt";
        byte[] first_block = null;
        byte[] encrypted = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            first_block = br.readLine().getBytes();
            encrypted = DatatypeConverter.parseHexBinary(br.readLine());
            br.close();
        } catch (Exception err) {
            System.err.println("Error handling file.");
            err.printStackTrace();
            System.exit(1);
        }
        String m = recoverMessage(first_block, encrypted);
        System.out.println("Recovered message: " + m);
    }

    /**
     * Recover the encrypted message (CBC encrypted with XOR, block size = 12).
     *
     * @param first_block
     *            We know that this is the value of the first block of plain
     *            text.
     * @param encrypted
     *            The encrypted text, of the form IV | C0 | C1 | ... where each
     *            block is 12 bytes long.
     */
    private static String recoverMessage(byte[] first_block, byte[] encrypted) {
        byte[][] msgInBlocks = retrieveBlocks(encrypted);
        byte[] key = getKey(first_block, msgInBlocks[0], msgInBlocks[1]);
        byte[] msg = new byte[encrypted.length-12];
        byte[] prevCipherBlock = msgInBlocks[0];
        for(int row=1; row<msgInBlocks.length; row++){
            for(int col=0; col<12; col++){
                msg[12*(row-1)+col] = (byte) ((key[col]^msgInBlocks[row][col])^prevCipherBlock[col]);
            }
            prevCipherBlock = msgInBlocks[row];
        }
        return new String(msg);
    }

    private static byte[] getKey(byte[] firstCipherBlock, byte[] firstPlainTextBlock, byte[] second){
        byte[] key = new byte[12];
        for(int i=0; i<12; i++){
            key[i] = (byte) (firstCipherBlock[i]^firstPlainTextBlock[i]^second[i]);
        }
        return key;
    }

    private static byte[][] retrieveBlocks(byte[] encrypted){
        int rows = encrypted.length/12;
        int cols = 12;
        byte[][] blocks = new byte[rows][cols];
        for(int i=0; i<rows-1; i++) {
            for (int j = 0; j < cols; j++) {
                blocks[i][j] = encrypted[12 * i + j];
            }
        }
        return blocks;
    }
}