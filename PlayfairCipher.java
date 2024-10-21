import java.util.Scanner;

public class PlayfairCipher {
    private static char[][] table = new char[5][5];

    // Method to create a 5x5 key table for Playfair cipher
    public static void generateTable(String key) {
        boolean[] used = new boolean[26];  // To check if letter is already used
        key = key.toUpperCase().replaceAll("[^A-Z]", "");  // Remove non-letter characters
        int index = 0;

        // Add key characters to the table
        for (char c : key.toCharArray()) {
            if (!used[c - 'A']) {
                table[index / 5][index % 5] = c;
                used[c - 'A'] = true;
                index++;
            }
        }

        // Add remaining alphabet letters
        for (char c = 'A'; c <= 'Z'; c++) {
            if (!used[c - 'A'] && c != 'J') {  // Not including 'J'
                table[index / 5][index % 5] = c;
                index++;
            }
        }
    }

    // Method to find the row and column of a letter in the table
    public static int[] findPosition(char letter) {
        // Treat 'J' as 'I' to avoid NullPointerException for 'J'
        if (letter == 'J') {
            letter = 'I';
        }

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (table[row][col] == letter) {
                    return new int[]{row, col};
                }
            }
        }
        return null;  // Character not found
    }

    // Encrypt using the Playfair cipher
    public static String encrypt(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");  // Clean the input text
        String encrypted = "";
        
        for (int i = 0; i < text.length(); i += 2) {
            if (i + 1 == text.length() || text.charAt(i) == text.charAt(i + 1)) {
                text = text.substring(0, i + 1) + 'X' + text.substring(i + 1);  // Padding for same letters
            }

            char first = text.charAt(i);
            char second = text.charAt(i + 1);

            int[] pos1 = findPosition(first);
            int[] pos2 = findPosition(second);

            if (pos1 == null || pos2 == null) {
                throw new IllegalArgumentException("Character not found in table: " + first + " or " + second);
            }

            // Same row, shift right
            if (pos1[0] == pos2[0]) {
                encrypted += table[pos1[0]][(pos1[1] + 1) % 5];
                encrypted += table[pos2[0]][(pos2[1] + 1) % 5];
            }
            // Same column, shift down
            else if (pos1[1] == pos2[1]) {
                encrypted += table[(pos1[0] + 1) % 5][pos1[1]];
                encrypted += table[(pos2[0] + 1) % 5][pos2[1]];
            }
            // Rectangle case
            else {
                encrypted += table[pos1[0]][pos2[1]];
                encrypted += table[pos2[0]][pos1[1]];
            }
        }

        return encrypted;
    }

    // Decrypt using the Playfair cipher
    public static String decrypt(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");  // Clean the input text
        String decrypted = "";
        
        for (int i = 0; i < text.length(); i += 2) {
            char first = text.charAt(i);
            char second = text.charAt(i + 1);

            int[] pos1 = findPosition(first);
            int[] pos2 = findPosition(second);

            if (pos1 == null || pos2 == null) {
                throw new IllegalArgumentException("Character not found in table: " + first + " or " + second);
            }

            // Same row, shift left
            if (pos1[0] == pos2[0]) {
                decrypted += table[pos1[0]][(pos1[1] + 4) % 5];
                decrypted += table[pos2[0]][(pos2[1] + 4) % 5];
            }
            // Same column, shift up
            else if (pos1[1] == pos2[1]) {
                decrypted += table[(pos1[0] + 4) % 5][pos1[1]];
                decrypted += table[(pos2[0] + 4) % 5][pos2[1]];
            }
            // Rectangle case
            else {
                decrypted += table[pos1[0]][pos2[1]];
                decrypted += table[pos2[0]][pos1[1]];
            }
        }

        return decrypted;
    }

    // Main method to run the cipher
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the key and the plaintext
        System.out.print("Enter key: ");
        String key = scanner.nextLine();
        System.out.print("Enter text to encrypt: ");
        String text = scanner.nextLine();

        // Generate table and encrypt the text
        generateTable(key);
        String encryptedText = encrypt(text);

        // Display encrypted text
        System.out.println("Encrypted Text: " + encryptedText);

        // Decrypt the text and display decrypted result
        String decryptedText = decrypt(encryptedText);
        System.out.println("Decrypted Text: " + decryptedText);

        // Display the Playfair table for reference
        System.out.println("Playfair Table:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }

        scanner.close();
    }
}
