package bank.database;
import bank.interfaces.InterfaceCommon;
import bank.pojo.CustomerInfo;
import pojo_account.AccountInfo;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DbStore implements InterfaceCommon {
    static Connection con = null;
    public DbStore(){
        try {
            if(con==null) {
                con = DriverManager.getConnection("jdbc:mysql://localhost/zoho", "root", "vallaK@6");
               // System.out.println("Connection Established ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public HashMap<Long, AccountInfo> showFromAccountTableAll() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        HashMap<Long, AccountInfo> accountInfoHashMap = new HashMap<>();

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * from accountInfo");

            while (rs.next()) {
                AccountInfo pda = new AccountInfo();
                Integer customer_id = rs.getInt("customerId");
                Long accountNumber = rs.getLong("customerAccountNumber");
                String bankName = rs.getString("customerBankName");
                Long balance = rs.getLong("customerBalance");

              pda.setId(customer_id);
              pda.setAccountNumber(accountNumber);
              pda.setBankName(bankName);
              pda.setBalance(balance);
              accountInfoHashMap.put(accountNumber,pda);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        rs.close();
        st.close();

        return accountInfoHashMap;

    }

    public HashMap<Long, AccountInfo> showFromAccountTable() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        HashMap<Long, AccountInfo> accountHashMap = new HashMap<>();

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * from accountInfo where status = 1 ");

            while (rs.next()) {
                AccountInfo pda = new AccountInfo();
                Integer customer_id = rs.getInt("customerId");
                Long accNumber = rs.getLong("customerAccountNumber");
                String bank = rs.getString("customerBankName");
                Long balance = rs.getLong("customerBalance");

                pda.setId(customer_id);
                pda.setAccountNumber(accNumber);
                pda.setBankName(bank);
                pda.setBalance(balance);
                accountHashMap.put(accNumber,pda);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

            rs.close();
            st.close();

        return accountHashMap;

    }
    public int[] insertToCustomerTable(ArrayList<CustomerInfo> arrayList) throws SQLException {

        con.setAutoCommit(false);
        PreparedStatement st = null;
        int size = arrayList.size();
        int cusIdArray[] = new int[size];
        int[] batchResults = new int[0];
        try {
            String sql = "INSERT INTO customerInfo (customerName,customerMobile,customerStatus) VALUES (?, ?, ?)";
            st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);


            for (int i = 0; i < size; i++) {
                CustomerInfo passData = arrayList.get(i);
                String name = passData.getName();
                long mobile = passData.getMobileNumber();
                st.setString(1, name);
                st.setLong(2, mobile);
                st.setInt(3,1);
                st.addBatch();

            }
            int i = 0;

            batchResults = null;

            batchResults = st.executeBatch();
           // System.out.println("Batch insert successful. Committing.");
            con.commit();
            ResultSet generatedKeysResultSet = st.getGeneratedKeys();

            while (generatedKeysResultSet.next()) {
                cusIdArray[i] = generatedKeysResultSet.getInt(1);
                //System.out.println("Your" + i + "id is " + generatedKeysResultSet.getInt(1));
                i++;
            }

        } catch (BatchUpdateException e) {
            System.out.println("Error message: " + e.getMessage());
            batchResults = e.getUpdateCounts();
            System.out.println("Rolling back batch insertion");
            con.rollback();


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Return value from inserting batch 1: " + Arrays.toString(batchResults));
        st.close();
        return cusIdArray;
    }
    public int[] insertToAccountTable(ArrayList<AccountInfo> accountInfo) throws SQLException {
        con.setAutoCommit(false);
        PreparedStatement st = null;
        int[] batchResults = new int[0];

        int size= accountInfo.size();
        int id;
        try {
            String sql = "INSERT INTO accountInfo (customerId,customerAccountNumber,customerBankName,customerBalance,status) VALUES ( ?, ?, ?, ?, ?)";
            st = con.prepareStatement(sql);
            for (int i = 0; i < size; i++) {
                AccountInfo passDataAccount = accountInfo.get(i);
                id = passDataAccount.getId();
                long accountNumber = passDataAccount.getAccountNumber();
                String bankName = passDataAccount.getBankName();
                long balance = passDataAccount.getBalance();
                st.setInt(1, id);
                st.setLong(2, accountNumber);
                st.setString(3, bankName);
                st.setLong(4, balance);
                st.setInt(5,1);
                st.addBatch();
            }
            batchResults = null;

            batchResults = st.executeBatch();
            //System.out.println("Batch insert successful. Committing.");
            con.commit();

//          int ar[] =  st.executeBatch();
//            for(int count : ar) {
//              //  if(ar[i]!=1)
//                System.out.println("There is an error in account table at id entered" +count);
//            }

            }catch (BatchUpdateException e) {
            //System.out.println("Error message: " + e.getMessage());
            batchResults = e.getUpdateCounts();
            System.out.println("Rolling back batch insertion");
            con.rollback();
        }
        catch (Exception e){
            System.out.println("toString(): " + e.toString());
            System.out.println("getMessage(): " + e.getMessage());
       }
        System.out.println("Return value from inserting batch 2: " + Arrays.toString(batchResults));
        st.close();
        return batchResults;

    }
    public void deleteFromAllTables(int id) throws SQLException {
        PreparedStatement st = null;
        PreparedStatement st1 = null;
            int cust_id = id;
        try {
            String sql1 = "DELETE FROM accountInfo WHERE customerId = ?";
            st1 = con.prepareStatement(sql1);
            st1.setInt(1,cust_id);
            st1.executeUpdate();
            String sql = "DELETE FROM customerInfo WHERE customerId = ?";
            st = con.prepareStatement(sql);
            st.setInt(1,cust_id);
            st.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        st.close();
        st1.close();
    }
    public void deleteFromAccountTable(int id,long accountNumber) throws SQLException {
        PreparedStatement st = null;
        int cust_id = id;
        long acc_Number = accountNumber;
        try {

            String sql = "UPDATE accountInfo SET status = 0 WHERE customerId = ? AND customerAccountNumber = ?";
            st = con.prepareStatement(sql);
//            st.setBoolean(1,0);
            st.setInt(1,cust_id);
            st.setLong(2,acc_Number);
            st.executeUpdate();
            System.out.println("Ststus changes in acount");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        st.close();

    }




    public void deleteFromCustomerTable(int id) throws SQLException {
     con.setAutoCommit(false);
        PreparedStatement st = null;

        int cust_id = id;
        try {
            String sql = "DELETE FROM customerInfo WHERE customerId = ?";
            st = con.prepareStatement(sql);
            st.setInt(1,cust_id);
            st.executeUpdate();
          con.commit();
           // System.out.println("db layer dalte"+cust_id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        st.close();

    }


    public void updateBalance(long balance,int cus_id,long acc_number) throws SQLException {
        PreparedStatement st = null;
        try {
       long customerBalance = balance;
        int customerId = cus_id;
        long accountNumber = acc_number;

            String sql = "UPDATE accountInfo SET customerBalance = ? WHERE customerId = ? AND customerAccountNumber = ?";
            st = con.prepareStatement(sql);
            st.setLong(1, customerBalance);
            st.setInt(2,customerId);
            st.setLong(3,accountNumber);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        st.close();
    }
    public void updateCustomerStatus(int id) throws SQLException {
        PreparedStatement st = null;
        try {
            int customerId = id;
            String sql = "UPDATE customerInfo SET customerStatus = 0 WHERE customerId = ?";
            st = con.prepareStatement(sql);
            st.setInt(1, customerId);
            st.executeUpdate();
            System.out.println("status changd from customer");

        }catch (SQLException e) {
            e.printStackTrace();
        }
        st.close();
    }
    public void updateCustomerStatusToActive(int id, long accountNumber) throws SQLException{
        PreparedStatement st = null;
        PreparedStatement st1 = null;
        try {
            int customerId = id;
            long accountnumber = accountNumber;
            String sql = "UPDATE customerInfo SET customerStatus = 1 WHERE customerId = ?";
            st = con.prepareStatement(sql);
            st.setInt(1, customerId);
            st.executeUpdate();
            String sql1 = "UPDATE accountInfo SET status = 1 WHERE customerId = ? AND customerAccountNumber = ?";
            st1 = con.prepareStatement(sql1);
            st1.setInt(1, customerId);
            st1.setLong(2,accountnumber);
            st1.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        st.close();

    }

    public void closeConnection(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}