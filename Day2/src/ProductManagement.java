import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductManagement {

    private final String file_name = "products.txt";
    private List<Product> productsList = new ArrayList<>();

    public ProductManagement() {
        loadFiles();
    }

    public void addProducts(String productName, double productPrice, int stocksAvailable) {
        int productId = lastProductId() + 1;
        String productCode = "PROD" + productId;
        Product product = new Product(productId, productCode, productName, productPrice, stocksAvailable, false);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_name, true))) {
            writer.write(product.toString());
            writer.newLine();
            System.out.println("Product added successfully!!!");
            System.out.println("===============================");
        } catch (IOException error) {
            System.out.println("Error adding product: " + error.getMessage());
        }
        productsList.add(product);
    }

    public int lastProductId() {
        int maximumId = 100;
        for (Product product : productsList) {
            if (product.getProductId() > maximumId) {
                maximumId = product.getProductId();
            }
        }
        return maximumId;
    }

    public void loadFiles() {
        productsList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file_name))) {
            String lines;
            while ((lines = reader.readLine()) != null) {
                String[] part = lines.split(",");
                int productId = Integer.parseInt(part[0]);
                String productCode = part[1];
                String productName = part[2];
                double productPrice = Double.parseDouble(part[3]);
                int stocksAvail = Integer.parseInt(part[4]);
                boolean deletionStatus = Boolean.parseBoolean(part[5]);
                Product product = new Product(productId, productCode, productName, productPrice, stocksAvail, deletionStatus);
                productsList.add(product);
            }
        } catch (FileNotFoundException | NumberFormatException error) {
            System.out.println("Error processing products.txt: " + error.getMessage());
        } catch (IOException error) {
            System.out.println("Error processing products.txt: " + error.getMessage());
        }
    }

    public void viewProductsById(int productIdToView) {
        boolean found = false;
        for (Product product : productsList) {
            if (product.getProductId() == productIdToView) {
                System.out.println("Product Id: " + product.getProductId());
                System.out.println("Product Code: " + product.getProductCode());
                System.out.println("Product Name: " + product.getProductName());
                System.out.println("Product Price: " + product.getProductPrice());
                System.out.println("Available Stocks: " + product.getStocksAvail());
                System.out.println("Deletion Status: " + product.isDeleted());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("The product is not available now. Please come again later.");
        }
    }

    public void viewAllProducts() {
        if (productsList.isEmpty()) {
            System.out.println("No products available at the moment. Please check again later.");
        } else {
            System.out.println("==== Available Products ====");
            for (Product product : productsList) {
                System.out.println("Product Id: " + product.getProductId());
                System.out.println("Product Code: " + product.getProductCode());
                System.out.println("Product Name: " + product.getProductName());
                System.out.println("Product Price: " + product.getProductPrice());
                System.out.println("Available Stocks: " + product.getStocksAvail());
                System.out.println("Deletion Status: " + product.isDeleted());
                System.out.println("====================");
            }
        }
    }

    public Product getProduct(int productId) {
        for (Product product : productsList) {
            if (productId == product.getProductId()) {
                return product;
            }
        }
        return null;
    }

    synchronized public void saveUpdate() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_name, false))) {
            for (Product product : productsList) {
                writer.write(product.getProductId() + "," + product.getProductCode() + "," +
                        product.getProductName() + "," + product.getProductPrice() + "," +
                        product.getStocksAvail() + "," + product.isDeleted());
                writer.newLine();
            }
//            System.out.println("Changes saved successfully.");
        } catch (IOException error) {
            System.out.println("Error processing " + file_name + ": " + error.getMessage());
        }
    }

    synchronized public void updateStocks(int productIdToUpdate, int updatedStocks) {
        Product product = getProduct(productIdToUpdate);
        Scanner sc=new Scanner(System.in);
        if (product != null && !product.isDeleted()) {
            System.out.println("1.Override Stock");
            System.out.println("2.Replace Stock");
            int option=sc.nextInt();
            switch(option){
                case 1:
                    System.out.println("1.Want to add to the existing stock");
                    System.out.println("2.Want to reduce from the existing stock");
                    int choice=sc.nextInt();
                    int currentStocks = product.getStocksAvail();
                    switch(choice){
                        case 1:

                            product.setStocksAvail(currentStocks-updatedStocks);
                            break;
                        case 2:
                            if(currentStocks>=updatedStocks) {
                                product.setStocksAvail(currentStocks-updatedStocks);
                            }
                            else{
                                System.out.println("Insufficient stocks to update");
                            }
                            break;
                        default:
                            System.out.println("Invalid option!!!");
                            break;
                    }

                    break;
                case 2:
                    product.setStocksAvail(updatedStocks);
                    break;
            }
            saveUpdate();
            System.out.println("Stocks updated successfully for Product ID: " + productIdToUpdate);
        } else {
            System.out.println("Product ID " + productIdToUpdate + " not found or deleted.");
        }
    }

    synchronized public void updatePrice(int productIdToUpdate, double updatedPrice) {
        Product product = getProduct(productIdToUpdate);
        Scanner sc=new Scanner(System.in);
        if (product != null && !product.isDeleted()) {
            System.out.println("1.Override Price");
            System.out.println("2.Replace Price");
            int option=sc.nextInt();
            switch(option){
                case 1:
                    System.out.println("1.Want to add to the existing price");
                    System.out.println("2.Want to reduce from the existing price");
                    int choice=sc.nextInt();
                    double currentPrice = product.getProductPrice();
                    switch(choice){
                        case 1:
                            product.setProductPrice(currentPrice-updatedPrice);
                            break;
                        case 2:
                            if(currentPrice>=updatedPrice) {
                                product.setProductPrice(currentPrice-updatedPrice);
                            }
                            else{
                                System.out.println("Insufficient amount to update");
                            }
                            break;
                        default:
                            System.out.println("Invalid option!!!");
                            break;
                    }

                    break;
                case 2:
                    product.setProductPrice(updatedPrice);
                    break;
            }
            saveUpdate();
            System.out.println("Stocks updated successfully for Product ID: " + productIdToUpdate);
        } else {
            System.out.println("Product ID " + productIdToUpdate + " not found or deleted.");
        }
    }



    synchronized public void updateName(int productIdToUpdate, String updatedName) {
        Product product = getProduct(productIdToUpdate);
        if (product != null && !product.isDeleted()) {
            product.setProductName(updatedName);
            saveUpdate();
            System.out.println("Product name updated successfully for Product ID: " + productIdToUpdate);
        } else {
            System.out.println("Product ID " + productIdToUpdate + " not found or deleted.");
        }
    }

    public void deleteProduct(int productIdToDelete){
        Product product=getProduct(productIdToDelete);
        if(product!=null && !product.isDeleted()){
            product.setDeletionStatus(true);
            System.out.println("Product deleted successfully!!!");
            saveUpdate();
        }
        else{
            System.out.println("Not available already");
        }
    }

    public void restockProduct(int productIdToRestock){
        Product product=getProduct(productIdToRestock);
        if(product!=null && product.isDeleted()){
            product.setDeletionStatus(false);
            saveUpdate();
        }
        else{
            System.out.println("Product is available already");
        }
    }


}
