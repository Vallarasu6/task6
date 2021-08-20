package bank.logic;
import bank.exception.HandledException;
import bank.interfaces.InterfaceCommon;
import bank.pojo.CustomerInfo;
import hashMap.HashMapHandler;
import pojo_account.AccountInfo;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class LogicLayer {
    private InterfaceCommon db;
    FileReader reader;

    {
        try {
            reader = new FileReader("file.properties");
            Properties properties=new Properties();
            properties.load(reader);
            String data = properties.getProperty("dbConnection");
            System.out.println(data);
            db = (InterfaceCommon)Class.forName(data).newInstance();

        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }
    public void addNewCustomers(ArrayList<CustomerInfo> customerList, ArrayList<AccountInfo> accountList){
        int key[]= new int[0];
        try {
            key = db.insertToCustomerTable(customerList);
            int size = key.length;
        AccountInfo accountinfo = new AccountInfo();
        for (int i=0;i<size;i++) {
           accountinfo = accountList.get(i);
            accountinfo.setId(key[i]);
           // System.out.println(key[i]);

        }
        int batch[] = db.insertToAccountTable(accountList);
        for(int i=0;i< batch.length;i++){
            if(batch[i]!=1){
                for(int j=0;j<size;j++){
                    deleteCustomerData(key[j]);
                    //System.out.println(key[j]);
                }
                System.out.println("No data stored, RollBacked");
                break;
            }
        }
        for (int i=0;i<size;i++) {
            HashMapHandler.INSTANCE.store(accountinfo.getAccountNumber(),accountinfo,key[i]);
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void accountInsert(ArrayList<AccountInfo> accountList)  {
        try {
            db.insertToAccountTable(accountList);
            int size = accountList.size();
            System.out.println("Logical layer account method worked");
            for (int i = 0; i < size; i++) {
                AccountInfo accountinfo = accountList.get(i);
                HashMapHandler.INSTANCE.store(accountinfo.getAccountNumber(), accountinfo, accountinfo.getId());
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void showDataAll() throws Exception {
        try {
            HashMap<Long, AccountInfo> accountInfoHashMap=db.showFromAccountTableAll();
            //System.out.println("complete account "+accountInfoHashMap);
            for(Long i : accountInfoHashMap.keySet()){
                AccountInfo  accountInfo = accountInfoHashMap.get(i);
                HashMapHandler.INSTANCE.getDbHashMapAll(accountInfo.getAccountNumber(), accountInfo, accountInfo.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public void showData() {
        try {
            HashMap<Long, AccountInfo> accountInfoHashMap=db.showFromAccountTable();
            for(Long i : accountInfoHashMap.keySet()){
                AccountInfo  accountInfo = accountInfoHashMap.get(i);
                HashMapHandler.INSTANCE.store(accountInfo.getAccountNumber(), accountInfo, accountInfo.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void closeAll() {
        db.closeConnection();
    }
    public void deleteData(int delId){
        int id = delId;
        try {
            db.deleteFromAllTables(id);
            HashMapHandler.INSTANCE.dbHashMap.remove(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteAccountData(int delId,long accountNumber) throws HandledException {
        int id = delId;
        long account_number = accountNumber;
        try{
            db.deleteFromAccountTable(id,account_number);
           // HashMapHandler.INSTANCE.dbHashMap.remove(id);
            HashMap<Long, AccountInfo> hash = HashMapHandler.INSTANCE.getAccountInfo(id);
           // if(hash.containsKey(account_number)) {
                hash.remove(account_number);
                System.out.println("Size is " + hash.size());

                if (hash.size() == 0) {
                    db.updateCustomerStatus(id);
                    HashMapHandler.INSTANCE.dbHashMap.remove(id);
                    System.out.println("Your data is completely deleted bcz of 0 accounts");
                }
           // }
//            else{
//                System.out.println("The id is already inactive");
//            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void deleteCustomerData(int delId){
        int id = delId;
        try {
            db.deleteFromCustomerTable(id);
            //System.out.println("Logic layer dalte"+id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HashMapHandler.INSTANCE.dbHashMap.remove(id);
    }


    public void balancDepositeData(int balId,long accNumber,long deposit) throws SQLException {
        HashMap<Long,AccountInfo> map = HashMapHandler.INSTANCE.output(balId);
if(map.containsKey(accNumber)) {
    long balance = map.get(accNumber).getBalance();
    balance = balance + deposit;
    db.updateBalance(balance, balId, accNumber);
    map.get(accNumber).setBalance(balance);
    System.out.println("Your new Balance is " + balance);
}else{
    System.out.println("Please enter the correct id and acc number");
}
        }
        public void balanceWithdrawalData(int balId,long accNumber,long withdraw) throws SQLException {
        HashMap<Long,AccountInfo> map = HashMapHandler.INSTANCE.output(balId);
            if(map.containsKey(accNumber)) {
                long balance = map.get(accNumber).getBalance();
                if (balance >= withdraw) {
                    balance = balance - withdraw;
                    db.updateBalance(balance, balId, accNumber);
                    map.get(accNumber).setBalance(balance);
                    System.out.println("Your remaining Balance is " + balance);

                } else {
                    System.out.println("Balance Insufficient");
                }
            }else{
                System.out.println("Please enter the correct id and acc number");

            }
    }
   public void updateCustomerStatusActive(int id, long accountNumber) throws SQLException {
        db.updateCustomerStatusToActive(id,accountNumber);
   }
   public boolean checkExistId(int customerId){
       if(HashMapHandler.INSTANCE.dbHashMapAll.containsKey(customerId)) {
           return true;
       }
       else{
           return false;
       }
    }
    public boolean chechIdActive(int customerId){
        if (HashMapHandler.INSTANCE.dbHashMap.containsKey(customerId)){
            return  true;
        }else{
            return false;
        }
    }
    public boolean checkAccountNumberExist(int customerId,long account_Number){
        HashMap<Long, AccountInfo> accountInfoHashMap = HashMapHandler.INSTANCE.allAcccountData(customerId);
        if(accountInfoHashMap.containsKey(account_Number)) {
            return true;
        }else{
            return false;
        }

        }
}