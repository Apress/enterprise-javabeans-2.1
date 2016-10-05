package ejb.part;

public class TsPartDetails implements java.io.Serializable {
    
    String partNumber;
    String partDescription;
    String supplierName;
    float  price;
    
    private long timestamp = 0;

    TsPartDetails() {
    }
    
    public boolean isOutDated(TsPartDetails details) {
        return details.timestamp < this.timestamp;
    }
    
    void updateTimestamp(javax.ejb.EntityContext ctx, long tt) {
        if(ctx == null) {
            throw new IllegalStateException("ctx == null not allowed");
        }
        timestamp = tt;
    }

    public String getPartNumber() {
        return partNumber;
    }
    
    public void setPartDescription(String desc) {
        partDescription = desc;
    }
    
    public String getPartDescritption() {
        return partDescription;
    }
    
    public void setSupplierName(String name) {
        supplierName = name;
    }
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setPrice(float p) {
        price = p;
    }
    
    public float getPrice() {
        return price;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("[[TsPartDetails]");
        sb.append("partNumber=").append(trim(partNumber)).append(";");
        sb.append("partDescription=").append(trim(partDescription)).append(";");
        sb.append("supplierName=").append(trim(supplierName)).append(";");
        sb.append("price=").append(price).append(";");
        sb.append("timestamp=").append(timestamp).append(";");
        sb.append("]");
        return sb.toString();
    }
    
    private String trim(String s) {
        return s != null ? s.trim() : null;
    }
    
}