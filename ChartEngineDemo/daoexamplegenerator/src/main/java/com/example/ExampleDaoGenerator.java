package com.example;

import javax.xml.validation.Schema;

public class ExampleDaoGenerator {
    public static void main(String[] args)throws Exception{
        //1.创建一个用于添加实体（Entity）的模式（Scheme）对象
        //  两个参数分别代表：数据库版本号与自动生成代码的包路径
        Schema schema=new Schema(1,"com.example.greendao")
    }
}
