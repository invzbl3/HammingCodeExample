package org.example;

// import required classes and packages
import java.util.Scanner;

/**
 * Create HammingCodeExample class to implement the Hamming Code functionality in Java
 *
 * This refactored version organizes the code into smaller, more manageable methods with descriptive names,
 * improves variable names, and eliminates unnecessary loops and conditions where possible.
 * Additionally, it maintains the functionality of the original code while making it more readable and concise.
 */
public class HammingCodeExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the data size (number of bits):");
        int dataSize = scanner.nextInt();

        int[] data = getDataFromUser(scanner, dataSize);
        printData("Data entered by user:", data);

        int[] hammingCode = generateHammingCode(data);
        printData("Hamming code generated:", hammingCode);

        int errorPosition = getErrorPosition(scanner, hammingCode.length);
        if (errorPosition != 0) {
            introduceError(hammingCode, errorPosition);
            System.out.println("Data after introducing error:");
            printData(hammingCode);
        }

        receiveAndCorrectData(hammingCode, data.length);
        scanner.close();
    }

    // Helper method to get data from user
    private static int[] getDataFromUser(Scanner scanner, int dataSize) {
        int[] data = new int[dataSize];
        for (int i = dataSize - 1; i >= 0; i--) {
            System.out.println("Enter bit " + (i + 1) + " of the data:");
            data[i] = scanner.nextInt();
        }
        return data;
    }

    // Helper method to print data with a message
    private static void printData(String message, int[] data) {
        System.out.println(message);
        printData(data);
    }

    // Overloaded helper method to print data without a message
    private static void printData(int[] data) {
        for (int bit : data) {
            System.out.print(bit);
        }
        System.out.println();
    }

    // Method to generate Hamming code
    private static int[] generateHammingCode(int[] data) {
        int dataSize = data.length;
        int parityBitsCount = calculateParityBits(dataSize);

        int[] hammingCode = new int[dataSize + parityBitsCount];
        int dataIndex = 0;
        for (int i = 0, j = 0; i < hammingCode.length; i++) {
            if (isPowerOfTwo(i + 1)) {
                hammingCode[i] = 2; // Placeholder for parity bit
            } else {
                hammingCode[i] = data[dataIndex++];
            }
        }

        for (int i = 0; i < parityBitsCount; i++) {
            hammingCode[(int) Math.pow(2, i) - 1] = calculateParityBit(hammingCode, i);
        }

        return hammingCode;
    }

    // Helper method to calculate number of parity bits required
    private static int calculateParityBits(int dataSize) {
        int parityBitsCount = 0;
        while (Math.pow(2, parityBitsCount) < dataSize + parityBitsCount + 1) {
            parityBitsCount++;
        }
        return parityBitsCount;
    }

    // Helper method to check if a number is a power of 2
    private static boolean isPowerOfTwo(int number) {
        return (number & (number - 1)) == 0;
    }

    // Method to calculate parity bit
    private static int calculateParityBit(int[] hammingCode, int pow) {
        int parityBit = 0;
        for (int i = 0; i < hammingCode.length; i++) {
            if (hammingCode[i] != 2) {
                int index = i + 1;
                if (((index >> pow) & 1) == 1) {
                    parityBit ^= hammingCode[i];
                }
            }
        }
        return parityBit;
    }

    // Helper method to get error position from user
    private static int getErrorPosition(Scanner scanner, int hammingCodeSize) {
        System.out.println("Enter position of bit to alter in received data (0 for no error):");
        return scanner.nextInt();
    }

    // Method to introduce error in data
    private static void introduceError(int[] hammingCode, int errorPosition) {
        hammingCode[errorPosition - 1] ^= 1;
    }

    // Method to receive and correct data
    private static void receiveAndCorrectData(int[] hammingCode, int originalDataSize) {
        int parityBitsCount = calculateParityBits(originalDataSize);

        int[] parityArray = new int[parityBitsCount];
        StringBuilder errorLocation = new StringBuilder();

        for (int i = 0; i < parityBitsCount; i++) {
            for (int j = 0; j < hammingCode.length; j++) {
                if (((j + 1) & (1 << i)) != 0 && hammingCode[j] != 2) {
                    parityArray[i] ^= hammingCode[j];
                }
            }
            errorLocation.insert(0, parityArray[i]);
        }

        int errorLocationIndex = Integer.parseInt(errorLocation.toString(), 2);
        if (errorLocationIndex != 0) {
            System.out.println("Error detected and corrected at position " + errorLocationIndex + ".");
            hammingCode[errorLocationIndex - 1] ^= 1;
        } else {
            System.out.println("No error detected in received data.");
        }

        System.out.println("Original data sent:");
        int pow = parityBitsCount - 1;
        for (int i = hammingCode.length; i > 0; i--) {
            if (!isPowerOfTwo(i)) {
                System.out.print(hammingCode[i - 1]);
            } else {
                pow--;
            }
        }
        System.out.println();
    }
}