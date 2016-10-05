package ejb.part;

public class PartDetails implements java.io.Serializable {
    
    String partNumber;
    String partDescription;
    String supplierName;
    float  price;

    public PartDetails() {
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
    
}