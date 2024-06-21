import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Updates.*;
import java.util.InputMismatchException;
import java.util.Scanner;
public class ProductManagement {
    private final String DATABASE_NAME = "Training";
    private final String COLLECTION_NAME = "Product";
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;  //(key)BSON Document json like format used to store data in mongodb

    public ProductManagement() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    public void addProduct(String productName, double productPrice, int stocksAvailable, Product.Category category) {
        int productId = lastProductId() + 1;
        String productCode = "PROD" + productId;
        Product product = new Product(productId, productCode, productName, productPrice, stocksAvailable, false, category);

        Document categoryDocument = new Document("id", category.getId())
                .append("name", category.getName());
        Document productDocument = new Document("productId", product.getProductId())
                .append("productCode", product.getProductCode())
                .append("productName", product.getProductName())
                .append("productPrice", product.getProductPrice())
                .append("stocksAvail", product.getStocksAvail())
                .append("deletionStatus", product.isDeleted())
                .append("category", categoryDocument);

        collection.insertOne(productDocument);
        System.out.println("Product added successfully!!!");
        System.out.println("===============================");
    }

    public int lastProductId() {
        Document lastProduct = collection.find().sort(new Document("productId", -1)).first();
        return (lastProduct != null) ? lastProduct.getInteger("productId") : 100;
    }

    public void viewProductById(int productIdToView) {
        Document productDocument = collection.find(Filters.eq("productId", productIdToView)).first();
        if (productDocument != null) {
            printProduct(productDocument);
        } else {
            System.out.println("The product is not available now. Please come again later.");
        }
    }

    public void viewAllProducts() {
        FindIterable<Document> products = collection.find();
        if (products.first() == null) {
            System.out.println("No products available at the moment. Please check again later.");
        } else {
            System.out.println("==== Available Products ====");
            for (Document product : products) {
                printProduct(product);
                System.out.println("====================");
            }
        }
    }

    private void printProduct(Document product) {
        System.out.println("Product Id: " + product.getInteger("productId"));
        System.out.println("Product Code: " + product.getString("productCode"));
        System.out.println("Product Name: " + product.getString("productName"));
        System.out.println("Product Price: " + product.getDouble("productPrice"));
        System.out.println("Available Stocks: " + product.getInteger("stocksAvail"));
        System.out.println("Deletion Status: " + product.getBoolean("deletionStatus"));

        Document categoryDocument = product.get("category", Document.class);
        if (categoryDocument != null) {
            System.out.println("Category Id: " + categoryDocument.getInteger("id"));
            System.out.println("Category Name: " + categoryDocument.getString("name"));
        } else {
            System.out.println("Category: null");
        }
    }

    public void updateStocks(int productIdToUpdate, int updatedStocks) {
        Document product = collection.find(Filters.eq("productId", productIdToUpdate)).first();
        Scanner sc = new Scanner(System.in);

        if (product != null && !product.getBoolean("deletionStatus")) {
            System.out.println("1. Override Stocks");
            System.out.println("2. Replace Stocks");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    System.out.println("1. Want to add to the existing stocks");
                    System.out.println("2. Want to reduce from the existing stocks");
                    int choice = sc.nextInt();
                    sc.nextLine();

                    int currentStocks = product.getInteger("stocksAvail");
                    switch (choice) {
                        case 1:
                            int newStocksAddition = currentStocks + updatedStocks;
                            collection.updateOne(Filters.eq("productId", productIdToUpdate), set("stocksAvail", newStocksAddition));
                            System.out.println("Stocks updated successfully for Product ID: " + productIdToUpdate);
                            break;
                        case 2:
                            if (currentStocks >= updatedStocks) {
                                int newStocksReduction = currentStocks - updatedStocks;
                                collection.updateOne(Filters.eq("productId", productIdToUpdate), set("stocksAvail", newStocksReduction));
                                System.out.println("Stocks updated successfully for Product ID: " + productIdToUpdate);
                            } else {
                                System.out.println("Insufficient stocks to update.");
                            }
                            break;
                        default:
                            System.out.println("Invalid option.");
                            break;
                    }
                    break;
                case 2:
                    collection.updateOne(Filters.eq("productId", productIdToUpdate), set("stocksAvail", updatedStocks));
                    System.out.println("Stocks updated successfully for Product ID: " + productIdToUpdate);
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } else {
            System.out.println("Product ID " + productIdToUpdate + " not found or deleted.");
        }
    }
    public void updatePrice(int productIdToUpdate, double updatedPrice) {
        Document product = collection.find(Filters.eq("productId", productIdToUpdate)).first();
        Scanner sc = new Scanner(System.in);

        if (product != null && !product.getBoolean("deletionStatus")) {
            System.out.println("1. Override Price");
            System.out.println("2. Replace Price");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    System.out.println("1. Want to add to the existing price");
                    System.out.println("2. Want to reduce from the existing price");
                    int choice = sc.nextInt();
                    sc.nextLine();

                    double currentPrice = product.getDouble("productPrice");
                    switch (choice) {
                        case 1:
                            double newPriceAddition = currentPrice + updatedPrice;
                            collection.updateOne(Filters.eq("productId", productIdToUpdate), set("productPrice", newPriceAddition));
                            System.out.println("Price updated successfully for Product ID: " + productIdToUpdate);
                            break;
                        case 2:
                            if (currentPrice >= updatedPrice) {
                                double newPriceReduction = currentPrice - updatedPrice;
                                collection.updateOne(Filters.eq("productId", productIdToUpdate), set("productPrice", newPriceReduction));
                                System.out.println("Price updated successfully for Product ID: " + productIdToUpdate);
                            } else {
                                System.out.println("Insufficient amount to update.");
                            }
                            break;
                        default:
                            System.out.println("Invalid option.");
                            break;
                    }
                    break;
                case 2:
                    collection.updateOne(Filters.eq("productId", productIdToUpdate), set("productPrice", updatedPrice));
                    System.out.println("Price updated successfully for Product ID: " + productIdToUpdate);
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } else {
            System.out.println("Product ID " + productIdToUpdate + " not found or deleted.");
        }
    }

    public void updateName(int productIdToUpdate, String updatedName) {
        Document product = collection.find(Filters.eq("productId", productIdToUpdate)).first();
        if (product != null && !product.getBoolean("deletionStatus")) {
            collection.updateOne(Filters.eq("productId", productIdToUpdate), set("productName", updatedName));
            System.out.println("Product name updated successfully for Product ID: " + productIdToUpdate);
        } else {
            System.out.println("Product ID " + productIdToUpdate + " not found or deleted.");
        }
    }

    public void deleteProduct(int productIdToDelete) {
        Document product = collection.find(Filters.eq("productId", productIdToDelete)).first();
        if (product != null && !product.getBoolean("deletionStatus")) {
            collection.updateOne(Filters.eq("productId", productIdToDelete), set("deletionStatus", true));
            System.out.println("Product deleted successfully!!!");
        } else {
            System.out.println("Not available");
        }
    }

    public void restockProduct(int productIdToRestock) {
        Document product = collection.find(Filters.eq("productId", productIdToRestock)).first();
        Scanner sc = new Scanner(System.in);

        if (product != null) {
            if (product.getBoolean("deletionStatus")) {
                System.out.println("Enter the number of stocks to restock:");
                int restockStocks;
                try {
                    restockStocks = sc.nextInt();
                    sc.nextLine();
                }
                catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    return;
                }
                if (restockStocks < 0) {
                    System.out.println("Number of stocks to restock cannot be negative.");
                    return;
                }

                int currentStocks = product.getInteger("stocksAvail");
                int newStocks = currentStocks + restockStocks;
                collection.updateOne(
                        Filters.eq("productId", productIdToRestock),
                        Updates.combine(
                                Updates.set("stocksAvail", newStocks),
                                Updates.set("deletionStatus", false)
                        )
                );

                System.out.println("Product restocked successfully!!!");
            }
            else {
                System.out.println("Product is available already");
            }
        }
        else {
            System.out.println("Product not found.");
        }
    }

}