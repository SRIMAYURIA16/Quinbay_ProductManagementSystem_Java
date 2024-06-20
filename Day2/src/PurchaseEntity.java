public class PurchaseEntity {
    private int purchaseID;
    private String purchaseCode;
    private String productName;
    private int purchasedStocks;
    private double actualAmount;
    private double purchasedAmount;

    PurchaseEntity(int purchaseID, String purchaseCode, String productName, int purchasedStocks,double actualAmount,double purchasedAmount) {
        this.purchaseID = purchaseID;
        this.purchaseCode = purchaseCode;
        this.productName = productName;
        this.purchasedStocks = purchasedStocks;
        this.actualAmount=actualAmount;
        this.purchasedAmount=purchasedAmount;
    }

    public int getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID = purchaseID;
    }

    public String getPurchaseCode() {
        return purchaseCode;
    }

    public void setPurchaseCode(String purchaseCode) {
        this.purchaseCode = purchaseCode;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPurchasedStocks() {
        return purchasedStocks;
    }

    public void setPurchasedStocks(int purchasedStocks) {
        this.purchasedStocks = purchasedStocks;
    }

    public double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public double getPurchasedAmount() {
        return purchasedAmount;
    }

    public void setPurchasedAmount(double purchasedAmount) {
        this.purchasedAmount = purchasedAmount;
    }

    @Override
    public String toString() {
        return purchaseID +","+ purchaseCode + ","+productName + "," +purchasedStocks +","+actualAmount+","+purchasedAmount;
    }


}
