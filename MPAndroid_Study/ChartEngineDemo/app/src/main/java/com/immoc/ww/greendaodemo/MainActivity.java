package com.immoc.ww.greendaodemo;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.immoc.ww.greendaodemo.Adapter.MyPagerAdapter;
import com.immoc.ww.greendaodemo.fragment.BarChartFragment;
import com.immoc.ww.greendaodemo.fragment.GroupBarChartFragment;
import com.immoc.ww.greendaodemo.fragment.LineChartFragment;
import com.immoc.ww.greendaodemo.fragment.PieChartFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.WhereCondition;
import www.immoc.com.DaoMaster;
import www.immoc.com.DaoSession;
import www.immoc.com.Father;
import www.immoc.com.FatherDao;
import www.immoc.com.Son;
import www.immoc.com.SonDao;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.tl_home)
    TabLayout tlHome;
    @Bind(R.id.vp_home)
    ViewPager vpHome;
    @Bind(R.id.activity_main)
    RelativeLayout mActivityMain;

    //    @Bind(R.id.fl_Home)
//    FrameLayout mFlHome;
//    @Bind(R.id.activity_main)
//    RelativeLayout mActivityMain;
//
//    @Bind(R.id.tl_home)
//    TabLayout tlHome;
//    @Bind(R.id.vp_home)
//    ViewPager vpHome;
    private DaoMaster master;
    private DaoSession session;
    private SQLiteDatabase db;
    private SonDao sonDao;
    private FatherDao fatherDao;

    private void openDb() {
        db = new DaoMaster.DevOpenHelper(MainActivity.this, "person.db",
                null).getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();
        sonDao = session.getSonDao();
        fatherDao = session.getFatherDao();
    }

    private void addPerson() {
        //插入儿子
        Son son = new Son();
        son.setName("ksheng");
        son.setAge(20);
        sonDao.insert(son);

        //插入父亲前必须有已经保存的儿子
        Father tom = new Father();
        tom.setName("Tom");
        tom.setSon(son);
        fatherDao.insert(tom);
        Father jake = new Father();
        jake.setName("Jake");
        jake.setSon(son);
        fatherDao.insert(jake);
    }

    public void queryOne2Many() {
        List<Son> sons = sonDao.queryBuilder().list();
        for (Son son : sons) {
            List<Father> fathers = son.getFathers();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        openDb();
        //addPerson();
        initUI();
    }

    /* @描述 初始化UI */
    private void initUI() {
//        LineChartFragment chartFragment = new LineChartFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fl_Home, chartFragment).commit();


        String[] TITLE = {"LineChart", "BarChart","GroupBarChart","PieChart"};
        tlHome.setupWithViewPager(vpHome);

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        fragmentList.add(new LineChartFragment());
        fragmentList.add(new BarChartFragment());
        fragmentList.add(new GroupBarChartFragment());
        fragmentList.add(new PieChartFragment());

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList, TITLE);

        vpHome.setAdapter(adapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        tlHome.setupWithViewPager(vpHome);

        //如果我们要对ViewPager设置监听，用indicator设置就行了
        tlHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpHome.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void queryAll() {
        List<Son> sons = sonDao.queryBuilder().list();
    }

    public void queryEq() {
        Son son = sonDao.queryBuilder().where(SonDao.Properties.Name.eq("ksheng")).unique();
    }

    public void queryLike() {
        List sons = sonDao.queryBuilder().where(SonDao.Properties.Name.like("ksheng")).list();
    }

    public void queryBetween() {
        List sons = sonDao.queryBuilder().where(SonDao.Properties.Age.between(20, 30)).list();
    }

    public void queryGt() {
        List sons = sonDao.queryBuilder().where(SonDao.Properties.Age.gt(20)).list();
    }

    public void queryLt() {
        List sons = sonDao.queryBuilder().where(SonDao.Properties.Age.lt(50)).list();
    }

    public void queryGe() {
        List sons = sonDao.queryBuilder().where(SonDao.Properties.Age.ge(20)).list();
    }

    public void queryLe() {
        List sons = sonDao.queryBuilder().where(SonDao.Properties.Age.le(50)).list();
    }

    public void queryNoteq() {
        List sons = sonDao.queryBuilder().where(SonDao.Properties.Age.notEq(50)).list();
    }

    public void queryAsc() {
        List sons = sonDao.queryBuilder().where(SonDao.Properties.Name.like("ksheng")).orderAsc(SonDao.Properties.Age).list();
    }

    public void queryDesc() {
        List sons = sonDao.queryBuilder().where(SonDao.Properties.Name.like("ksheng")).orderDesc(SonDao.Properties.Age).list();
    }

    //查询父亲年龄低于45岁的儿子的信息
    public void querySql() {
        List son = sonDao.queryBuilder().where(new WhereCondition.StringCondition("FATHER_ID IN" +
                "(SELCET _ID FROM FATHER WHERE AGE<45)")).list();
    }

    //多线程查询
    public void queryThread() {
        final Query query = sonDao.queryBuilder().build();
        new Thread() {
            @Override
            public void run() {
                List data = query.forCurrentThread().list();
            }
        }.start();
    }


}
