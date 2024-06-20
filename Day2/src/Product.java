public class Product {
    public int getProductCode;
    private int productId;
    private String productCode;
    private String productName;
    private double productPrice;
    private int stocksAvail;
    private boolean deletionStatus;

    Product(int productId,String productCode,String productName,double productPrice,int stocksAvail,boolean deletionStatus){
        this.productId=productId;
        this.productCode=productCode;
        this.productName=productName;
        this.productPrice=productPrice;
        this.stocksAvail=stocksAvail;
        this.deletionStatus=deletionStatus;
    }

    public String getProductCode(){
        return productCode;
    }
    public void setProductCode(String productCode){
        this.productCode=productCode;
    }
    public boolean isDeleted(){
        return deletionStatus;
    }
    public void setDeletionStatus(boolean deletionStatus){
        this.deletionStatus=deletionStatus;
    }
    public int getProductId(){
        return productId;
    }
    public String getProductName(){
        return productName;
    }
    public double getProductPrice(){
        return productPrice;
    }
    public int getStocksAvail(){
        return stocksAvail;
    }

    public void setProductName(String name){
        this.productName=name;
    }
    public void setProductPrice(double price){
        this.productPrice=price;
    }
    public void setStocksAvail(int stocks){
        this.stocksAvail=stocks;
    }

    @Override
    public String toString(){
        return productId+","+productCode+","+productName+","+productPrice+","+stocksAvail+","+deletionStatus;
    }


}
