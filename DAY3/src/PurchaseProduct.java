import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PurchaseProduct {
    private List<PurchaseEntity> purchaseList = new ArrayList<>();
    private final String DATABASE_NAME = "Training";
    private final String COLLECTION_NAME = "Product";
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> productCollection;

    private final String JDBC_URL = "jdbc:postgresql://localhost:5432/ProductManagementSystem";
    private final String USERNAME = "srimayuriannadurai";
    private final String PASSWORD = "Skcet@143";

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public PurchaseProduct() {
        mongoDatabase = MongoClients.create("mongodb://localhost:27017").getDatabase(DATABASE_NAME);
        productCollection = mongoDatabase.getCollection(COLLECTION_NAME);
    }

//    synchronized public void addPurchase(PurchaseEntity purchase) {
//        purchaseList.add(purchase);
//    }

    private void savePurchasesToPostgreSQL(Map<Integer,PurchaseEntity> purchaseList) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            connection.setAutoCommit(false);
            int totalProducts = purchaseList.size();
            double totalAmount = 0;
            for (PurchaseEntity purchase: purchaseList.values()){
                totalAmount+=purchase.getPurchasedAmount();
            }
            String insertOrderSql = "INSERT INTO orders (purchase_code, purchase_date, total_products, total_amount) VALUES (?, NOW(), ?, ?) RETURNING purchase_id";
            int purchaseId = -1;
            String purchaseCode = purchaseList.values().iterator().next().getPurchaseCode();
            try (PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderSql)) {
                insertOrderStatement.setString(1, purchaseCode);
                insertOrderStatement.setInt(2, totalProducts);
                insertOrderStatement.setDouble(3, totalAmount);
                try (ResultSet resultSet = insertOrderStatement.executeQuery()) {
                    if (resultSet.next()) {
                        purchaseId = resultSet.getInt(1);
                    }
                }
            }
            if (purchaseId != -1) {
                String insertDetailSql = "INSERT INTO order_details (purchase_id, product_name, quantity, price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertDetailStatement = connection.prepareStatement(insertDetailSql)) {
                    for (PurchaseEntity purchase:purchaseList.values()){
                        insertDetailStatement.setInt(1,purchaseId);
                        insertDetailStatement.setString(2,purchase.getProductName());
                        insertDetailStatement.setInt(3,purchase.getPurchasedStocks());
                        insertDetailStatement.setDouble(4,purchase.getActualAmount());
                        insertDetailStatement.addBatch();
                    }
                    insertDetailStatement.executeBatch();
                }
                connection.commit();
                updateMongoDBStocks(purchaseList);
                System.out.println("Purchase completed successfully.");
            } else {
                connection.rollback();
                System.out.println("Failed to insert order");
            }
        } catch (SQLException e) {
            System.out.println("Error saving purchase to PostgreSQL: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void updateMongoDBStocks(Map<Integer,PurchaseEntity> purchases) {
        for (PurchaseEntity purchase : purchases.values()) {
            productCollection.updateOne(
                    Filters.eq("productName", purchase.getProductName()),
                    Updates.inc("stocksAvail", -purchase.getPurchasedStocks())
            );
        }
    }

    public void displayPurchaseHistory() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT orders.purchase_id, orders.purchase_code, orders.purchase_date, orders.total_products, orders.total_amount, " +
                    "order_details.product_name, order_details.quantity, order_details.price " +
                    "FROM orders JOIN order_details ON orders.purchase_id = order_details.purchase_id";
            try (Statement stmt = connection.createStatement()) {
                Map<Integer, Purchase> purchaseMap = new HashMap<>();

                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    int purchaseId = rs.getInt("purchase_id");
                    String purchaseCode = rs.getString("purchase_code");
                    Timestamp purchaseDate = rs.getTimestamp("purchase_date");
                    int totalProducts = rs.getInt("total_products");
                    double totalAmount = rs.getDouble("total_amount");
                    String productName = rs.getString("product_name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");

                    Purchase purchase = purchaseMap.getOrDefault(purchaseId, new Purchase(purchaseId, purchaseCode, purchaseDate, totalProducts, totalAmount));
                    purchase.addProduct(new ProductDetail(productName, quantity, price));

                    purchaseMap.put(purchaseId, purchase);
                }

                for (Purchase purchase : purchaseMap.values()) {

                    System.out.println("Purchase ID: " + purchase.getPurchaseId());
                    System.out.println("Purchase Code: " + purchase.getPurchaseCode());
                    System.out.println("Purchase Date: " + purchase.getPurchaseDate());
                    System.out.println("Total Products: " + purchase.getTotalProducts());
                    System.out.println("Total Amount: " + purchase.getTotalAmount());
                    System.out.println("Products:");
                    for (ProductDetail product : purchase.getProducts()) {
                        System.out.println("  Product Name: " + product.getProductName());
                        System.out.println("  Quantity: " + product.getQuantity());
                        System.out.println("  Price: " + product.getPrice());
                    }
                    System.out.println("===============================");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error displaying purchase history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void purchaseProductFromMongoDB(Scanner sc) {
        Map<Integer,PurchaseEntity> purchaseList=new HashMap<>();
        String purchaseCode = "PUR" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Map<Integer,Integer> tempStocks=new HashMap<>();
        while (true) {
            System.out.println("Enter Product ID:");
            int productId = sc.nextInt();
            System.out.println("Enter Number of Stocks to Purchase:");
            int purchasingStocks = sc.nextInt();
            sc.nextLine();

            Document productDocument = productCollection.find(Filters.eq("productId", productId)).first();

            if (productDocument != null && !productDocument.getBoolean("deletionStatus")) {

                int availableStocks = productDocument.getInteger("stocksAvail");
                int stocks=availableStocks-tempStocks.getOrDefault(productDocument.getInteger("productId"),0);
                if (availableStocks >= purchasingStocks && stocks>=purchasingStocks) {
                    tempStocks.put(productDocument.getInteger("productId"),purchasingStocks);
                    String productName = productDocument.getString("productName");
                    double productPrice = productDocument.getDouble("productPrice");

                    double totalAmount = productPrice * purchasingStocks;
                    PurchaseEntity purchaseEntity = new PurchaseEntity(purchaseCode, productName, purchasingStocks, productPrice, totalAmount);
                    if(purchaseList.containsKey(productDocument.getInteger("productId"))){
                        PurchaseEntity temp=purchaseList.get(productDocument.getInteger("productId"));
                        temp.setPurchasedStocks(temp.getPurchasedStocks()+purchasingStocks);
                        temp.setPurchasedAmount(temp.getPurchasedAmount()+productPrice);
                    }
                    else{
                        purchaseList.put(productDocument.getInteger("productId"),purchaseEntity);
                    }

                    System.out.println("Do you want to add more products? (yes/no)");
                    String response = sc.nextLine().trim().toLowerCase();
                    if (response.equals("no")) {
                        System.out.println("Confirm order? (yes/no)");
                        String confirmResponse = sc.nextLine().trim().toLowerCase();
                        if (confirmResponse.equals("yes")) {
                            executorService.submit(() -> savePurchasesToPostgreSQL(purchaseList));
                            break;
                        } else {
                            System.out.println("Order cancelled.");
                            break;
                        }
                    }
                } else {
                    System.out.println("Insufficient stocks available. Available stocks: " + stocks);
                }
            } else {
                System.out.println("Product ID " + productId + " not found or deleted.");
            }
        }
    }


    class Purchase {
        private int purchaseId;
        private String purchaseCode;
        private Timestamp purchaseDate;
        private int totalProducts;
        private double totalAmount;
        private List<ProductDetail> products;

        public Purchase(int purchaseId, String purchaseCode, Timestamp purchaseDate, int totalProducts, double totalAmount) {
            this.purchaseId = purchaseId;
            this.purchaseCode = purchaseCode;
            this.purchaseDate = purchaseDate;
            this.totalProducts = totalProducts;
            this.totalAmount = totalAmount;
            this.products = new ArrayList<>();
        }

        public int getPurchaseId() {
            return purchaseId;
        }

        public String getPurchaseCode() {
            return purchaseCode;
        }

        public Timestamp getPurchaseDate() {
            return purchaseDate;
        }

        public int getTotalProducts() {
            return totalProducts;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public List<ProductDetail> getProducts() {
            return products;
        }

        public void addProduct(ProductDetail product) {
            this.products.add(product);
        }
    }

    class ProductDetail {
        private String productName;
        private int quantity;
        private double price;

        public ProductDetail(String productName, int quantity, double price) {
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getProductName() {
            return productName;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }
}
