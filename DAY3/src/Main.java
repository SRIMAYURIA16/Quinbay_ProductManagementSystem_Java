import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        Logger monoLogger = Logger.getLogger("mongodb.driver");
        monoLogger.setLevel(Level.OFF);
        ProductManagement management = new ProductManagement();
        PurchaseProduct purchase = new PurchaseProduct();

        while (!exit) {
            System.out.println("1. Add Products");
            System.out.println("2. View products by Id");
            System.out.println("3. View available Products");
            System.out.println("4. Update Stocks");
            System.out.println("5. Update Price");
            System.out.println("6. Update Product Name");
            System.out.println("7. Delete a product");
            System.out.println("8. Restock a product");
            System.out.println("9. Purchase Product");
            System.out.println("10. Purchase History");
            System.out.println("11. Exit");
            System.out.println("Enter your choice:");

            try {
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("Enter the product Name:");
                        String productName = sc.nextLine();
                        System.out.println("Enter the product Price:");
                        double productPrice = sc.nextDouble();
                        System.out.println("Enter the number of products available:");
                        int stocksAvail = sc.nextInt();
                        System.out.println("Enter the category id:");
                        int categoryId = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Enter the category name:");
                        String categoryName = sc.nextLine();
                        Product.Category category = new Product.Category(categoryId, categoryName);
                        management.addProduct(productName, productPrice, stocksAvail, category);
                        break;
                    case 2:
                        System.out.println("Enter the product ID:");
                        int productIdToView = sc.nextInt();
                        management.viewProductById(productIdToView);
                        break;
                    case 3:
                        management.viewAllProducts();
                        break;
                    case 4:
                        System.out.println("Enter the product ID to update stocks:");
                        int productIdToUpdateStocks = sc.nextInt();
                        System.out.println("Enter the new stocks count:");
                        int updatedStocks = sc.nextInt();
                        management.updateStocks(productIdToUpdateStocks, updatedStocks);
                        break;
                    case 5:
                        System.out.println("Enter the product ID to update price:");
                        int productIdToUpdatePrice = sc.nextInt();
                        System.out.println("Enter the new price:");
                        double updatedPrice = sc.nextDouble();
                        management.updatePrice(productIdToUpdatePrice, updatedPrice);
                        break;
                    case 6:
                        System.out.println("Enter the product ID to update name:");
                        int productIdToUpdateName = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Enter the new name:");
                        String updatedName = sc.nextLine();
                        management.updateName(productIdToUpdateName, updatedName);
                        break;
                    case 7:
                        System.out.println("Enter the product ID to delete:");
                        int productIdToDelete = sc.nextInt();
                        management.deleteProduct(productIdToDelete);
                        break;
                    case 8:
                        System.out.println("Enter the product ID to restock:");
                        int productIdToRestock = sc.nextInt();
                        management.restockProduct(productIdToRestock);
                        break;
                    case 9:
                        purchase.purchaseProductFromMongoDB(sc);
                        break;
                    case 10:
                        purchase.displayPurchaseHistory();
                        break;
                    case 11:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a valid option.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Error occurred: " + e.getMessage());
//                sc.nextLine();
            }
        }

    }
}
