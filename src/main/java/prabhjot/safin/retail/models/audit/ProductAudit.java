package prabhjot.safin.retail.models.audit;

import java.sql.Date;

public class ProductAudit extends Audit {
    private int productId;

    public ProductAudit(String change, Date date, int objId, int productId) {
        super(change, date, objId);
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return super.toString() + ", Product Id: " + this.productId;
    }
}
