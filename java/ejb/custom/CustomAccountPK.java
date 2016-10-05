package ejb.custom;

public class CustomAccountPK implements java.io.Serializable {
  
    public String clientNumber;
    public String accountNumber;

    public CustomAccountPK() {
    }

    public int hashCode() {
        return clientNumber.hashCode() ^ 
               accountNumber.hashCode();
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof CustomAccountPK)) {
            return false;
        }
        CustomAccountPK pk = (CustomAccountPK)obj;
        return (clientNumber.equals(pk.clientNumber)
             && accountNumber.equals(pk.accountNumber));
    }

    public String toString() {
        return clientNumber + ":" + accountNumber;
    }
}
