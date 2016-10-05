package ejb.store;


public class Item implements java.io.Serializable {

    private Integer id;
    private String  description;
    private String  supplier;
    
    public Item(Integer id, String desc, String s) {
        if(id == null || id.intValue() < 0)
            throw new IllegalArgumentException("id");
        if(desc == null)
            throw new IllegalArgumentException("desc == null!");
        if(s == null)
            throw new IllegalArgumentException("s == null!");
        
        this.id = id;
        this.description = desc;
        this.supplier = s;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getSupplier() {
        return this.supplier;
    }
    
    public String toString() {
        return "[Item[id=" + this.id 
                  + ";description=" + this.description
                  + ";supplier=" + this.supplier + "]";
    }

}
