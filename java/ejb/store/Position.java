package ejb.store;


public class Position implements java.io.Serializable {

    private Rack rack;
    private Integer  row;
    private Integer  column;
    
    public Position(Rack r, Integer row, Integer column) {
        if(r == null)
            throw new IllegalArgumentException("rack is nulL!");
        if(row == null || row.intValue() < 0)
            throw new IllegalArgumentException("row");
        if(column == null || column.intValue() < 0)
            throw new IllegalArgumentException("column");
        
        this.rack   = r;
        this.row    = row;
        this.column = column;
    }
    
    public Rack getRack() {
        return this.rack;
    }
    
    public Integer getRow() {
        return this.row;
    }
    
    public Integer getColumn() {
        return this.column;
    }

    public String toString() {
        return "Position [rack:" + this.rack +
                        "; row:" + this.row +
                     "; column:" + this.column + "]";
    }

}
