package com.xotonic.dashboard.visitors;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ServerAddress;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.xotonic.dashboard.ExceptionForUser;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import static java.util.Arrays.asList;

/**
 * Загрузчик/регистратор числа посещений из БД MongoDB <br>
 * Порт, адрес, и БД сервера дефолтные <br>
 * Собсно, требуется рабочий mongod <br>
 * Используется 3я версия драйвера
 * <p>
 * Формат документа<br>
 * <code>{ ip : string, count : integer}</code>
 * </p>
 *
 * @author xotonic
 */
public class MongoVisitorsDataService implements VisitorsDataService {

    String MongoDBServerAddress = "localhost";
    String dbName = "test";
    int MongoDBServerPort = 27017;

    private MongoCollection connect() throws ExceptionForUser {
        System.out.println("CONNECTING TO MONGO SERVER");
        try {
            MongoClientOptions opts = MongoClientOptions
                    .builder()
                    .socketTimeout(5000)
                    .connectTimeout(3000)
                    .maxWaitTime(3000)
                    .build();
            ServerAddress addr = new ServerAddress(MongoDBServerAddress, MongoDBServerPort);
            MongoCollection collection
                    = new MongoClient(addr, opts)
                    .getDatabase(dbName)
                    .getCollection("visitors");
            System.out.println("CONNECTED SUCCESSFULLY");

            return collection;
        } catch (MongoTimeoutException e) {
            throw new ExceptionForUser("Не удалось подключиться к серверу БД");
        }
    }

    @Override
    public VisitorsData getData() throws ExceptionForUser {
        VisitorsData data = new VisitorsData();
        data.total = -1;
        data.unique = -1;
        MongoCollection<Document> collection
                = connect();
        System.out.println("AGRREGATING");

        AggregateIterable<Document> sum = collection.aggregate(asList(
                new Document("$group",
                        new Document("_id", null)
                        .append("total",
                                new Document("$sum", "$count"))
                        .append("unique",
                                new Document("$sum", 1)))));
        if (sum.first() != null) {
            data.total = sum.first().getInteger("total");
            data.unique = sum.first().getInteger("unique");
        }

        return data;
    }

    @Override
    public void registerIP(String ip) throws ExceptionForUser {
        try {
            MongoCollection col = connect();
            System.out.println("UPDATING IP COLLECTION");

            col.updateOne(
                    eq("ip", ip),
                    inc("count", 1),
                    new UpdateOptions().upsert(true));
        } catch (MongoTimeoutException e) {
            throw new ExceptionForUser("Не удалось подключиться к серверу БД");
        }
    }

}
