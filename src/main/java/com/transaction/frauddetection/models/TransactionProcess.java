package com.transaction.frauddetection.models;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionProcess {

    @CsvBindByPosition(position = 0)
    private String timestamp;
    @CsvBindByPosition(position = 1)
    private String amount;
    @CsvBindByPosition(position = 2)
    private String userId;
    @CsvBindByPosition(position = 3)
    private String serviceId;


}
