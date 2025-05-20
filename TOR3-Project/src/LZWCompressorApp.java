import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LZWCompressorApp {

    private static final int MAX_DICT_SIZE = 1048576; //2^20 = 1048576 possible entries

    public static void main(String[] args) {
        String inputPath = "book.pdf";
        String outputPath = "book_compressed.lzw";
        try {
            //read input PDF into byte array
            byte[] inputData = Files.readAllBytes(Paths.get(inputPath));
            //compress using LZW
            byte[] compressedData = compressLZW(inputData);
            //write compressed output
            Files.write(Paths.get(outputPath), compressedData);
            //report sizes
            long originalSize = inputData.length;
            long compressedSize = compressedData.length;

            System.out.println("Original size: " + originalSize + " bytes");
            System.out.println("Compressed size: " + compressedSize + " bytes");
            System.out.printf("Compression ratio: %.2f%%\n", (100.0 * compressedSize / originalSize));

           //comparisons
            Map<String, String> formats = new LinkedHashMap<>();
            formats.put("Compressed (LZW)", outputPath);
            formats.put("DJVU", "book.djvu");
            formats.put("ZIP", "book.zip");
            formats.put("TXT", "book.txt");
            formats.put("HTML", "book.html");
            formats.put("RTF", "book.rtf");

            System.out.println("\n--- Format Comparison ---");
            for (Map.Entry<String, String> entry : formats.entrySet()) {
                String label = entry.getKey();
                String path = entry.getValue();
                try {
                    long size = Files.size(Paths.get(path));
                    double ratio = 100.0 * size / originalSize;
                    System.out.printf("%-12s: %8d bytes (%.2f%% of original)\n", label, size, ratio);
                } catch (IOException e) {
                    System.out.printf("%-12s: [File not found: %s]\n", label, path);
                }
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //core LZW compression function
    private static byte[] compressLZW(byte[] input) {
        Map<String, Integer> dictionary = new HashMap<>();
        int dictIndex = 256;

        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char) i, i);
        }
        List<Integer> outputCodes = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (byte b : input) {
            char c = (char) (b & 0xFF);
            String next = current.toString() + c;
            if (dictionary.containsKey(next)) {
                current.append(c);
            } else {
                outputCodes.add(dictionary.get(current.toString()));
                if (dictionary.size() < MAX_DICT_SIZE) {
                    dictionary.put(next, dictIndex++);
                }
                current.setLength(0);
                current.append(c);
            }
        }
        if (!current.isEmpty()) {
            outputCodes.add(dictionary.get(current.toString()));
        }
        return packCodesToBytes(outputCodes, 12);
    }

    //pack 12-bit codes into byte stream
    private static byte[] packCodesToBytes(List<Integer> codes, int bitsPerCode) {
        int totalBits = codes.size() * bitsPerCode;
        BitSet bitSet = new BitSet(totalBits);
        int bitPos = 0;

        for (int code : codes) {
            for (int i = bitsPerCode - 1; i >= 0; i--) {
                if (((code >> i) & 1) == 1) {
                    bitSet.set(bitPos);
                }
                bitPos++;
            }
        }

        byte[] packed = new byte[(totalBits + 7) / 8];
        for (int i = 0; i < packed.length; i++) {
            for (int j = 0; j < 8; j++) {
                if (bitSet.get(i * 8 + j)) {
                    packed[i] |= (1 << (7 - j));
                }
            }
        }
        return packed;
    }
}
