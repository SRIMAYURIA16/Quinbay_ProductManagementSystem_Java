public class PurchaseThread implements Runnable {
    private int productId;
    private int purchasingStocks;
    private Purchase purchase;

    public PurchaseThread(int productId, int purchasingStocks, Purchase purchase) {
        this.productId = productId;
        this.purchasingStocks = purchasingStocks;
        this.purchase = purchase;
    }

    @Override
    public void run() {
        purchaseProduct(productId, purchasingStocks);
    }

    private void purchaseProduct(int idToPurchase, int purchasingStocks) {
        ProductManagement management = new ProductManagement();
        Product product = management.getProduct(idToPurchase);
        if (product != null && !product.isDeleted()) {
            if (product.getStocksAvail() >= purchasingStocks) {
                product.setStocksAvail(product.getStocksAvail() - purchasingStocks);
                management.saveUpdate();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int purchaseId = purchase.getNextPurchaseId();
                String purchaseCode = "PUR" + purchaseId;
                double actualAmount = product.getProductPrice() * purchasingStocks;
                double totalAmount = actualAmount * purchasingStocks;
                PurchaseEntity purchaseEntity = new PurchaseEntity(purchaseId, purchaseCode, product.getProductName(),
                        purchasingStocks, actualAmount, totalAmount);



//                synchronized (purchase) {
                    purchase.addPurchase(purchaseEntity);
//                }

                System.out.println("Product purchased successfully!");
            }
            else {
                System.out.println("Insufficient stock available for purchase.");
            }
        }
        else {
            System.out.println("Product ID " + idToPurchase + " not found or deleted.");
        }
    }

}
