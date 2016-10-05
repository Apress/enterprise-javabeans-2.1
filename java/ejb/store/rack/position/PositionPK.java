package ejb.store.rack.position;


public class PositionPK implements java.io.Serializable {
    
    public Integer rackId;
    public Integer row;
    public Integer column;

    public PositionPK() {
    }
    
    public PositionPK(Integer rid, Integer r, Integer c) {
        if(rid == null)
            throw new IllegalArgumentException("rid");
        if(r == null)
            throw new IllegalArgumentException("r");
        if(c == null)
            throw new IllegalArgumentException("c");

        this.rackId  = rid;
        this.row     = r;
        this.column  = c;
    }
    
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(!(o instanceof PositionPK)) {
            return false;
        }
        PositionPK pk = (PositionPK)o;
        return this.rackId.intValue()  == pk.rackId.intValue()  &&
               this.row.intValue()     == pk.row.intValue()     &&
               this.column.intValue()  == pk.column.intValue();
    }
    
    
    public int hashCode() {
        return this.rackId.intValue() ^
               this.row.intValue() ^
               this.column.intValue();
    }


    public String toString() {
        return "PositionPK[rackId:" + this.rackId +
                            ";row:" + this.row +
                         ";column:" + this.column + "]";
    }

}
