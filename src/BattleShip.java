import java.io.*;
import java.util.Scanner;

public class BattleShip {

    public static final int SIZE = 10;
    public static char[][] board = new char[SIZE][SIZE];
    public static char[][] aimBoard = new char[SIZE][SIZE];


    // Tahtayı oluşturma fonksiyonu
    public static void createBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '.';
            }
        }
    }

    // Tahtayı yazdırma fonksiyonu
    public static void printBoard() {
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        char rows[] = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        for (int i = 0; i < SIZE; i++) {
            System.out.print(rows[i] + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    public static void printAimBoard() {
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        for (int i = 0; i < SIZE; i++) {
            System.out.print(rows[i] + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(aimBoard[i][j] + " ");
            }
            System.out.println();
        }
    }


    // Tahtayı dosyaya kaydet
    public static void saveBoardToFile(char[][] board, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    writer.write(board[i][j]);
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Dosyaya yazma hatası: " + e.getMessage());
        }
    }

    // Dosyadan tahta yükle
    public static boolean loadBoardFromFile(char[][] board, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            for (int i = 0; i < SIZE; i++) {
                String line = reader.readLine();
                if (line == null) return false;

                // Satır sonu karakterlerini temizle
                line = line.replace("\r", "").replace("\n", "");

                if (line.length() < SIZE) return false;

                for (int j = 0; j < SIZE; j++) {
                    board[i][j] = line.charAt(j);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }



    public static void showAvailableDirections(int rowIndex, int columnIndex, int shipLength, char[][] board) {
        boolean isRightAvailable = columnIndex + shipLength <= SIZE;
        boolean isLeftAvailable = (columnIndex + 1) - shipLength >= 0;
        boolean isUpAvailable = (rowIndex + 1) - shipLength >= 0;
        boolean isDownAvailable = rowIndex + shipLength <= SIZE;

        if (isRightAvailable) {
            for (int i = 0; i < shipLength; i++) {
                if (board[rowIndex][columnIndex + i] != '.') {
                    isRightAvailable = false;
                    break;
                }
            }
        }
        if (isLeftAvailable) {
            for (int i = 0; i < shipLength; i++) {
                if (board[rowIndex][columnIndex - i] != '.') {
                    isLeftAvailable = false;
                    break;
                }
            }
        }

        if (isUpAvailable) {
            for (int i = 0; i < shipLength; i++) {
                if (board[rowIndex - i][columnIndex] != '.') {
                    isUpAvailable = false;
                    break;
                }
            }
        }

        if (isDownAvailable) {
            for (int i = 0; i < shipLength; i++) {
                if (board[rowIndex + i][columnIndex] != '.') {
                    isDownAvailable = false;
                    break;
                }
            }
        }

        System.out.println("Gemiyi yerleştirebileceğiniz yönler:");
        if (isRightAvailable) {
            System.out.println(" RIGHT");
        }
        if (isLeftAvailable) {
            System.out.println(" LEFT");
        }
        if (isDownAvailable) {
            System.out.println(" DOWN");
        }
        if (isUpAvailable) {
            System.out.println(" UP");
        }
        if (!isRightAvailable && !isLeftAvailable && !isDownAvailable && !isUpAvailable) {
            System.out.println("Hiçbir yöne yerleştirilemez, başka bir koordinat seçiniz.");
        }
    }

    public static boolean placeShip(int rowIndex, int columnIndex, int shipLength, char direction, char shipChar) {

        direction = Character.toUpperCase(direction); // yönü büyük harfe çevir


        // Ön kontrol: geçerli pozisyon mu?
        for (int i = 0; i < shipLength; i++) {
            int newRowIndex = rowIndex;
            int newColumnIndex = columnIndex;

            switch (direction) {
                case 'R':
                    newColumnIndex = columnIndex + i;
                    break;
                case 'L':
                    newColumnIndex = columnIndex - i;
                    break;
                case 'U':
                    newRowIndex = rowIndex - i;
                    break;
                case 'D':
                    newRowIndex = rowIndex + i;
                    break;
                default:
                    System.out.println("Geçersiz yön! Lütfen R, L, U veya D giriniz.");
                    return false;
            }


            // Oyun tahtasının dışına çıkıyor mu çıkmıyor mu kontrolü
            if (newRowIndex < 0 || newRowIndex >= SIZE || newColumnIndex < 0 || newColumnIndex >= SIZE) {
                System.out.println("Gemi tahtanın dışına çıktı, başka koordinat veya yön girin.");
                return false;
            }

            // Gemi çakışması kontrolü
            if (board[newRowIndex][newColumnIndex] != '.') {
                System.out.println("Gemiyi yerleştirmeye çalıştığınız bölgede başka bir gemi var.");
                return false;
            }

        }
        // Uygunsa gerçekten yerleştir
        for (int i = 0; i < shipLength; i++) {
            switch (direction) {
                case 'R':
                    board[rowIndex][columnIndex + i] = shipChar;
                    break;
                case 'L':
                    board[rowIndex][columnIndex - i] = shipChar;
                    break;
                case 'U':
                    board[rowIndex - i][columnIndex] = shipChar;
                    break;
                case 'D':
                    board[rowIndex + i][columnIndex] = shipChar;
                    break;
            }
        }

        return true;
    }

    public static void makeAShoot(int row, int coloumn){

        char shipChar [] = new char[]{'A','B','C','D','E'};

        if (aimBoard[row][coloumn] == 'x' || aimBoard[row][coloumn] == 'o' ) {
            System.out.println("Bu noktaya daha önce atış yaptınız, başka bir kordinat seçin");
            return;
        }
        if (board[row][coloumn] == '.'){
            aimBoard[row][coloumn] = 'o';
            System.out.println("Iskaladınız");

        }else {
            aimBoard[row][coloumn] = 'x';
            System.out.println("Vurdunuz");

            char damagedShip = board[row][coloumn];
            if (isDestroyed(damagedShip)){
                System.out.println(damagedShip + " gemisi battı.");
            }

        }
        saveBoardToFile(aimBoard,"src/2ndaim.txt");
    }

    public static boolean isGameOver(){
        for (int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE;j++){
                if (board[i][j] != '.' && aimBoard[i][j] != 'x' ){ // ya 1stShips'te gemi kalmayacak ya da 2ndAim'de vurulmamış gemi kalmayacak
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isDestroyed (char shipChar){
        for (int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE;j++){
                  if (board[i][j]==shipChar && aimBoard[i][j] != 'x'){
                        return false;
                  }
            }
        }
        return true;
    }
    public static void resetAimBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                aimBoard[i][j] = '.';
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        File shipFile = new File("src/1stships.txt");
        File aimFile = new File("src/2ndaim.txt");

        boolean shipExists = shipFile.exists();
        boolean aimExists = aimFile.exists();

        if (shipExists || aimExists) {
            System.out.println("Önceki oyun kayıtları bulundu.");
            System.out.println("Yeni oyun başlatmak için 'Q' tuşuna basın.");
            String choice = input.nextLine().toUpperCase();

            if (choice.equals("Q")) {
                if (shipFile.exists()) shipFile.delete();
                if (aimFile.exists()) aimFile.delete();
                System.out.println("Önceki oyun silindi. Yeni oyun başlatılıyor...\n");
            }
        }

        // Gemilerin tahtası dosyadan yüklensin ya da oluşturulsun
        boolean loaded = loadBoardFromFile(board, "src/1stships.txt");
        if (!loaded) {
            System.out.println("Gemi tahtası bulunamadı. Yeni gemiler yerleştirilecek.");
            createBoard();
            resetAimBoard();

            // Hedef tahtasını da sıfırla
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    aimBoard[i][j] = '.';
                }
            }

            // Gemi tanımları
            char[] shipChars = {'A', 'B', 'C', 'C', 'D', 'D', 'E', 'E', 'E'};
            int[] shipLengths = {5, 4, 3, 3, 2, 2, 1, 1, 1};

            for (int s = 0; s < shipChars.length; s++) {
                char shipChar = shipChars[s];
                int shipLength = shipLengths[s];
                boolean isPlaced = false;

                while (!isPlaced) {
                    printBoard();
                    System.out.println(shipLength + " birimlik '" + shipChar + "' gemisinin ilk koordinatını giriniz (örnek: B4): ");
                    String coordinate = input.nextLine().toUpperCase();

                    if (coordinate.length() < 2) {
                        System.out.println("Geçersiz koordinat! Tekrar deneyin.");
                        continue;
                    }

                    char rowChar = coordinate.charAt(0);
                    int rowIndex = rowChar - 'A';

                    int columnIndex;
                    try {
                        columnIndex = Integer.parseInt(coordinate.substring(1)) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Geçersiz sayı! Tekrar deneyin.");
                        continue;
                    }

                    if (rowIndex < 0 || rowIndex >= SIZE || columnIndex < 0 || columnIndex >= SIZE) {
                        System.out.println("Koordinatlar tahtanın dışında! Tekrar deneyin.");
                        continue;
                    }

                    showAvailableDirections(rowIndex, columnIndex, shipLength, board);

                    System.out.print("Yön girin (R, L, U, D): ");
                    char direction = input.nextLine().toUpperCase().charAt(0);

                    isPlaced = placeShip(rowIndex, columnIndex, shipLength, direction, shipChar);

                    if (isPlaced) {
                        saveBoardToFile(board, "src/1stships.txt");
                    } else {
                        System.out.println("Yerleştirme başarısız! Tekrar deneyin.\n");
                    }
                }
            }

            saveBoardToFile(board, "src/1stships.txt");
            System.out.println("Gemiler başarıyla yerleştirildi ve 1stships.txt dosyasına kaydedildi.\n");
        } else {
            System.out.println("1stships.txt dosyasından gemi tahtası yüklendi.");
        }

        // Hedef tahtasını yükle veya oluştur
        boolean aimLoaded = loadBoardFromFile(aimBoard, "src/2ndaim.txt");
        if (!aimLoaded) {
            System.out.println("2ndaim.txt bulunamadı. Boş hedef tahtası oluşturuluyor.");
            createBoard();  // aimBoard için tekrar kullan
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    aimBoard[i][j] = '.';
                }
            }
            saveBoardToFile(aimBoard, "src/2ndaim.txt");
        } else {
            System.out.println("2ndaim.txt hedef tahtası yüklendi.");
        }

        // Atış yapma Kısmı
        while(true){

            printAimBoard();
            System.out.print("Atış yapmak için koordinat giriniz (örnek: B4): ");
            String shotCoordinate = input.nextLine().toUpperCase();
            char row = shotCoordinate.charAt(0);
            int rowIndex = row - 'A';

            int column = Integer.parseInt(shotCoordinate.substring(1)) - 1;
            if (rowIndex<0 || rowIndex>=10 || column<0 || column>=10){
                System.out.println("Geçerseiz kordinat, yeri kordinat giriniz");
                continue;
            }
            makeAShoot(rowIndex,column);

            if (isGameOver()){
                break;
            }


        }



    }


}

