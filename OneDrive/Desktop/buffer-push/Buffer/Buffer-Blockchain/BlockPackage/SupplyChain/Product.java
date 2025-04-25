package BlockPackage.SupplyChain;
import java.util.Date;

public class Product {
    private String productId;
    private String location;
    private String status;
    private Date timestamp;

    public Product(String productId, String location, String status) {
        this.productId = productId;
        this.location = location;
        this.status = status;
        this.timestamp = new Date();
    }

    public String getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "[ProductID: " + productId +
               ", Location: " + location +
               ", Status: " + status +
               ", Timestamp: " + timestamp + "]";
    }
}