package sample.appConfigurations;

public class UsersParameters {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String location;

    /**
     * -->User parameters set to be taken over and used in the required processes:
     * @param firstName First Name of the User
     * @param lastName Last Name of the User
     * @param userName Username for the User
     * @param password Password Set by User
     * @param location Location of the User
     */
    public UsersParameters(String firstName, String lastName, String userName, String password, String location){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.location = location;
    }

    //get @param firstName
    public String getFirstName() {
        return firstName;
    }

    //set @param firstName
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //get @param Last Name
    public String getLastName() {
        return lastName;
    }

    //set @param Last Name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //get @param Username
    public String getUserName() {
        return userName;
    }

    //set @param Username
    public void setUserName(String userName) {
        this.userName = userName;
    }

    //get @param Password
    public String getPassword() {
        return password;
    }

    //set @param Password
    public void setPassword(String password) {
        this.password = password;
    }

    //get @param Location
    public String getLocation() {
        return location;
    }

    //set @param Location
    public void setLocation(String location) {
        this.location = location;
    }
}
