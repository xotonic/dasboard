package com.xotonic.dashboard.visitors;

import com.mongodb.MongoClient;

import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import org.bson.*;

import static java.util.Arrays.asList;

/**
 * Загрузчик/регистратор числа посещений из БД MongoDB <br>
 * Порт, адрес, и БД сервера дефолтные <br>
 * Собсно, требуется рабочий mongod <br>
 * Используется 3я версия драйвера
 * <p>Формат документа<br>
 * <code>{ ip : string, count : integer}</code>
 * </p>
 * @author xotonic
 */
public class MongoVisitorsLoader implements VisitorsLoader {

    String MongoDBServerAddress = "localhost";
    int MongoDBServerPort = 27017;
    String dbName = "test";

    @Override
    public VisitorsData getData() {
        VisitorsData data = new VisitorsData();
        data.total = -1;
        data.unique = -1;

        MongoCollection collection
                = new MongoClient(MongoDBServerAddress, MongoDBServerPort)
                .getDatabase(dbName)
                .getCollection("visitors");

        AggregateIterable<Document> sum = collection.aggregate(asList(
                new Document("$group", 
                            new Document("_id", null)
                            .append("total",
                                new Document("$sum", "$count"))
                            .append("unique",
                                new Document("$sum", 1)))));
        if (sum.first() != null)
        {
        data.total = sum.first().getInteger("total");
        data.unique = sum.first().getInteger("unique");
        }


        return data;
    }

    @Override
    public void registerIP(String ip) {
        MongoCollection col
                = new MongoClient(MongoDBServerAddress, MongoDBServerPort)
                .getDatabase(dbName)
                .getCollection("visitors");
        col.updateOne(
                eq("ip", ip),
                inc("count", 1),
                new UpdateOptions().upsert(true));
    }

}
