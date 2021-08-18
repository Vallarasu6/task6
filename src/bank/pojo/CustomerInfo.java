package bank.pojo;
public class CustomerInfo {

    public CustomerInfo(){}

    private int id;
    private String Name;
    private long Mobile;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String Name){
        this.Name = Name;
    }
    public String getName(){
        return this.Name;
    }
    public void setMobileNumber(long Mobile){
        this.Mobile = Mobile;
    }
    public long getMobileNumber(){ return this.Mobile; }

    @Override
    public String toString(){
        String output = this.Mobile+" "+this.Name+" "+this.id;
        return output;
    }
}