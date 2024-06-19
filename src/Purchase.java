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
            String lines;
            while ((lines = reader.readLine()) != null) {
                String[] part = lines.split(",");
                int purchaseId = Integer.parseInt(part[0]);
                String purchaseCode = part[1];
                String productCode = part[2];
                String productName = part[3];
                int stocksPurchased = Integer.parseInt(part[4]);
                double actualAmount = Double.parseDouble(part[5]);
                double totalAmount = Double.parseDouble(part[6]);
                PurchaseEntity purchase = new PurchaseEntity(purchaseId, purchaseCode, productName, stocksPurchased, actualAmount, totalAmount);
                purchaseList.add(purchase);
            }
        }
        catch (FileNotFoundException | NumberFormatException error) {
            System.out.println("Error processing " + purchase_file + ": " + error.getMessage());
        }
        catch (IOException error) {
            System.out.println("Error processing " + purchase_file + ": " + error.getMessage());
        }
    }

    public void puchaseProduct(int idToPurchase, int purchasingStocks) {
        ProductManagement management = new ProductManagement();
        Product product = management.getProduct(idToPurchase);
        if (product != null && !product.isDeleted()) {
            if (product.getStocksAvail() >= purchasingStocks) {

                product.setStocksAvail(product.getStocksAvail() - purchasingStocks);
                management.saveUpdate();

                int purchaseId = getPurchaseId() + 1;
                String purchaseCode = "PUR"+ purchaseId;
                double actualAmount = product.getProductPrice() * purchasingStocks;
                double totalAmount = actualAmount*purchasingStocks;
//                System.out.println(product.getProductCode);
                PurchaseEntity purchase = new PurchaseEntity(purchaseId, purchaseCode, product.getProductName(),
                        purchasingStocks, actualAmount, totalAmount);
                purchaseList.add(purchase);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(purchase_file, true))) {
//                    System.out.println(product.getProductCode);
                    writer.write(purchaseId+","+purchaseCode+","+product.getProductName()+","+purchasingStocks+","+actualAmount+","+totalAmount);
                    writer.newLine();
                    System.out.println("Product purchased successfully!");
                } catch (IOException error) {
                    System.out.println("Error processing " + purchase_file + ": " + error.getMessage());
                }
            } else {
                System.out.println("Insufficient stock available for purchase.");
            }
        } else {
            System.out.println("Product ID " + idToPurchase + " not found or deleted.");
        }
    }

    public int getPurchaseId() {
        int maximumId = 100;
        for (PurchaseEntity purchase : purchaseList) {
            if (purchase.getPurchaseID() > maximumId) {
                maximumId = purchase.getPurchaseID();
            }
        }
        return maximumId;
    }
    public void displayPurchaseHistory(){
        try(BufferedReader reader=new BufferedReader(new FileReader(purchase_file))){
            String lines;
            while((lines= reader.readLine())!=null){
                String parts[]=lines.split(",");
                int purchaseId=Integer.parseInt(parts[0]);
                String purchaseCode=parts[1];
                String productName=parts[2];
                int stocksPurchased=Integer.parseInt(parts[3]);
                double actualAmount=Double.parseDouble(parts[4]);
                double totalamount=Double.parseDouble(parts[5]);
                System.out.println("Purchase ID: " + purchaseId);
                System.out.println("Purchase Code: " + purchaseCode);
                System.out.println("Product Name: " + productName);
                System.out.println("Stocks Purchased: " + stocksPurchased);
                System.out.println("Actual Amount: " + actualAmount);
                System.out.println("Total Amount: " + totalamount);
                System.out.println("===============================");
            }
        }
        catch (FileNotFoundException | NumberFormatException error) {
            System.out.println("Error processing " + purchase_file + ": " + error.getMessage());
        }
        catch (IOException error) {
            System.out.println("Error processing " + purchase_file + ": " + error.getMessage());
        }
    }
}
