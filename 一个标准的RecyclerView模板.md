#一个标准的RecyclerView模板

----------

### 1 在view中初始化RecyclerView
	
	@Bind(R.id.rv_huxiu)
    RecyclerView rvData;
	
	/* @描述 设置Adapter */
        adapter = new BaseAdapter(getContext(),mDataList);

        /* @描述 布局 */
        LinearLayoutManager llManager=new LinearLayoutManager(getContext());
        rvData.setLayoutManager(llManager);
        /* @描述 设置间距 */
        SpacesItemDecoration mDecoration = new SpacesItemDecoration(3);
        /* @描述 添加间距 */
        rvData.addItemDecoration(mDecoration);
        /* @描述 设置基本动画 */
        rvData.setItemAnimator(new DefaultItemAnimator());
        /* @描述 rvNoteList */
        rvData.setAdapter(adapter);

### 2.Adapter
#### 2.1.RecyclerViewBaseAdapter ####

	/*
	* 
	* 描    述：
	* 作    者：ksheng
	* 时    间：
	*/
	
	import android.support.v7.widget.RecyclerView;
	import android.view.View;
	import android.view.ViewGroup;
	
	import java.util.ArrayList;
	import java.util.List;
	
	import ViewHolder.RecyclerViewHolderBase;
	
	/**
	 * 横向RecyclerView基类adapter
	 *
	 * @param <ItemDataType> 数据实体类型
	 * @author Robin
	 *         time 2015-04-10 12:33:43
	 */
	public abstract class RecyclerViewBaseAdapter<ItemDataType> extends
	        RecyclerView.Adapter<RecyclerViewHolderBase> {
	
	    protected List<ItemDataType> mItemDataList = new ArrayList<ItemDataType>();
	
	    public void setmItemDataList(List<ItemDataType> mItemDataList) {
	        this.mItemDataList = mItemDataList;
	    }
	
	    public List<ItemDataType> getItemDataList() {
	        return mItemDataList;
	    }
	
	    public void setItemDataList(List<ItemDataType> itemDataList) {
	        mItemDataList = itemDataList;
	    }
	
	    public RecyclerViewBaseAdapter(List<ItemDataType> mItemDataList) {
	        this.mItemDataList = mItemDataList;
	    }
	
	    /**
	     * 动态增加一条数据
	     *
	     * @param itemDataType 数据实体类对象
	     */
	    public void append(int position, ItemDataType itemDataType) {
	        if (itemDataType != null) {
	            mItemDataList.add(position+1,itemDataType);
	            notifyItemInserted(position + 1);
	            notifyItemRangeChanged(position + 1, mItemDataList.size());
	        }
	    }
	
	
	    /*
	     * @desc 设置点击事件
	     * @时间 2016/6/18 15:53
	     */
	    public interface OnItemClickLitener {
	        void onItemClick(View view, int position);
	
	        void onItemLongClick(View view, int position);
	    }
	
	    protected OnItemClickLitener mOnItemClickLitener;
	
	    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
	        this.mOnItemClickLitener = mOnItemClickLitener;
	    }
	
	
	    /**
	     * 动态增加一组数据集合
	     *
	     * @param itemDataTypes 数据实体类集合
	     */
	    public void append(List<ItemDataType> itemDataTypes) {
	        if (itemDataTypes.size() > 0) {
	            for (ItemDataType itemDataType : itemDataTypes) {
	                mItemDataList.add(itemDataType);
	            }
	            notifyDataSetChanged();
	        }
	    }
	
	    /**
	     * 替换全部数据
	     *
	     * @param itemDataTypes 数据实体类集合
	     */
	    public void replace(List<ItemDataType> itemDataTypes) {
	        mItemDataList.clear();
	        if (itemDataTypes.size() > 0) {
	            mItemDataList.addAll(itemDataTypes);
	            notifyDataSetChanged();
	        }
	    }
	
	    /**
	     * 移除一条数据集合
	     *
	     * @param position
	     */
	    public void remove(int position) {
	        mItemDataList.remove(position);
	        notifyItemRemoved(position);
	    }
	
	    /**
	     * 移除一条ScheduleItem
	     *
	     * @param position
	     */
	    public void removeScheduleItem(int position) {
	        if (position == mItemDataList.size()) {
	            notifyItemRemoved(position);
	        } else {
	            mItemDataList.remove(position);
	            notifyDataSetChanged();
	        }
	    }
	
	
	    /**
	     * 移除所有数据
	     */
	    public void removeAll() {
	        mItemDataList.clear();
	        notifyDataSetChanged();
	    }
	
	
	    /*
	    * @方法 获取mItemDataList
	    *
	    */
	    public List<ItemDataType> getItemList() {
	        return mItemDataList;
	    }
	
	    /*
	    * @方法 remove_pos
	    *
	    */
	    public void removePosItem(int pos) {
	        mItemDataList.remove(pos);
	    }
	
	
	    /**
	     * 更新pos数据
	     */
	    public void update(int pos) {
	        notifyItemChanged(pos);
	    }
	
	
	    @Override
	    public int getItemCount() {
	        return mItemDataList.size();
	    }
	
	    @Override
	    public void onBindViewHolder(final RecyclerViewHolderBase viewHolder, int i) {
	        showData(viewHolder, i, mItemDataList);
	        // 如果设置了回调，则设置点击事件
	        if (mOnItemClickLitener != null) {
	            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                    int pos = viewHolder.getLayoutPosition();
	                    mOnItemClickLitener.onItemClick(viewHolder.itemView, pos);
	                }
	            });
	
	            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
	                @Override
	                public boolean onLongClick(View v) {
	                    int pos = viewHolder.getLayoutPosition();
	                    mOnItemClickLitener.onItemLongClick(viewHolder.itemView, pos);
	                    return false;
	                }
	            });
	        }
	
	    }
	
	    @Override
	    public RecyclerViewHolderBase onCreateViewHolder(ViewGroup viewGroup, int i) {
	        View view = createView(viewGroup, i);
	        RecyclerViewHolderBase holder = createViewHolder(view);
	        return holder;
	    }
	
	
	    /**
	     * 显示数据抽象函数
	     *
	     * @param viewHolder    基类ViewHolder,需要向下转型为对应的ViewHolder（example:MainRecyclerViewHolder mainRecyclerViewHolder=(MainRecyclerViewHolder) viewHolder;）
	     * @param i             位置
	     * @param mItemDataList 数据集合
	     */
	    public abstract void showData(RecyclerViewHolderBase viewHolder, int i, List<ItemDataType> mItemDataList);
	
	    /**
	     * 加载item的view,直接返回加载的view即可
	     *
	     * @param viewGroup 如果需要Context,可以viewGroup.getContext()获取
	     * @param i
	     * @return item 的 view
	     */
	    public abstract View createView(ViewGroup viewGroup, int i);
	
	    /**
	     * 加载一个ViewHolder,为RecyclerViewHolderBase子类,直接返回子类的对象即可
	     *
	     * @param view item 的view
	     * @return RecyclerViewHolderBase 基类ViewHolder
	     */
	    public abstract RecyclerViewHolderBase createViewHolder(View view);
	}

#### 2.2 RecyclerViewHolderBase

	/*
	* 
	* 描    述：
	* 作    者：ksheng
	* 时    间：
	*/
	
	import android.support.v7.widget.RecyclerView;
	import android.view.View;
	
	/**
	 * RecyclerViewHolder基类
	 * @author Robin
	 *time 2015-04-10 11:43:49
	 */
	public abstract class RecyclerViewHolderBase extends RecyclerView.ViewHolder {
	
	    public RecyclerViewHolderBase(View itemView) {
	
	        super(itemView);
	    }
	
	}

#### 2.3 继承RecyclerViewBaseAdapter的具体Adapter

>这里传入的是BaseBean，使用的viewHolder是HuxiuViewHolder

	/*
	* 
	* 描    述：
	* 作    者：ksheng
	* 时    间：2016/11/21$ 21:24$.
	*/
	public class BaseAdapter extends RecyclerViewBaseAdapter<BaseBean> {
	    private Context context;
	    private LayoutInflater inflater;
	
	    public BaseAdapter(Context context, List<BaseBean> mItemDataList) {
	        super(mItemDataList);
	        this.context = context;
	        inflater = LayoutInflater.from(context);
	    }
	
	    @Override
	    public void showData(RecyclerViewHolderBase viewHolder, int i, List<BaseBean> mItemDataList) {
	        //向下转型为子类
	        HuxiuViewHolder holder = (HuxiuViewHolder) viewHolder;
	
	        holder.tvItem.setText(mItemDataList.get(i).getTitle().replace("\uE40B"," ").replace("\uE40A"," "));
	
	        if (!TextUtils.isEmpty(mItemDataList.get(i).getImgSrc())) {
	            Glide.with(context)
	                    .load(mItemDataList.get(i).getImgSrc())
	                    .diskCacheStrategy(DiskCacheStrategy.ALL)
	                    .into(holder.ivItem);
	        }
	    }
	
	
	
	    @Override
	    public View createView(ViewGroup viewGroup, int i) {
	        context = viewGroup.getContext();
	        //加载item的布局
	        View view = inflater.inflate(R.layout.item_information, viewGroup, false);
	        return view;
	    }
	
	    @Override
	    public RecyclerViewHolderBase createViewHolder(View view) {
	        //直接返回viewholder对象
	        return new HuxiuViewHolder(view);
	    }
	}

#### 2.4.继承BaseViewHolder的具体ViewHolder ####

>viewHolder的具体实现
>
	/*
	* 
	* 描    述：
	* 作    者：ksheng
	* 时    间：2016/11/21$ 21:26$.
	*/
	public class HuxiuViewHolder extends RecyclerViewHolderBase{
	    @Bind(R.id.iv_item)
	    public ImageView ivItem;
	    @Bind(R.id.tv_item)
	    public TextView tvItem;
	    public HuxiuViewHolder(View itemView) {
	        super(itemView);
	        ButterKnife.bind(this,itemView);
	    }
	}

完成.
	

### 3.ItemLayout ###

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:orientation="vertical">
	
	<LinearLayout
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:orientation="horizontal">
	    <ImageView
	        android:layout_marginLeft="5dp"
	        android:src="@mipmap/ic_launcher"
	        android:focusable="false"
	        android:focusableInTouchMode="false"
	        android:id="@id/iv_item"
	        android:scaleType="fitCenter"
	        android:layout_width="80dp"
	        android:layout_height="70dp" />
	
	    <TextView
	
	        android:layout_marginLeft="5dp"
	        android:layout_marginRight="5dp"
	        android:gravity="center_vertical"
	        android:textSize="15dp"
	        android:text="内容"
	        android:focusable="false"
	        android:focusableInTouchMode="false"
	        android:id="@id/tv_item"
	        android:layout_width="match_parent"
	        android:layout_height="70dp"
	        />
	</LinearLayout>
	<!--这是一根线-->
	    <ImageView
	        android:layout_marginRight="2dp"
	        android:layout_marginLeft="2dp"
	        android:layout_width="match_parent"
	        android:layout_height="0.1dp"
	        android:background="@color/colorPrimary"/>
	    
	</LinearLayout>
