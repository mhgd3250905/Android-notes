#GreenDao基本使用

#初始化

	private DaoMaster master;
    private DaoSession session;
    private SQLiteDatabase db;
    private SonDao sonDao;
    private FatherDao fatherDao;

    private void openDb(){
        db=new DaoMaster.DevOpenHelper(MainActivity.this,"person.db",
                null).getWritableDatabase();
        master=new DaoMaster(db);
        session=master.newSession();
        sonDao=session.getSonDao();
        fatherDao=session.getFatherDao();
    }

#查询

## 1.常规查询
### 1.1 常规查询
	List<Son> sons=sonDao.queryBuilder.list();
### 1.2 懒加载查询 ###
	List<Son> sons=sonDao.queryBuilder.listLazy();

## 2.条件查询
### 2.1 eq： ###
	Son son=sonDao.queryBuilder().where(SonDao.Properties.Name.eq("ksheng")).unique();
	对应SQL：
	select * from son where name="ksheng"
### 2.2 Like： ###
	List sons=sonDao.queryBuilder().where(SonDao.Properties.Name.like("ksheng")).list();
	对应SQL：
	select * from son where name like "ksheng"
### 2.3 Between: ###
	List sons=sonDao.queryBuilder().where(SonDao.Properties.Age.between(20,30)).list();
	对应SQL：
	select * from son where age>20 and age<30
### 2.4 Gt(>) Lt(<):###
	List sons=sonDao.queryBuilder().where(SonDao.Properties.Age.gt(20)).list();
	List sons=sonDao.queryBuilder().where(SonDao.Properties.Age.lt(30)).list();
	对应SQL：
	select * from son where age>20
	select * from son where age<30
### 2.5 Ge(>=) Le(<=): ###
	List sons=sonDao.queryBuilder().where(SonDao.Properties.Age.gt(20)).list();
	List sons=sonDao.queryBuilder().where(SonDao.Properties.Age.lt(30)).list();
	对应SQL：
	select * from son where age>=20
	select * from son where age<=30
### 2.6 NotEq(!=|<>): ###
	List sons=sonDao.queryBuilder().where(SonDao.Properties.Age.notEq(50)).list();
	对应SQL：
	select * from son where age<>20
### 2.7 排序 ###
	//升序排序
	List sons=sonDao.queryBuilder().where(SonDao.Properties.Name.like("ksheng")).orderAsc(SonDao.Properties.Age).list();
	//降序排序
	List sons=sonDao.queryBuilder().where(SonDao.Properties.Name.like("ksheng")).orderDesc(SonDao.Properties.Age).list();
	对应SQL：
	select * from son where name like "ksheng" order by age asc
	select * from son where name like "ksheng" order by age desc
## 3.sql查询 ##
	//查询父亲年龄低于45岁的儿子的信息
    public void querySql(){
        List son=sonDao.queryBuilder().where(new WhereCondition.StringCondition("FATHER_ID IN"+
        "(SELCET _ID FROM FATHER WHERE AGE<45)")).list();
    }
	对应SQL：
	select * from son where father_id in（select _id from father where age<45）

## 4.多线程查询 ##
>在greenDao中，不可以在子线程中直接使用query对象进行list()等查询操作，需使用forCurrentThread()方法后查询
	
	//多线程查询
    public void queryThread(){
        final Query query=sonDao.queryBuilder().build();
        new Thread(){
            @Override
            public void run() {
                List data=query.forCurrentThread().list();
            }
        }.start();
    }

## 5.一对一，一对多查询 ##
###5.1 一对一
	public void queryOnt2One(){
        List<Son> sons=sonDao.queryBuilder().list();
        for(Son son:sons){
            Father father=son.getFather();
        }
    }
	
###5.2 一对多
#### 5.2.1 重新构建数据表 ####

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

#### 5.2.2 插入数据 ####
	private void addPerson(){
        //插入儿子
        Son son=new Son();
        son.setName("ksheng");
        son.setAge(20);
        sonDao.insert(son);

        //插入父亲前必须有已经保存的儿子
        Father tom=new Father();
        tom.setName("Tom");
        tom.setSon(son);
        fatherDao.insert(tom);
        Father jake=new Father();
        jake.setName("Jake");
        jake.setSon(son);
        fatherDao.insert(jake);
    }
#### 5.2.3 一对多查询 ####
	
	public void queryOne2Many(){
        List<Son> sons=sonDao.queryBuilder().list();
        for (Son son:sons){
            List<Father> fathers = son.getFathers();
        }
    }
	
