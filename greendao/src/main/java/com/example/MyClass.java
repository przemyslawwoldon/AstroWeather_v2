package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyClass {

    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.example.przemyslaw.aw.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();
        addTables(schema);
        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        addUserEntities(schema);
    }
    // This is use to describe the colums of your table
    private static Entity addUserEntities(final Schema schema) {
        Entity city = schema.addEntity("City");
        city.addIdProperty().primaryKey().autoincrement();
        city.addStringProperty("name");
        city.addDoubleProperty("latitude");
        city.addDoubleProperty("longitude");
        return city;
    }

}