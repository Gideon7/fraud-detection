package com.transaction.frauddetection.services;

import com.opencsv.bean.CsvToBeanBuilder;
import com.transaction.frauddetection.models.TransactionProcess;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TransactionProcessService {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Starting Fraud Check");
        processTransaction();
    }
    public static void processTransaction() throws FileNotFoundException {
        System.out.println("Checking Fraudulent Transaction Processes");

        //read transactions from file and convert to object
        var transactions = new CsvToBeanBuilder<TransactionProcess>(new FileReader("dataset.csv"))
                .withType(TransactionProcess.class)
                .build()
                .parse();
        detectFraud(transactions);
    }

    public static void detectFraud(List<TransactionProcess> transactions) {
        System.out.println("Detecting Fraud");

        //Detecting For Services
        for (int i = 0; i < transactions.size(); i++){
            var timestamp = Long.parseLong(transactions.get(i).getTimestamp()) * 1000;
            var userID = transactions.get(i).getUserId();
            var timeInFive = TransactionProcessService.getFiveMinutesAheadTimestamp(timestamp);

            if (TransactionProcessService.checkServices(transactions, userID, i, timeInFive )) {
                System.out.println("FRAUD DETECTED! FRAUD DETECTED!! FRAUD DETECTED!!! for "+userID);
            }
        }

        //Detecting For Amount
        for (int i = 0; i < transactions.size(); i++){

            var timestamp = Long.parseLong(transactions.get(i).getTimestamp()) * 1000;
            var userID = transactions.get(i).getUserId();
            var time = getADayAgoTimestamp(timestamp);

            System.out.println("24hrs: "+ time);

            if (TransactionProcessService.checkTransactionAmount(transactions, userID, i, time)) {
                System.out.println("FRAUD DETECTED! FRAUD DETECTED!! FRAUD DETECTED!!! for "+ userID);
            }
        }

        //Detecting For Ping Pong
        for (int i = 0; i < transactions.size(); i++){
            var timestamp = Long.parseLong(transactions.get(i).getTimestamp()) * 1000;
            var timeInTen = TransactionProcessService.getTenMinutesAheadTimestamp(timestamp);

            if (TransactionProcessService.checkPingPong(transactions, i, timeInTen)) {
                System.out.println("FRAUD DETECTED! FRAUD DETECTED!! FRAUD DETECTED!!! ");
            }
        }
    }

    private static boolean checkServices(List<TransactionProcess> transactions, String userID, int position, long timestamp){
        System.out.println("Checking Services In Short Time");

        Map<String, String> serviceMap = new HashMap<>();

        //starting at next service
        var check = 1;

        for (int i = position + 1; i < transactions.size(); i++){
            var tranTime = Long.parseLong(transactions.get(i).getTimestamp()) * 1000;

            //checking user transaction in services within timeframe
            if (transactions.get(i).getUserId().equals(userID) && (tranTime <= timestamp) && !serviceMap.containsKey(transactions.get(i).getServiceId())){
                serviceMap.put(transactions.get(i).getServiceId(), transactions.get(i).getServiceId());
                check += 1;
            }

            //if check is more than 3, flag
            if (check > 3)
                return true;

            if (tranTime > timestamp)
                break;
        }

        return false;
    }

    private static boolean checkTransactionAmount(List<TransactionProcess> transactions, String userID, int position, long timestamp){
        System.out.println("Checking Transaction Amounts "+ userID);

        if (position == 0)
            return false;

        double sum = 0.0;
        var count = 0;
        for (int i = 0; i < transactions.size(); i++){
            var tranTime = Long.parseLong(transactions.get(i).getTimestamp()) * 1000;
            if (transactions.get(i).getUserId().equals(userID) && tranTime <= timestamp){
                sum += Double.parseDouble(transactions.get(i).getAmount());
                count += 1;
            }
        }
        System.out.println("Sum: "+ sum+", Count: "+count);
        var avg = sum / count;
        System.out.println("Average: "+ avg);
        double tranAmount = Double.parseDouble(transactions.get(position).getAmount());
        System.out.println("TranAmount: "+ tranAmount);
        System.out.println("Fraud: "+ (tranAmount >= (5 * avg)));

        if ( tranAmount >= (5 * avg))
            return true;

        return false;
    }

    private static boolean checkPingPong(List<TransactionProcess> transactions, int position, long timestamp){
        System.out.println("Checking Ping Pong Transaction");

        Map<String, String> serviceMap = new HashMap<>();
        String prev = "";

        for (int i = position; i < transactions.size(); i++){
            long tranTime = Long.parseLong(transactions.get(i).getTimestamp());
            if (tranTime > timestamp)
                break;

            if (!serviceMap.containsKey(transactions.get(i).getServiceId()) && serviceMap.size() < 2){
                serviceMap.put(transactions.get(i).getServiceId(), transactions.get(i).getServiceId());
            }
            if (serviceMap.containsKey(transactions.get(i).getServiceId()) && serviceMap.size() == 2
                    && !prev.equals(transactions.get(i).getServiceId()))
                return true;

            prev = transactions.get(i).getServiceId();
        }

        return false;
    }

    public static long getFiveMinutesAheadTimestamp(long timestamp) {
        System.out.println("Getting Five Minutes Ahead Timestamp");
        Date date = new Date(timestamp + TimeUnit.MINUTES.toMillis(5));
        return date.getTime();
    }

    public static long getTenMinutesAheadTimestamp(long timestamp) {
        System.out.println("Getting Ten Minutes Ahead Timestamp");
        Date date = new Date(timestamp + TimeUnit.MINUTES.toMillis(10));
        return date.getTime();
    }

    //Method to get timestamp 24 hours ago
    public static long getADayAgoTimestamp(long timestamp) {
        System.out.println("Getting A Day Ago Timestamp");

        var date = new Date(timestamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(timestamp));
        cal.add(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime().getTime();
    }

}
