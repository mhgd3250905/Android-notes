#Retrofit2.0+Rxjava初探
###1.WebService

	public interface WebService {

	    @Headers({
	            "X-Bmob-Application-Id: 9e16e39fa5374f446e5c928da0f83d62",
	            "X-Bmob-REST-API-Key: 42db163cd4c45884279b914e1c2a4e75",
	            "Content-Type: application/json"
	    })
	    @GET("1/classes/{tableName}")
	    Observable<BaseGsonBean> getHXGsonData(@Path("tableName")String tableName,
	                                           @Query("limit")String limit, @Query("skip") String skip,
	                                           @Query("order")String order);
	
	    //获取暴走日报首页内容
	    @GET("http://dailyapi.ibaozou.com/api/v31/documents/latest")
	    Observable<BaozouGsonBean> getBaozouGsonBean();
	
	    //获取暴走日报首页内容
	    @GET("http://dailyapi.ibaozou.com/api/v31/documents/latest")
	    Observable<BaozouGsonBean> getNextBaozouGsonBean(@Query("timestamp")int timestamp);
	
	}

### 2.初始化配置

	//新的配置
        retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(WebService.class);

### 3.结合Rxjava

	service.getBaozouGsonBean()
	                .subscribeOn(Schedulers.io())
	                .map(new Func1<BaozouGsonBean, List<BaseBean>>() {
	                    @Override
	                    public List<BaseBean> call(BaozouGsonBean baozouGsonBean) {
	                        //获取时间戳
	                        timeStamp=baozouGsonBean.getTimestamp();
	
	                        List<BaozouGsonBean.DataBean> results = baozouGsonBean.getData();
	                        List<BaseBean> responses=new ArrayList<BaseBean>();
	//                        LogUtils.Log(""+results.size());
	
	                        for (BaozouGsonBean.DataBean resultsBean:results){
	
	//                            LogUtils.Log(resultsBean.getTitle());
	
	                            BaseBean baseBean =new BaseBean();
	                            baseBean.setTitle(resultsBean.getTitle());
	                            baseBean.setImgSrc(resultsBean.getThumbnail());
	                            baseBean.setContentURL(resultsBean.getUrl());
	                            responses.add(baseBean);
	                        }
	                        return responses;
	                    }
	                })
	                .observeOn(AndroidSchedulers.mainThread())
	                .subscribe(new Subscriber<List<BaseBean>>() {
	                    @Override
	                    public void onCompleted() {
	//                        LogUtils.Log("completed");
	                        mRefreshLayout.endRefreshing();
	                        mRefreshLayout.endLoadingMore();
	//                        if (rvData.isRefresh()) {
	//                            rvData.setPullLoadMoreCompleted();
	//                        }
	                    }
	
	                    @Override
	                    public void onError(Throwable e) {
	
	                    }
	
	                    @Override
	                    public void onNext(List<BaseBean> huXiuList) {
	                        LogUtils.Log("这里这里这里是一开始~~~"+huXiuList.size()+"");
	                        adapter.append(huXiuList);
	                    }
                });