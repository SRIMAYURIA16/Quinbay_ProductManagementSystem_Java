import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Purchase {
    private final String purchase_file = "purchase_history.txt";
    private List<PurchaseEntity> purchaseList = new ArrayList<>();

    public Purchase() {
        loadPurchaseFiles();
    }

    public void loadPurchaseFiles() {
        purchaseList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(purchase_file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int purchaseId = Integer.parseInt(parts[0]);
                String purchaseCode = parts[1];
                String productName = parts[2];
                int stocksPurchased = Integer.parseInt(parts[3]);
                double actualAmount = Double.parseDouble(parts[4]);
                double totalAmount = Double.parseDouble(parts[5]);
                PurchaseEntity purchase = new PurchaseEntity(purchaseId, purchaseCode, productName, stocksPurchased, actualAmount, totalAmount);
                purchaseList.add(purchase);
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            System.out.println("Error loading purchase history file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading purchase history file: " + e.getMessage());
        }
    }

    synchronized public void addPurchase(PurchaseEntity purchase) {
        purchaseList.add(purchase);
        savePurchaseToFile(purchase);
    }

    private void savePurchaseToFile(PurchaseEntity purchase) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(purchase_file, true))) {
            writer.write(purchase.toString());
            writer.newLine();
//            System.out.println("Purchase saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving purchase to file: " + e.getMessage());
        }
    }

    public int getNextPurchaseId() {
        return purchaseList.isEmpty() ? 1 : purchaseList.get(purchaseList.size() - 1).getPurchaseID() + 1;
    }

    public void displayPurchaseHistory() {
        for (PurchaseEntity purchase : purchaseList) {
            System.out.println(purchase.toString());
            System.out.println("===============================");
        }
    }
}
