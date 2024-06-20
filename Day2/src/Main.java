import java.util.*;
import java.util.InputMismatchException;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        ProductManagement management = new ProductManagement();
        Purchase purchase = new Purchase();

        while (!exit) {
            System.out.println("1. Add Products");
            System.out.println("2. View products by Id");
            System.out.println("3. View available Products");
            System.out.println("4. Update Stocks");
            System.out.println("5. Update Price");
            System.out.println("6. Update Product Name");
            System.out.println("7. Purchase Product");
            System.out.println("8. View Purchase History");
            System.out.println("9. Delete a product");
            System.out.println("10. Restock a product");
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
                        if (productPrice > 0 && stocksAvail > 0) {
                            management.addProducts(productName, productPrice, stocksAvail);
                        } else {
                            if (productPrice <= 0) {
                                System.out.println("Invalid product price.");
                            }
                            if (stocksAvail <= 0) {
                                System.out.println("Invalid stocks.");
                            }
                        }
                        break;

                    case 2:
                        System.out.println("Enter the product Id to view:");
                        int productIdToView = sc.nextInt();
                        management.viewProductsById(productIdToView);
                        break;

                    case 3:
                        management.viewAllProducts();
                        break;

                    case 4:
                        System.out.println("Enter the product Id to update Stocks:");
                        int productIdToUpdate = sc.nextInt();
                        System.out.println("Enter the updated stocks:");
                        int updatedStocks = sc.nextInt();
                        if (updatedStocks > 0) {
                            management.updateStocks(productIdToUpdate, updatedStocks);
                        } else {
                            System.out.println("Enter valid stocks.");
                        }
                        break;

                    case 5:
                        System.out.println("Enter the product Id to update Price:");
                        int IdToUpdate = sc.nextInt();
                        System.out.println("Enter the amount to be updated:");
                        double updatedAmount = sc.nextDouble();
                        if (updatedAmount > 0) {
                            management.updatePrice(IdToUpdate, updatedAmount);
                        } else {
                            System.out.println("Enter valid price.");
                        }
                        break;

                    case 6:
                        System.out.println("Enter the product Id to update Name:");
                        int IdToUpdateName = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Enter the name to be updated:");
                        String name = sc.nextLine();
                        management.updateName(IdToUpdateName, name);
                        break;

                    case 7:
                        System.out.println("Enter the product Id to purchase:");
                        int idToPurchase = sc.nextInt();
                        System.out.println("Enter the number of stocks to be purchased:");
                        int stocksToPurchase = sc.nextInt();
                        PurchaseThread purchaseThread = new PurchaseThread(idToPurchase, stocksToPurchase, purchase);
                        Thread thread = new Thread(purchaseThread);
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            System.out.println("Thread interrupted: " + e.getMessage());
                        }
                        break;

                    case 8:
                        purchase.displayPurchaseHistory();
                        break;

                    case 9:
                        System.out.println("Enter the product Id to delete:");
                        int idToDelete = sc.nextInt();
                        management.deleteProduct(idToDelete);
                        break;

                    case 10:
                        System.out.println("Want to restock a deleted product? (yes/no)");
                        String option = sc.next().toLowerCase();
                        if (option.equals("yes")) {
                            System.out.println("Enter the ID of the product to restock:");
                            int restockId = sc.nextInt();
                            management.restockProduct(restockId);
                        } else {
                            System.out.println("Invalid option. Please enter 'yes' or 'no'.");
                        }
                        break;

                    case 11:
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a valid option (1-11).");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine();
            }
        }

    }
}
