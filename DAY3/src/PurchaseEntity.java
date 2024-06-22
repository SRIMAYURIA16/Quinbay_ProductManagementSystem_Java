class PurchaseEntity {
    private int purchaseID;
    private String purchaseCode;
    private String productName;
    private int purchasedStocks;
    private double actualAmount;
    private double purchasedAmount;

    public PurchaseEntity(String purchaseCode, String productName, int purchasedStocks, double actualAmount, double purchasedAmount) {
        this.purchaseID = purchaseID;
        this.purchaseCode = purchaseCode;
        this.productName = productName;
        this.purchasedStocks = purchasedStocks;
        this.actualAmount = actualAmount;
        this.purchasedAmount = purchasedAmount;
    }

    public int getPurchaseID() {
        return purchaseID;
    }

    public String getPurchaseCode() {
        return purchaseCode;
    }

    public String getProductName() {
        return productName;
    }

    public int getPurchasedStocks() {
        return purchasedStocks;
    }

    public double getActualAmount() {
        return actualAmount;
    }
    public void setPurchasedStocks(int stocks){
        this.purchasedStocks=stocks;
    }
    public void setPurchasedAmount(double price){
        this.purchasedAmount=price;
    }

    public double getPurchasedAmount() {
        return purchasedAmount;
    }

    @Override
    public String toString() {
        return purchaseID + "," + purchaseCode + "," + productName + "," + purchasedStocks + "," + actualAmount + "," + purchasedAmount;
    }
}
