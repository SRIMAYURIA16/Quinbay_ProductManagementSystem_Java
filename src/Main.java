import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        boolean exit=false;
        ProductManagement management=new ProductManagement();
        UpdateManagement updatemanage=new UpdateManagement();
        Purchase purchase=new Purchase();
        while(!exit){
            System.out.println("1.Add Products");
            System.out.println("2.View products by Id");
            System.out.println("3.View availableProducts");
            System.out.println("4.Update Stocks");
            System.out.println("5.Update Price");
            System.out.println("6.Update productName");
            System.out.println("7.Purchase Product");
            System.out.println("8.View purchase History");
            System.out.println("9.Delete a product");
            System.out.println("10.Restock a product");
            System.out.println("11.Exit");
            System.out.println("Enter your choice");
            int choice=sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1:
                    System.out.println("Enter the productName");
                    String productName=sc.nextLine();
                    System.out.println("Enter the productPrice");
                    double productPrice=sc.nextDouble();
                    System.out.println("Enter no.of products available");
                    int stocksAvail=sc.nextInt();
                    management.addProducts(productName,productPrice,stocksAvail);
                    break;

                case 2:
                    System.out.println("Enter the productId to view");
                    int productIdToView=sc.nextInt();
                    management.viewProductsById(productIdToView);
                    break;
                case 3:
                    management.viewAllProducts();
                    break;

                case 4:
                    System.out.println("Enter the productId to updateStocks");
                    int productIdToUpdate=sc.nextInt();
                    System.out.println("Enter the updated stocks");
                    int updatedStocks=sc.nextInt();
                    if(updatedStocks>0){
                        management.updateStocks(productIdToUpdate,updatedStocks);
                    }
                    else{
                        System.out.println("Enter valid stocks");
                    }
                    break;

                case 5:
                    System.out.println("Enter the productId to updatePrice");
                    int IdToUpdate=sc.nextInt();
                    System.out.println("Enter the amount to be updated");
                    double updatedAmount=sc.nextDouble();
                    if(updatedAmount>0){
                        management.updatePrice(IdToUpdate,updatedAmount);
                    }
                    else{
                        System.out.println("Enter valid price");
                    }
                case 6:
                    System.out.println("Enter the productId to updateName");
                    int IdToUpdateName=sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter the name to be updated");
                    try {
                        String name = sc.nextLine();
                        management.updateName(IdToUpdateName,name);
                    }
                    catch(InputMismatchException error){
                        System.out.println(error.getMessage());
                    }
                    break;
                case 7:
                    System.out.println("Enter the productId to purchase");
                    int idToPurchase=sc.nextInt();
                    System.out.println("Enter the no.of stocks to be purchased");
                    int stocksToPurchase=sc.nextInt();
                    purchase.puchaseProduct(idToPurchase,stocksToPurchase);
                    break;
                case 8:
                    purchase.displayPurchaseHistory();
                    break;

                case 9:
                    System.out.println("Enter the productIdToDelete");
                    int idToDelete=sc.nextInt();
                    management.deleteProduct(idToDelete);
                    break;
                case 10:
                    System.out.println("Want to restock a deleted product?");
                    String option=sc.nextLine().toLowerCase();
                    if(option.equals("yes")){
                        System.out.println("Enter the id you want to restock: ");
                        int restockId=sc.nextInt();
                        management.restockProduct(restockId);
                        break;
                    }
                case 11:
                    exit=true;
                    break;
                default:
                    System.out.println("Invalid choice. Kindly choose a valid choice");
                    break;
            }
        }
    }
}
