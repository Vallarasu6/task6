package bank.main;
import bank.logic.LogicLayer;
import bank.object.ObjectHandler;
import bank.pojo.CustomerInfo;
import hashMap.HashMapHandler;
import pojo_account.AccountInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TestRunner {
    static Scanner scan = new Scanner(System.in);
    public static void main(String args[]) throws Exception {

        int j=1;
        boolean flag=false;
        while (j==1==true) {
            System.out.println("Enter your choice\n1.Create Account\n2.Existing User\n3.Display\n4.Delete an id\n5.Transaction\n6.Exit");
            int choice = scan.nextInt();

            switch (choice) {
                case 1: {
                    flag=false;
                    ArrayList<CustomerInfo> arrList = new ArrayList<>();
                    ArrayList<AccountInfo> arrayList = new ArrayList<>();
                    System.out.println("Enter the number of users");
                        int count = scan.nextInt();
                        for (int i = 0; i < count; i++) {
                            scan.nextLine();
                            System.out.println("Enter your name");
                            String name = scan.nextLine();
                            System.out.println("Enter your mobile number");
                            long mobile = scan.nextLong();
                            scan.nextLine();
                            System.out.println("Enter your Bank Name");
                            String bank = scan.nextLine();
                            System.out.println("Enter your Account number");
                            long accountNumber = scan.nextLong();
                            System.out.println("Enter your Balance");
                            long balance = scan.nextLong();
                            CustomerInfo customerInfo = ObjectHandler.customerObject(name,mobile);
                            arrList.add(customerInfo);
                            AccountInfo accountInfo = ObjectHandler.accountObject(bank,accountNumber,balance);
                            arrayList.add(accountInfo);
                        }
                        LogicLayer ll = new LogicLayer();
                        ll.addNewCustomers(arrList, arrayList);
                        break;

                }
                case 2:{
                    flag=false;
                    LogicLayer ll = new LogicLayer();
                    ll.showData();
                    ll.showDataAll();
                    ArrayList<AccountInfo> arrayList = new ArrayList<>();
                    AccountInfo accountInfo  =new AccountInfo();
                    System.out.println("Enter the number of users");
                    int count = scan.nextInt();
                    for (int i = 0; i < count; i++) {
                        System.out.println("Enter your id");
                        int customerId = scan.nextInt();
                        scan.nextLine();
                       // HashMap<Integer, AccountInfo> accountInfoHashMap =ll.storeAccountDataAll();
//                        HashMap<Long, AccountInfo> accountInfoHashMap = HashMapHandler.INSTANCE.allAcccountData(customerId);
                      // System.out.println(accountInfoHashMap);
                        //if(HashMapHandler.INSTANCE.dbHashMapAll.containsKey(customerId)) {
                        if(ll.checkExistId(customerId)){
                           // if (HashMapHandler.INSTANCE.dbHashMap.containsKey(customerId)) {
                            if(ll.chechIdActive(customerId)){
                                System.out.println("1.Do u want to create a new account\n2.So u want to activate Exist account\n3.No");
                                int option = scan.nextInt();
                                switch (option) {
                                    case 1: {
                                        System.out.println("Enter your Bank Name");
                                        String bank = scan.nextLine();
                                        System.out.println("Enter your Account number");
                                        long accountNumber = scan.nextLong();
                                        System.out.println("Enter your Balance");
                                        long balance = scan.nextLong();
                                        accountInfo = ObjectHandler.accountObject(customerId, bank, accountNumber, balance);
                                        arrayList.add(accountInfo);
                                    }
                                    case 2:{
                                        System.out.println("Enter your Existing Account number");
                                        long account_Number = scan.nextLong();
                                        // System.out.println(accountInfoHashMap.get(customerId));
//                                        if(accountInfoHashMap.containsKey(account_Number)) {
                                        if(ll.checkAccountNumberExist(customerId,account_Number)){
                                            ll.updateCustomerStatusActive(customerId, account_Number);
                                            ll.showData();
                                        }else{
                                            System.out.println("You have entered the wrong account number");
                                        }
                                        break;
                                    }
                                    case 3:{
                                        break;
                                    }
                                }
                            } else {
                                System.out.println("Your id not active\nDo you want to activate your account?\n1.Yes and I want to activate Existing account\n" +
                                        "2.Yes and I wanna create new account\n3.No\n");
                                int option = scan.nextInt();
                                switch (option) {
                                    case 1: {
                                        System.out.println("Enter your Existing Account number");
                                        long account_Number = scan.nextLong();
                                       // System.out.println(accountInfoHashMap.get(customerId));
//                                        if(accountInfoHashMap.containsKey(account_Number)) {
                                        if(ll.checkAccountNumberExist(customerId,account_Number)){
                                            ll.updateCustomerStatusActive(customerId, account_Number);
                                            ll.showData();
                                        }else{
                                            System.out.println("You have entered the wrong account number");
                                        }

                                           break;

                                    }
                                    case 2: {
                                        scan.nextLine();
                                        System.out.println("Enter your Bank Name");
                                        String bank = scan.nextLine();
                                        System.out.println("Enter your Account number");
                                        long accountNumber = scan.nextLong();
                                        System.out.println("Enter your Balance");
                                        long balance = scan.nextLong();
                                        accountInfo = ObjectHandler.accountObject(customerId, bank, accountNumber, balance);
                                        arrayList.add(accountInfo);
                                        break;
                                    }
                                    case 3: {

                                        break;
                                    }
                                    default:
                                        System.out.println("Enter the valid choice");
                                }
                                break;
                            }
                        }else{
                            System.out.println("You are not a existing customer.Kindly register as a new");

                        }

                    }
                    ll.accountInsert(arrayList);
                    break;
                }
                case 3: {
                    int cusId=enterId();
                    if(!flag) {
                        LogicLayer ll = new LogicLayer();
                        ll.showData();
                        flag = true;
                    }
                    System.out.println(show(cusId));
                    break;
                }
                case 4:{
                    flag=false;
                    System.out.println("In which table you want to delete\nEnter your choice\n1.Delete your account completely\n2.Delete particular bank account");
                    int option = scan.nextInt();
                    LogicLayer ll = new LogicLayer();
                    ll.showData();
                    switch (option){
                        case 1:{
                            System.out.println("Enter the id You want to delete");
                            int delId = scan.nextInt();
                            ll.deleteData(delId);
                            System.out.println("The ID deleted");
                            break;
                        }
                        case 2:{
                            System.out.println("Enter the id You want to delete");
                            int delId = scan.nextInt();
                            System.out.println("Enter the account number of that id");
                            int accountNumber= scan.nextInt();
                            ll.deleteAccountData(delId,accountNumber);
                            System.out.println("The ID deleted");
                            break;
                        }
                        case 3:{
                            break;
                        }
                        default:
                            System.out.println("Enter the valid choice");
                    }
                    break;
                }
                case 5:{
                    flag = false;
                    System.out.println("\nEnter your choice\n1.Deposit amount\n2.Withdrawal amount");
                    int option = scan.nextInt();
                    LogicLayer ll = new LogicLayer();
                    ll.showData();
                    switch (option){
                        case 1:{
                            System.out.println("Enter the id You want to deposit");
                            int balId = scan.nextInt();
                            System.out.println("Enter the account number");
                            long balAccNumber = scan.nextInt();
                            System.out.println("Enter the amount to deposit");
                            long balance = scan.nextInt();
                            ll.balancDepositeData(balId,balAccNumber,balance);
                            break;
                        }
                        case 2:{
                            System.out.println("Enter the id You want to Withdrawal");
                            int customerId = scan.nextInt();
                            System.out.println("Enter the account number");
                            long accountNumber = scan.nextInt();
                            System.out.println("Enter the amount to withdrawal");
                            long balance = scan.nextInt();
                            ll.balanceWithdrawalData(customerId,accountNumber,balance);
                            break;
                        }
                        case 3:{
                            break;
                        }
                        default:
                            System.out.println("Enter the valid choice");
                    }
                    break;
                }
                case 6: {
                    j=0;
                    scan.close();
                    LogicLayer ll = new LogicLayer();
                    ll.closeAll();
                    break;
                }

                default:
                    System.out.println("Kindly enter the valid choice");
            }
        }
    }
  public static int enterId(){
      System.out.println("Enter the id you want to see");
      int id = scan.nextInt();
        return id;
  }
public static String show(int id){
    String s = "";
    HashMap<Long, AccountInfo> hashMap = HashMapHandler.INSTANCE.output(id);
    if(hashMap!=null){
        for (Long i : hashMap.keySet()) {

            s =s+ "The Balance is " + hashMap.get(i).getBalance() + " " + "in " + hashMap.get(i).getBankName()+"\n";
        }

        System.out.println("");
        System.out.println("Data fetched Successfully\n");
    }

    return s;
}

}
