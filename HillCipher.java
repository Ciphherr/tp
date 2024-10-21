import java.util.Scanner;

public class HillCipher {
    // Function to multiply two matrices (key matrix and text vector) and return the result
    public static int[] matrixMultiply(int[][] keyMatrix, int[] textVector, int matrixSize) {
        int[] result = new int[matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            result[i] = 0;
            for (int j = 0; j < matrixSize; j++) {
                result[i] += keyMatrix[i][j] * textVector[j];
            }
            result[i] = (result[i] % 26 + 26) % 26;  // Mod 26 to handle wrap-around
        }
        return result;
    }

    // Function to find the modular inverse of a number modulo 26
    public static int modInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1;  // Return 1 if no modular inverse (which shouldn't happen for valid keys)
    }

    // Function to calculate the determinant of a matrix (limited to 2x2 or 3x3)
    public static int determinant(int[][] matrix, int size) {
        if (size == 2) {
            return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % 26;
        } else if (size == 3) {
            return (matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
                    - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
                    + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0])) % 26;
        }
        return 0;
    }

    // Function to calculate the inverse of a 2x2 or 3x3 matrix
    public static int[][] inverseMatrix(int[][] keyMatrix, int size) {
        int determinant = determinant(keyMatrix, size);
        determinant = (determinant + 26) % 26;  // Ensure positive determinant
        int inverseDet = modInverse(determinant, 26);  // Find modular inverse of determinant

        int[][] inverseMatrix = new int[size][size];

        if (size == 2) {
            // 2x2 inverse matrix formula
            inverseMatrix[0][0] = (keyMatrix[1][1] * inverseDet) % 26;
            inverseMatrix[0][1] = (-keyMatrix[0][1] + 26) * inverseDet % 26;
            inverseMatrix[1][0] = (-keyMatrix[1][0] + 26) * inverseDet % 26;
            inverseMatrix[1][1] = (keyMatrix[0][0] * inverseDet) % 26;
        } else if (size == 3) {
            // 3x3 inverse matrix formula (calculated manually for simplicity)
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int minorDet = determinant(minor(keyMatrix, i, j, 3), 2);
                    inverseMatrix[j][i] = (minorDet * inverseDet * ((i + j) % 2 == 0 ? 1 : -1) + 26) % 26;
                }
            }
        }
        return inverseMatrix;
    }

    // Helper function to calculate the minor matrix for a 3x3 matrix
    public static int[][] minor(int[][] matrix, int row, int col, int size) {
        int[][] minor = new int[size - 1][size - 1];
        int minorRow = 0;
        for (int i = 0; i < size; i++) {
            if (i == row) continue;
            int minorCol = 0;
            for (int j = 0; j < size; j++) {
                if (j == col) continue;
                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }
            minorRow++;
        }
        return minor;
    }

    // Function to encrypt plaintext
    public static String encrypt(int[][] keyMatrix, String plaintext, int matrixSize) {
        return processText(keyMatrix, plaintext, matrixSize);
    }

    // Function to decrypt ciphertext
    public static String decrypt(int[][] keyMatrix, String ciphertext, int matrixSize) {
        int[][] inverseKey = inverseMatrix(keyMatrix, matrixSize);
        return processText(inverseKey, ciphertext, matrixSize);
    }

    // Common function to process the text (encryption or decryption)
    public static String processText(int[][] keyMatrix, String text, int matrixSize) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");  // Clean the text input
        while (text.length() % matrixSize != 0) {
            text += "X";  // If length is not a multiple of matrixSize, pad with 'X'
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += matrixSize) {
            int[] textVector = new int[matrixSize];
            for (int j = 0; j < matrixSize; j++) {
                textVector[j] = text.charAt(i + j) - 'A';
            }

            int[] cipherVector = matrixMultiply(keyMatrix, textVector, matrixSize);

            for (int j = 0; j < matrixSize; j++) {
                result.append((char) (cipherVector[j] + 'A'));
            }
        }
        return result.toString();
    }

    // Main method to run the cipher
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the matrix size
        System.out.print("Enter matrix size (2 or 3): ");
        int matrixSize = scanner.nextInt();
        if (matrixSize != 2 && matrixSize != 3) {
            System.out.println("Invalid matrix size. Only 2x2 or 3x3 matrices are supported.");
            return;
        }

        // Input the key matrix
        int[][] keyMatrix = new int[matrixSize][matrixSize];
        System.out.println("Enter " + matrixSize + "x" + matrixSize + " key matrix (integers):");
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                keyMatrix[i][j] = scanner.nextInt();
            }
        }

        // Input the plaintext
        System.out.print("Enter text to encrypt: ");
        scanner.nextLine();  // Consume newline
        String plaintext = scanner.nextLine();

        // Encrypt the text
        String encryptedText = encrypt(keyMatrix, plaintext, matrixSize);
        System.out.println("Encrypted Text: " + encryptedText);

        // Decrypt the text
        String decryptedText = decrypt(keyMatrix, encryptedText, matrixSize);
        System.out.println("Decrypted Text: " + decryptedText);

        scanner.close();
    }
}
