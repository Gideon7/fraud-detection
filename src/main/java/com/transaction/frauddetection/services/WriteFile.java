package com.transaction.frauddetection.services;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteFile {

    public static void main(String[] args){
        String filePath = "dataset.csv";
        writeFile(filePath);
        System.out.println("File written successfully");
    }

    public static void writeFile(String filePath){

        File file = new File(filePath);

        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            //"timestamp": 1617906000, "amount": 150.00, "userID": "user1", "serviceID": "serviceA" }, {
            //"timestamp": 1617906060, "amount": 4500.00, "userID": "user2", "serviceID": "serviceB" }, {
            //"timestamp": 1617906120, "amount": 75.00, "userID": "user1", "serviceID": "serviceC" }, {
            //"timestamp": 1617906180, "amount": 3000.00, "userID": "user3", "serviceID": "serviceA" }, {
            //"timestamp": 1617906240, "amount": 200.00, "userID": "user1", "serviceID": "serviceB" }, {
            //"timestamp": 1617906300, "amount": 4800.00, "userID": "user2", "serviceID": "serviceC" }, {
            //"timestamp": 1617906360, "amount": 100.00, "userID": "user4", "serviceID": "serviceA" }, {
            //"timestamp": 1617906420, "amount": 4900.00, "userID": "user3", "serviceID": "serviceB" }, {
            //"timestamp": 1617906480, "amount": 120.00, "userID": "user1", "serviceID": "serviceD" }, {
            //"timestamp": 1617906540, "amount": 5000.00, "userID": "user3", "serviceID": "serviceC"

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] { "Timestamp", "Amount", "UserId", "ServiceId"});
            data.add(new String[] { "1617906000", "150.00", "user1", "serviceA" });
            data.add(new String[] { "1617906060", "4500.00", "user2","serviceB"});
            writer.writeAll(data);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
