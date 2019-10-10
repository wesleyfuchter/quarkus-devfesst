package com.wesleyfuchter.peopleservice;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class PeopleRepositoryImpl implements PeopleRepository {

    private final MongoClient mongoClient;

    @Inject
    public PeopleRepositoryImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public People save(People people) {
        Document document = new Document()
                .append("name", people.getName());
        getCollection().insertOne(document);
        people.setId(document.getObjectId("_id").toString());
        return people;
    }

    @Override
    public People update(String id, People people) {
        Optional<Document> optionalDocument = findDocumentById(id);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            document.put("name", people.getName());
            getCollection().replaceOne(eq(new ObjectId(id)), document);
        }
        return people;
    }

    @Override
    public List<People> findAll() {
        List<People> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                list.add(toPeople(cursor.next()));
            }
        }
        return list;
    }

    @Override
    public Optional<People> findById(String id) {
        return findDocumentById(id).map(this::toPeople);
    }

    private Optional<Document> findDocumentById(String id) {
        MongoCursor<Document> iterator = getCollection().find(eq(new ObjectId(id))).iterator();
        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
    }

    @Override
    public void delete(String id) {
        getCollection().deleteOne(eq(new ObjectId(id)));
    }

    private People toPeople(Document document) {
        return People.builder()
                .id(document.getObjectId("_id").toString())
                .name(document.getString("name"))
                .build();
    }

    private MongoCollection getCollection(){
        return mongoClient.getDatabase("quarkus-example").getCollection("people");
    }

}
