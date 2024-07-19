package edu.mdc.cop2805c.assignment2.app;


import edu.mdc.cop2805c.assignment2.assets.CryptoCurrencyHolding;
import edu.mdc.cop2805c.assignment2.assets.NFT;
import edu.mdc.cop2805c.assignment2.base.CryptoCurrency;
import edu.mdc.cop2805c.assignment2.wallet.CryptoWallet;
import edu.mdc.cop2805c.assignment2.wallet.WalletManager;

import java.util.Scanner;

public class Main {
    private static WalletManager walletManager;
    private static Scanner scanner;

    public static void main(String[] args) {
        walletManager = new WalletManager();
        scanner = new Scanner(System.in);

        // Initialize with some sample data
        initializeSampleData();

        while (true) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAllWallets();
                    break;
                case "2":
                    viewWallet();
                    break;
                case "3":
                    addWallet();
                    break;
                case "4":
                    buyCryptoAssets();
                    break;
                case "5":
                    sellCryptoAssets();
                    break;
                case "6":
                    transferCryptoAssets();
                    break;
                case "Q":
                case "q":
                    System.out.println("Exiting program. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n***********************************************************");
        System.out.println("** Please choose your option: ");
        System.out.println("** 1. View all Wallets");
        System.out.println("** 2. View Wallet");
        System.out.println("** 3. Add Wallet");
        System.out.println("** 4. Buy Crypto Assets");
        System.out.println("** 5. Sell Crypto Assets");
        System.out.println("** 6. Transfer Crypto Assets");
        System.out.println("** Q. Quit");
        System.out.println("***********************************************************");
        System.out.print("\nEnter your choice: ");
    }

    private static void viewAllWallets() {
        System.out.println(walletManager.getShortDescription());
    }

    private static void viewWallet() {
        System.out.println(">> Which Wallet? ");
        System.out.println(">> - Crypto Currency Wallets:");
        for (int i = 0; i < walletManager.getCryptoCurrencyWallets().size(); i++) {
            System.out.println(" (" + (i + 1) + ") " + walletManager.getCryptoCurrencyWallets().get(i).getNiceName());
        }
        System.out.println(">> - NFT Wallets:");
        for (int i = 0; i < walletManager.getNftWallets().size(); i++) {
            System.out.println(" (" + (i + 1 + walletManager.getCryptoCurrencyWallets().size()) + ") " + walletManager.getNftWallets().get(i).getNiceName());
        }

        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < walletManager.getCryptoCurrencyWallets().size()) {
            System.out.println(walletManager.getCryptoCurrencyWallet(choice).getLongDescription());
        } else {
            System.out.println(walletManager.getNFTWallet(choice - walletManager.getCryptoCurrencyWallets().size()).getLongDescription());
        }
    }

    private static void addWallet() {
        System.out.println(">> What type of Asset? ");
        System.out.println(" (1) NFT  (2) Crypto Currency ");
        int type = Integer.parseInt(scanner.nextLine());

        System.out.println(">> Nice name for the wallet: ");
        String niceName = scanner.nextLine();

        System.out.println(">> Address (Public Key): ");
        String address = scanner.nextLine();

        System.out.println(">> Private Key Filename: ");
        String privateKeyFilename = scanner.nextLine();

        if (type == 1) {
            walletManager.createNFTWallet(niceName, address, privateKeyFilename);
        } else {
            walletManager.createCryptoCurrencyWallet(niceName, address, privateKeyFilename);
        }

        System.out.println("Wallet created successfully.");
    }

    private static void buyCryptoAssets() {
        System.out.println(">> From which address? ");
        String fromAddress = scanner.nextLine();

        System.out.println(">> What type of Asset? ");
        System.out.println(" (1) NFT  (2) Crypto Currency ");
        int type = Integer.parseInt(scanner.nextLine());

        if (type == 1) {
            buyNFT(fromAddress);
        } else {
            buyCryptoCurrency(fromAddress);
        }
    }

    private static void buyNFT(String fromAddress) {
        System.out.println(">> Which Wallet? ");
        for (int i = 0; i < walletManager.getNftWallets().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + walletManager.getNftWallets().get(i).getNiceName());
        }
        int walletIndex = Integer.parseInt(scanner.nextLine()) - 1;

        System.out.println(">> Name of the NFT? ");
        String name = scanner.nextLine();

        System.out.println(">> Description of the NFT? ");
        String description = scanner.nextLine();

        System.out.println(">> Contract Address of the NFT? ");
        String contractAddress = scanner.nextLine();

        System.out.println(">> Blockchain network of the NFT? ");
        String blockchainNetwork = scanner.nextLine();

        System.out.println(">> Value in USD of the NFT? ");
        double valueInUSD = Double.parseDouble(scanner.nextLine());

        NFT nft = new NFT(name, description, contractAddress, blockchainNetwork, valueInUSD);
        walletManager.getNftWallets().get(walletIndex).buy(nft, fromAddress);

        System.out.println("NFT Bought!");
        System.out.println("Balance for " + walletManager.getNftWallets().get(walletIndex).getNiceName() + ": $" + walletManager.getNftWallets().get(walletIndex).getTotalValueInUSD());
    }

    private static void buyCryptoCurrency(String fromAddress) {
        System.out.println(">> Which Wallet? ");
        for (int i = 0; i < walletManager.getCryptoCurrencyWallets().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + walletManager.getCryptoCurrencyWallets().get(i).getNiceName());
        }
        int walletIndex = Integer.parseInt(scanner.nextLine()) - 1;

        System.out.println(">> Which crypto currency? ");
        for (int i = 0; i < walletManager.getCryptoCurrencies().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + walletManager.getCryptoCurrencies().get(i).getName());
        }
        System.out.println(" " + (walletManager.getCryptoCurrencies().size() + 1) + ". Add New Crypto Currency");

        int currencyChoice = Integer.parseInt(scanner.nextLine());
        CryptoCurrency cryptoCurrency;

        if (currencyChoice > walletManager.getCryptoCurrencies().size()) {
            cryptoCurrency = addNewCryptoCurrency();
        } else {
            cryptoCurrency = walletManager.getCryptoCurrencies().get(currencyChoice - 1);
        }

        System.out.println(">> Which amount? ");
        double amount = Double.parseDouble(scanner.nextLine());

        CryptoCurrencyHolding holding = new CryptoCurrencyHolding(cryptoCurrency, amount);
        walletManager.getCryptoCurrencyWallets().get(walletIndex).buy(holding, fromAddress);

        System.out.println("Crypto Currency Bought!");
        System.out.println("Balance for " + walletManager.getCryptoCurrencyWallets().get(walletIndex).getNiceName() + ": $" + walletManager.getCryptoCurrencyWallets().get(walletIndex).getTotalValueInUSD());
    }

    private static CryptoCurrency addNewCryptoCurrency() {
        System.out.println(">> Name of the new crypto currency? ");
        String name = scanner.nextLine();

        System.out.println(">> Symbol of the new crypto currency? ");
        String symbol = scanner.nextLine();

        System.out.println(">> Current price in USD of the new crypto currency? ");
        double currentPrice = Double.parseDouble(scanner.nextLine());

        System.out.println(">> Blockchain network of the new crypto currency? ");
        String blockchainNetwork = scanner.nextLine();

        CryptoCurrency newCryptoCurrency = new CryptoCurrency(name, symbol, currentPrice, blockchainNetwork);
        walletManager.addCryptoCurrency(newCryptoCurrency);

        return newCryptoCurrency;
    }

    private static void sellCryptoAssets() {
        System.out.println(">> To which address? ");
        String toAddress = scanner.nextLine();

        System.out.println(">> What type of Asset? ");
        System.out.println(" (1) NFT  (2) Crypto Currency ");
        int type = Integer.parseInt(scanner.nextLine());

        if (type == 1) {
            sellNFT(toAddress);
        } else {
            sellCryptoCurrency(toAddress);
        }
    }

    private static void sellNFT(String toAddress) {
        System.out.println(">> Which Wallet? ");
        for (int i = 0; i < walletManager.getNftWallets().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + walletManager.getNftWallets().get(i).getNiceName());
        }
        int walletIndex = Integer.parseInt(scanner.nextLine()) - 1;

        CryptoWallet<NFT> selectedWallet = walletManager.getNftWallets().get(walletIndex);
        System.out.println(">> Which NFT? ");
        for (int i = 0; i < selectedWallet.getHoldings().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + selectedWallet.getHoldings().get(i).getName());
        }
        int nftIndex = Integer.parseInt(scanner.nextLine()) - 1;

        NFT nftToSell = selectedWallet.getHoldings().get(nftIndex);
        selectedWallet.sell(nftToSell, toAddress);

        System.out.println("NFT Sold!");
        System.out.println("Balance for " + selectedWallet.getNiceName() + ": $" + selectedWallet.getTotalValueInUSD());
    }

    private static void sellCryptoCurrency(String toAddress) {
        System.out.println(">> Which Wallet? ");
        for (int i = 0; i < walletManager.getCryptoCurrencyWallets().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + walletManager.getCryptoCurrencyWallets().get(i).getNiceName());
        }
        int walletIndex = Integer.parseInt(scanner.nextLine()) - 1;

        CryptoWallet<CryptoCurrencyHolding> selectedWallet = walletManager.getCryptoCurrencyWallets().get(walletIndex);
        System.out.println(">> Which crypto currency? ");
        for (int i = 0; i < selectedWallet.getHoldings().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + selectedWallet.getHoldings().get(i).getCryptoCurrency().getName());
        }
        int currencyIndex = Integer.parseInt(scanner.nextLine()) - 1;

        System.out.println(">> Which amount? ");
        double amount = Double.parseDouble(scanner.nextLine());

        CryptoCurrencyHolding holdingToSell = selectedWallet.getHoldings().get(currencyIndex);
        CryptoCurrencyHolding sellHolding = new CryptoCurrencyHolding(holdingToSell.getCryptoCurrency(), amount);
        selectedWallet.sell(sellHolding, toAddress);

        System.out.println("Crypto Currency Sold!");
        System.out.println("Balance for " + selectedWallet.getNiceName() + ": $" + selectedWallet.getTotalValueInUSD());
    }

    private static void transferCryptoAssets() {
        System.out.println(">> What type of Asset? ");
        System.out.println(" (1) NFT  (2) Crypto Currency ");
        int type = Integer.parseInt(scanner.nextLine());
    
        if (type == 1) {
            transferNFT();
        } else {
            transferCryptoCurrency(); // This is the correct method to call
        }
    }

    private static void transferNFT() {
        System.out.println(">> From which Wallet? ");
        for (int i = 0; i < walletManager.getNftWallets().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + walletManager.getNftWallets().get(i).getNiceName());
        }
        int fromWalletIndex = Integer.parseInt(scanner.nextLine()) - 1;

        System.out.println(">> To which Wallet? ");
        for (int i = 0; i < walletManager.getNftWallets().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + walletManager.getNftWallets().get(i).getNiceName());
        }
        int toWalletIndex = Integer.parseInt(scanner.nextLine()) - 1;

        CryptoWallet<NFT> fromWallet = walletManager.getNftWallets().get(fromWalletIndex);
        CryptoWallet<NFT> toWallet = walletManager.getNftWallets().get(toWalletIndex);

        System.out.println(">> Which NFT? ");
        for (int i = 0; i < fromWallet.getHoldings().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + fromWallet.getHoldings().get(i).getName());
        }
        int nftIndex = Integer.parseInt(scanner.nextLine()) - 1;

        NFT nftToTransfer = fromWallet.getHoldings().get(nftIndex);
        fromWallet.transferTo(nftToTransfer, toWallet);

        System.out.println("NFT Transferred!");
        System.out.println("Balance for " + fromWallet.getNiceName() + ": $" + fromWallet.getTotalValueInUSD());
        System.out.println("Balance for " + toWallet.getNiceName() + ": $" + toWallet.getTotalValueInUSD());
    }

    private static void transferCryptoCurrency() {
        System.out.println(">> From which Wallet? ");
        for (int i = 0; i < walletManager.getCryptoCurrencyWallets().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + walletManager.getCryptoCurrencyWallets().get(i).getNiceName());
        }
        int fromWalletIndex = Integer.parseInt(scanner.nextLine()) - 1;

        System.out.println(">> To which Wallet? ");
        for (int i = 0; i < walletManager.getCryptoCurrencyWallets().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + walletManager.getCryptoCurrencyWallets().get(i).getNiceName());
        }
        int toWalletIndex = Integer.parseInt(scanner.nextLine()) - 1;

        CryptoWallet<CryptoCurrencyHolding> fromWallet = walletManager.getCryptoCurrencyWallets().get(fromWalletIndex);
        CryptoWallet<CryptoCurrencyHolding> toWallet = walletManager.getCryptoCurrencyWallets().get(toWalletIndex);

        System.out.println(">> Which crypto currency? ");
        for (int i = 0; i < fromWallet.getHoldings().size(); i++) {
            System.out.println(" " + (i + 1) + ". " + fromWallet.getHoldings().get(i).getCryptoCurrency().getName());
        }
        int currencyIndex = Integer.parseInt(scanner.nextLine()) - 1;

        System.out.println(">> Which amount? ");
        double amount = Double.parseDouble(scanner.nextLine());

        CryptoCurrencyHolding holdingToTransfer = fromWallet.getHoldings().get(currencyIndex);
        CryptoCurrencyHolding transferHolding = new CryptoCurrencyHolding(holdingToTransfer.getCryptoCurrency(), amount);
        fromWallet.transferTo(transferHolding, toWallet);

        System.out.println("Crypto Currency Transferred!");
        System.out.println("Balance for " + fromWallet.getNiceName() + ": $" + fromWallet.getTotalValueInUSD());
        System.out.println("Balance for " + toWallet.getNiceName() + ": $" + toWallet.getTotalValueInUSD());
    }

    private static void initializeSampleData() {
        // Add some sample cryptocurrencies
        CryptoCurrency bitcoin = new CryptoCurrency("Bitcoin", "BTC", 68000.1, "Bitcoin");
        CryptoCurrency ethereum = new CryptoCurrency("Ethereum", "ETH", 3500.5, "Ethereum");
        CryptoCurrency tether = new CryptoCurrency("Tether", "USDT", 0.99, "Ethereum");
        
        walletManager.addCryptoCurrency(bitcoin);
        walletManager.addCryptoCurrency(ethereum);
        walletManager.addCryptoCurrency(tether);

        // Create sample wallets
        walletManager.createCryptoCurrencyWallet("Trading Currency Wallet", "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", "mykeyfile5");
        walletManager.createCryptoCurrencyWallet("Long-Term Currency Wallet", "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy", "mykeyfile6");
        walletManager.createNFTWallet("Utility NFT Wallet", "0x1ABC7154748D1CE5144478CDEB574AE244B939B5", "mykeyfile7");
        walletManager.createNFTWallet("Long-Term NFT Wallet", "0x742d35Cc6634C0532925a3b844Bc454e4438f44e", "mykeyfile8");

        // Add some sample holdings
        CryptoWallet<CryptoCurrencyHolding> tradingWallet = walletManager.getCryptoCurrencyWallet(0);
        CryptoWallet<CryptoCurrencyHolding> longTermWallet = walletManager.getCryptoCurrencyWallet(1);
        
        tradingWallet.buy(new CryptoCurrencyHolding(tether, 10), "sample_address");
        tradingWallet.buy(new CryptoCurrencyHolding(bitcoin, 0.001), "sample_address");
        longTermWallet.buy(new CryptoCurrencyHolding(bitcoin, 0.2), "sample_address");

        CryptoWallet<NFT> utilityNFTWallet = walletManager.getNFTWallet(0);
        CryptoWallet<NFT> longTermNFTWallet = walletManager.getNFTWallet(1);

        utilityNFTWallet.buy(new NFT("Ticket for John Doe for Live Music Extravaganza on 2024-08-15", "Your reserved seat for the concert", "0xSAMPLE_CONTRACT_ADDRESS", "Ethereum", 20), "sample_address");
        longTermNFTWallet.buy(new NFT("Laser Eyes Ape", "A cartoon ape with laser eyes", "0xBC4CA0EdA7BcC49921e03F869d7CbDEdC4A8A", "Ethereum", 120000), "sample_address");
    }
}
        
/*import edu.mdc.cop2805c.assignment2.assets.CryptoCurrencyHolding;
import edu.mdc.cop2805c.assignment2.assets.NFT;
//import edu.mdc.cop2805c.assignment2.assets.CryptoCurrency;
import edu.mdc.cop2805c.assignment2.wallet.CryptoWallet;
import edu.mdc.cop2805c.assignment2.wallet.NFTWallet;
//import edu.mdc.cop2805c.assignment2.wallet.WalletManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static WalletManager walletManager = new WalletManager();

    public static void main(String[] args) {
        // Load wallets from file if available
        loadWalletsFromFile();

        // Display menu and process user input
        displayMenu();
    }

    private static void loadWalletsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("WalletManager.dat"))) {
            walletManager = (WalletManager) ois.readObject();
            System.out.println("Wallets loaded successfully from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved wallets found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading wallets from file: " + e.getMessage());
        }
    }

    private static void saveWalletsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("WalletManager.dat"))) {
            oos.writeObject(walletManager);
            System.out.println("Wallets saved successfully to file.");
        } catch (IOException e) {
            System.out.println("Error saving wallets to file: " + e.getMessage());
        }
    }

    private static void displayMenu() {
        String choice;
        do {
            System.out.println("***********************************************************");
            System.out.println("** Please choose your option: ");
            System.out.println("** 1. View all Wallets");
            System.out.println("** 2. View Wallet");
            System.out.println("** 3. Add Wallet");
            System.out.println("** 4. Buy Crypto Assets");
            System.out.println("** 5. Sell Crypto Assets");
            System.out.println("** 6. Transfer Crypto Assets");
            System.out.println("** Q. Quit");
            System.out.println("***********************************************************");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1":
                    viewAllWallets();
                    break;
                case "2":
                    viewWallet();
                    break;
                case "3":
                    addWallet();
                    break;
                case "4":
                    buyCryptoAssets();
                    break;
                case "5":
                    sellCryptoAssets();
                    break;
                case "6":
                    transferCryptoAssets();
                    break;
                case "q":
                    System.out.println("Saving wallets to WalletManager.dat.");
                    saveWalletsToFile();
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        } while (!choice.equals("q"));
    }

    private static void viewAllWallets() {
        System.out.println("Your wallets:");
        walletManager.displayWallets();
    }

    private static void viewWallet() {
        System.out.println("Which Wallet?");
        int index = selectWallet();
        if (index != -1) {
            System.out.println(walletManager.getWallet(index).getLongDescription());
        }
    }

    private static void addWallet() {
        System.out.println("What type of Asset?");
        System.out.println(" (1) NFT  (2) Crypto Currency ");
        String assetTypeChoice = scanner.nextLine().trim();

        if (assetTypeChoice.equals("1")) {
            addNFTWallet();
        } else if (assetTypeChoice.equals("2")) {
            addCryptoCurrencyWallet();
        } else {
            System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    private static void addNFTWallet() {
        System.out.println("Nice name for the wallet:");
        String niceName = scanner.nextLine().trim();

        System.out.println("Address (Public Key):");
        String address = scanner.nextLine().trim();

        System.out.println("Private Key Filename:");
        String privateKeyFilename = scanner.nextLine().trim();

        List<NFT> holdings = new ArrayList<>();
        NFTWallet wallet = new NFTWallet(niceName, address, privateKeyFilename, holdings);
        walletManager.addWallet(wallet);

        System.out.println("NFT Wallet added successfully!");
    }

    private static void addCryptoCurrencyWallet() {
        System.out.println("Nice name for the wallet:");
        String niceName = scanner.nextLine().trim();

        System.out.println("Address (Public Key):");
        String address = scanner.nextLine().trim();

        System.out.println("Private Key Filename:");
        String privateKeyFilename = scanner.nextLine().trim();

        List<CryptoCurrencyHolding> holdings = new ArrayList<>();
        CryptoWallet<CryptoCurrencyHolding> wallet = new CryptoWallet<>(niceName, address, privateKeyFilename, holdings);
        walletManager.addWallet(wallet);

        System.out.println("Crypto Currency Wallet added successfully!");
    }

    private static void buyCryptoAssets() {
        System.out.println("From which address?");
        String fromAddress = scanner.nextLine().trim();

        System.out.println("What type of Asset?");
        System.out.println(" (1) NFT  (2) Crypto Currency ");
        String assetTypeChoice = scanner.nextLine().trim();

        if (assetTypeChoice.equals("1")) {
            buyNFT(fromAddress);
        } else if (assetTypeChoice.equals("2")) {
            buyCryptoCurrency(fromAddress);
        } else {
            System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    private static void buyNFT(String fromAddress) {
        int index = selectWallet("Which Wallet?", NFTWallet.class);
        if (index != -1) {
            NFTWallet wallet = walletManager.getWallet(index);
            System.out.println("Name of the NFT?");
            String name = scanner.nextLine().trim();

            System.out.println("Description of the NFT?");
            String description = scanner.nextLine().trim();

            System.out.println("Contract Address of the NFT?");
            String contractAddress = scanner.nextLine().trim();

            System.out.println("Blockchain network of the NFT?");
            String blockchainNetwork = scanner.nextLine().trim();

            System.out.println("Value in USD of the NFT?");
            double valueInUSD = Double.parseDouble(scanner.nextLine().trim());

            NFT nft = new NFT(name, description, contractAddress, blockchainNetwork, valueInUSD);
            wallet.getHoldings().add(nft);
            System.out.println("NFT Bought!");
            System.out.printf("Balance for %s: $%.2f\n", wallet.getNiceName(), wallet.getTotalValueInUSD());
        }
    }

    private static void buyCryptoCurrency(String fromAddress) {
        int index = selectWallet("Which Wallet?", CryptoWallet.class);
        if (index != -1) {
            CryptoWallet<CryptoCurrencyHolding> wallet = walletManager.getCryptoWallet(index);
            System.out.println("Which crypto currency?");
            int cryptoIndex = selectCryptoCurrency(wallet);
            if (cryptoIndex != -1) {
                CryptoCurrency currency = wallet.getHoldings().get(cryptoIndex).getCryptoCurrency();

                System.out.println("Which amount?");
                double amount = Double.parseDouble(scanner.nextLine().trim());

                // Implement logic to buy crypto currency here (not fully implemented in this example)
                // For demonstration purpose, just print a message
                System.out.printf("Crypto Currency Bought! Balance for %s: $%.4f\n", wallet.getNiceName(), wallet.getTotalValueInUSD());
            }
        }
    }

    private static void sellCryptoAssets() {
        // Implement logic to sell crypto assets
        System.out.println("Functionality to sell crypto assets is not implemented yet.");
    }

    private static void transferCryptoAssets() {
        // Implement logic to transfer crypto assets
        System.out.println("Functionality to transfer crypto assets is not implemented yet.");
    }

    private static int selectWallet() {
        walletManager.displayWallets();
        System.out.print("Enter wallet number: ");
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index < 0 || index >= walletManager.getNumWallets()) {
            System.out.println("Invalid wallet number. Please try again.");
            return -1;
        }
        return index;
    }

    private static <T extends WalletStorable> int selectWallet(String message, Class<T> walletType) {
        System.out.println(message);
        List<T> wallets = walletManager.getWalletsOfType(walletType);
        for (int i = 0; i < wallets.size(); i++) {
            System.out.printf(" %d. %s\n", i + 1, wallets.get(i).getShortDescription());
        }
        System.out.print("Enter wallet number: ");
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index < 0 || index >= wallets.size()) {
            System.out.println("Invalid wallet number. Please try again.");
            return -1;
        }
        return walletManager.indexOf(wallets.get(index));
    }

    private static int selectCryptoCurrency(CryptoWallet<CryptoCurrencyHolding> wallet) {
        List<CryptoCurrencyHolding> holdings = wallet.getHoldings();
        for (int i = 0; i < holdings.size(); i++) {
            System.out.printf(" %d. %s\n", i + 1, holdings.get(i).getCryptoCurrency().toString());
        }
        System.out.print("Enter crypto currency number: ");
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index < 0 || index >= holdings.size()) {
            System.out.println("Invalid crypto currency number. Please try again.");
            return -1;
        }
        return index;
    }
}*/
