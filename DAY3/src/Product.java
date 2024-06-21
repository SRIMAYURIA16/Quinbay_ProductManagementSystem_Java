public class Product {
    private int productId;
    private String productCode;
    private String productName;
    private double productPrice;
    private int stocksAvail;
    private boolean deletionStatus;
    private Category category;

    public Product(int productId, String productCode, String productName, double productPrice, int stocksAvail, boolean deletionStatus, Category category) {
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.stocksAvail = stocksAvail;
        this.deletionStatus = deletionStatus;
        this.category = category;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getStocksAvail() {
        return stocksAvail;
    }

    public void setStocksAvail(int stocksAvail) {
        this.stocksAvail = stocksAvail;
    }

    public boolean isDeleted() {
        return deletionStatus;
    }

    public void setDeletionStatus(boolean deletionStatus) {
        this.deletionStatus = deletionStatus;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static class Category {   //static because i dont need to create instance for Product to create instance for category .. category will run independently
        private int id;
        private String name;

        public Category(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}