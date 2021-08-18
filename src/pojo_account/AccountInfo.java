package pojo_account;
public class AccountInfo {

    public AccountInfo(){}

    private int id;
    private String BankName;
    private long AccountNumber;
    private long Balance;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setBankName(String BankName){
        this.BankName = BankName;
    }
    public String getBankName(){
        return this.BankName;
    }
    public void setAccountNumber(long AccountNumber){
        this.AccountNumber = AccountNumber;
    }
    public long getAccountNumber(){
        return this.AccountNumber;
    }
    public void setBalance(long Balance){
        this.Balance = Balance;
    }
    public long getBalance(){ return this.Balance; }

    @Override
    public String toString(){
        String output = this.Balance+"  "+this.id;
        return output;
    }
}
