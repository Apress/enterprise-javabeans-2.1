package ejb.store;


public class Rack implements java.io.Serializable {

    private Integer id;
    private String  description;
    private Integer storeId;
    
    public Rack(Integer id, String desc, Integer storeId) {
        if(id == null || id.intValue() < 0)
            throw new IllegalArgumentException("id");
        if(desc == null)
            throw new IllegalArgumentException("desc == null!");
        if(storeId == null || storeId.intValue() < 0)
            throw new IllegalArgumentException("storeId");
        
        this.id          = id;
        this.description = desc;
        this.storeId     = storeId;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Integer getStoreId() {
        return this.storeId;
    }
    
    public String toString() {
        return "[Rack[id=" + this.id +
               ";description=" + this.description + "]";
    }

}
