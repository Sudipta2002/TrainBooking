package ticket.booking.entities;

import java.sql.SQLOutput;
import java.util.List;

public class User {

    private String name;
    private String password;
    private String hashedPassword;
    private List<Ticket> ticketsBooked;
    private String userId;

    public User(String name, String password, String hashedPassword, List<Ticket>ticketsBooked,String userId){
        this.name=name;
        this.password=password;
        this.hashedPassword=hashedPassword;
        this.userId=userId;
        this.ticketsBooked=ticketsBooked;
    }

    public User(){}

    public String getName(){
        return this.name;
    }
    public String getPassword(){
        return this.password;
    }
    public String getHashedPassword(){
        return this.hashedPassword;
    }
    public String getUserId(){
        return this.userId;
    }
    public List<Ticket> getTicketsBooked(){
        return this.ticketsBooked;
    }

    public void printTickets(){
        for(int i=0;i<this.ticketsBooked.size();i++){
            System.out.println(this.ticketsBooked.get(i).getTicketInfo());
        }
    }

    public void setName(String name){
        this.name=name;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setHashedPassword(String hashedPassword){
        this.hashedPassword=hashedPassword;
    }
    public void setUserId(String userId){
        this.userId=userId;
    }
    public void setTicketsBooked(List<Ticket>ticketsBooked){

        this.ticketsBooked=ticketsBooked;
    }





}
