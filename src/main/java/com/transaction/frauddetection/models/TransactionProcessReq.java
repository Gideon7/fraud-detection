package com.transaction.frauddetection.models;

import java.util.List;

public record TransactionProcessReq(List<TransactionProcess> transactions) {
}
