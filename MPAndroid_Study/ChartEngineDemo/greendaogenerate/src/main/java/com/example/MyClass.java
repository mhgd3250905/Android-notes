package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    public static void main(String[] arg)throws Exception{
        Schema schema=new Schema(1,"www.immoc.com");
        Entity son = schema.addEntity("Son");
        son.addIdProperty();
        son.addStringProperty("name");
        son.addIntProperty("age");

        Entity father=schema.addEntity("Father");
        father.addIdProperty();
        father.addStringProperty("name");
        father.addIntProperty("age");
        Property sonId = father.addLongProperty("sonId").getProperty();

        //一个父亲对应一个儿子
        father.addToOne(son,sonId);
        //一个儿子对应多个父亲
        son.addToMany(father,sonId).setName("fathers");

        new DaoGenerator().generateAll(schema,"app/src/main/java");
    }
}
