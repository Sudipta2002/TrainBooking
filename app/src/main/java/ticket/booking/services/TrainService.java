package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {
    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAINS_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";

    public TrainService() throws IOException {
        trainList=loadTrains();
    }
    private List<Train> loadTrains() throws IOException{
        File trains = new File(TRAINS_PATH);
        return objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }
    private boolean valid(Train train,String source, String destination){
        List<String> stationOrder = train.getStations();
        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destIndex = stationOrder.indexOf(destination.toLowerCase());

        return sourceIndex!=-1 && destIndex !=-1 && sourceIndex<destIndex;
    }
    public List<Train> searchTrains(String source,String destination){
        return trainList.stream().filter(train -> {
            return valid(train,source,destination);
        }).collect(Collectors.toList());
    }

    private void saveTrainListToFile() throws IOException{
        File trainsFile = new File(TRAINS_PATH);
        objectMapper.writeValue(trainsFile,trainList);
    }
    public void addTrain(Train newTrain) throws IOException {
        Optional<Train> existingTrain = trainList.stream().filter(train -> {
            return train.getTrainId().equalsIgnoreCase(newTrain.getTrainId());
        }).findFirst();

        if(existingTrain.isPresent()){
            updateTrain(newTrain);
        }else{
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }
    public void updateTrain(Train train) throws IOException{
        OptionalInt index = IntStream.range(0,trainList.size()).filter(i->{
            return trainList.get(i).getTrainId().equalsIgnoreCase(train.getTrainId());
        }).findFirst();

        if(index.isPresent()){
            trainList.set(index.getAsInt(),train);
            saveTrainListToFile();
        }else{
            addTrain(train);
        }
    }
    public Boolean bookSeats(Train train, Integer row, Integer col){
        List<List<Integer>>seatMatrix = train.getSeats();
        try {
            List<Integer>r = seatMatrix.get(row);
            if(r.get(col)==0){
                r.set(col,1);
                seatMatrix.set(row,r);
                saveTrainListToFile();
                return Boolean.TRUE;
            }else{
                System.out.println("The seat is booked by someone recently. Please book other available seats");
                return Boolean.FALSE;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
