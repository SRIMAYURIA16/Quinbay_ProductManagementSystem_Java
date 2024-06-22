import com.mongodb.client.MongoClient;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        ProductManagement management = new ProductManagement();
        PurchaseProduct purchase = new PurchaseProduct();
        CategoryManagement category=new CategoryManagement();
        while (!exit) {
            System.out.println("1. Add Category");
            System.out.println("2. View All Category");
            System.out.println("3. Update the CategoryName");
            System.out.println("4. Delete the Category");
            System.out.println("5. Add Products");
            System.out.println("6. View products by Id");
            System.out.println("7. View available Products");
            System.out.println("8. Update Stocks");
            System.out.println("9. Update Price");
            System.out.println("10. Update Product Name");
            System.out.println("11. Delete a product");
            System.out.println("12. Restock a product");
            System.out.println("13. Purchase Product");
            System.out.println("14. Purchase History");
            System.out.println("15. Exit");
            System.out.println("Enter your choice:");

            try {
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:

                        System.out.println("Enter the Category Name");
                        String categoryName=sc.nextLine();
                        String lowerCaseCategoryName=categoryName.toLowerCase();
                        category.addCategory(lowerCaseCategoryName);
                        break;
                    case 2:
                        category.viewCategory();
                        break;
                    case 3:
                        System.out.println("Enter the categoryId to update the name");
                        int categoryIdToUpdate=sc.nextInt();
                        sc.nextLine();
                        System.out.println("Enter the new CategoryName");
                        String newName=sc.nextLine();
                        category.updateCategoryName(categoryIdToUpdate,newName);
                        break;
                    case 4:
                        System.out.println("Enter the categoryId to delete");
                        int idToDelete=sc.nextInt();
                        category.deleteCategory(idToDelete);
                        break;
                    case 5:
                        System.out.println("Enter the product Name:");
                        String productName = sc.nextLine();
                        System.out.println("Enter the product Price:");
                        double productPrice = sc.nextDouble();
                        System.out.println("Enter the number of products available:");
                        int stocksAvail = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Enter the categoryName:");
                        String productCategory=sc.nextLine().toLowerCase();
                        management.addProduct(productName, productPrice, stocksAvail,productCategory);
                        break;
                    case 6:
                        System.out.println("Enter the product ID:");
                        int productIdToView = sc.nextInt();
                        management.viewProductById(productIdToView);
                        break;
                    case 7:
                        management.viewAllProducts();
                        break;
                    case 8:
                        System.out.println("Enter the product ID to update stocks:");
                        int productIdToUpdateStocks = sc.nextInt();
                        System.out.println("Enter the new stocks count:");
                        int updatedStocks = sc.nextInt();
                        management.updateStocks(productIdToUpdateStocks, updatedStocks);
                        break;
                    case 9:
                        System.out.println("Enter the product ID to update price:");
                        int productIdToUpdatePrice = sc.nextInt();
                        System.out.println("Enter the new price:");
                        double updatedPrice = sc.nextDouble();
                        management.updatePrice(productIdToUpdatePrice, updatedPrice);
                        break;
                    case 10:
                        System.out.println("Enter the product ID to update name:");
                        int productIdToUpdateName = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Enter the new name:");
                        String updatedName = sc.nextLine();
                        management.updateName(productIdToUpdateName, updatedName);
                        break;
                    case 11:
                        System.out.println("Enter the product ID to delete:");
                        int productIdToDelete = sc.nextInt();
                        management.deleteProduct(productIdToDelete);
                        break;
                    case 12:
                        System.out.println("Enter the product ID to restock:");
                        int productIdToRestock = sc.nextInt();
                        management.restockProduct(productIdToRestock);
                        break;
                    case 13:
                        purchase.purchaseProductFromMongoDB(sc);
                        break;
                    case 14:
                        purchase.displayPurchaseHistory();
                        break;
                    case 15:
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
