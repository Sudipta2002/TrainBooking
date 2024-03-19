package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService(User user) throws IOException {
        this.user = user;
        userList=loadUsers();
    }

    public UserBookingService() throws IOException {
        userList=loadUsers();
    }

    private List<User> loadUsers() throws IOException{
        File users = new File(USERS_PATH);
        return objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }
    private void saveUserListToFile() throws IOException{
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile,userList);
    }
    // json --> Object(User) --> Deserialize
    // Object (User) --> json --> Serialize
    public void fetchBooking(){
        user.printTickets();
    }
    public Boolean cancelBooking(String ticketId) throws IOException {
        Optional<Ticket> foundTicket=user.getTicketsBooked().stream().filter(ticket -> {
            return ticket.getTicketId().equals(ticketId);
        }).findFirst();
        if(foundTicket.isPresent()){
            user.getTicketsBooked().remove(foundTicket.get());
            saveUserListToFile();
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public List<Train> getTrains(String source, String destination) throws IOException {
        TrainService trainService = new TrainService();
        return trainService.searchTrains(source,destination);
    }

    public List<List<Integer>> fetchSeats(Train train){

        return train.getSeats();
    }
    public Boolean bookTrainSeat(Train train,int row, int col) throws IOException {
        TrainService trainService = new TrainService();
        return trainService.bookSeats(train,row,col);
    }
}
