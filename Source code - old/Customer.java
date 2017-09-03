/**
 * Created by Yuri Khechoyan on 1/1/2017.
 */
public class Customer {
    //Variables
    String firstname;
    String lastname;
    String mobileNumber;
    int place;

    public Customer(){
        //DEFAULT CONSTRUCTOR
        firstname = "unknown";
        lastname = "unknown";
        mobileNumber = "unknown";
        place = -1;
    }

    protected Customer(String name,String last_name, String MobileNumber){
        //constructor for the customer
        firstname = name;
        lastname = last_name;
        mobileNumber = MobileNumber;

    }

//SETTERS AND GETTERS

    public void SetName(String name){
        firstname = name;
    }
    public void SetLastName(String last_name){
        lastname = last_name;
    }
    public void SetNumber(String MobileNumber){
        mobileNumber = MobileNumber;
    }
    public String GetName(){
        return firstname;
    }
    public String GetLastName(){
        return lastname;
    }
    public String GetNumber(){
        return mobileNumber;
    }
}
