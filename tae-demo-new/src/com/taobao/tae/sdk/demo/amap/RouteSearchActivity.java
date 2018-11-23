package com.taobao.tae.sdk.demo.amap;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.taobao.tae.sdk.demo.R;
import com.taobao.tae.sdk.demo.amap.RouteSearchPoiDialog.OnListItemClick;
import com.taobao.tae.sdk.demo.amap.utils.ToastUtil;

public class RouteSearchActivity extends Activity implements OnPoiSearchListener, OnRouteSearchListener,
        OnClickListener {

    private Button drivingButton;
    private Button busButton;
    private Button walkButton;

    private ImageButton startImageButton;
    private ImageButton endImageButton;
    private ImageButton routeSearchImagebtn;

    private EditText startTextView;
    private EditText endTextView;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private int busMode = RouteSearch.BusDefault;// 公交默认模式
    private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
    private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
    private BusRouteResult busRouteResult;// 公交模式查询结果
    private DriveRouteResult driveRouteResult;// 驾车模式查询结果
    private WalkRouteResult walkRouteResult;// 步行模式查询结果
    private int routeType = 1;// 1代表公交模式，2代表驾车模式，3代表步行模式
    private String strStart;
    private String strEnd;
    private LatLonPoint startPoint = null;
    private LatLonPoint endPoint = null;
    private PoiSearch.Query startSearchQuery;
    private PoiSearch.Query endSearchQuery;

    private RouteSearch routeSearch;
    public ArrayAdapter<String> aAdapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_routesearch);
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        startTextView = (EditText) findViewById(R.id.autotextview_roadsearch_start);
        endTextView = (EditText) findViewById(R.id.autotextview_roadsearch_goals);
        busButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_transit);
        busButton.setOnClickListener(this);
        drivingButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_driving);
        drivingButton.setOnClickListener(this);
        walkButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_walk);
        walkButton.setOnClickListener(this);
        startImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_startoption);
        startImageButton.setOnClickListener(this);
        endImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_endoption);
        endImageButton.setOnClickListener(this);
        routeSearchImagebtn = (ImageButton) findViewById(R.id.imagebtn_roadsearch_search);
        routeSearchImagebtn.setOnClickListener(this);
    }

    /**
     * 选择公交模式
     */
    private void busRoute() {
        routeType = 1;// 标识为公交模式
        busMode = RouteSearch.BusDefault;
        drivingButton.setBackgroundResource(R.drawable.mode_driving_off);
        busButton.setBackgroundResource(R.drawable.mode_transit_on);
        walkButton.setBackgroundResource(R.drawable.mode_walk_off);

    }

    /**
     * 选择驾车模式
     */
    private void drivingRoute() {
        routeType = 2;// 标识为驾车模式
        drivingButton.setBackgroundResource(R.drawable.mode_driving_on);
        busButton.setBackgroundResource(R.drawable.mode_transit_off);
        walkButton.setBackgroundResource(R.drawable.mode_walk_off);
    }

    /**
     * 选择步行模式
     */
    private void walkRoute() {
        routeType = 3;// 标识为步行模式
        walkMode = RouteSearch.WalkMultipath;
        drivingButton.setBackgroundResource(R.drawable.mode_driving_off);
        busButton.setBackgroundResource(R.drawable.mode_transit_off);
        walkButton.setBackgroundResource(R.drawable.mode_walk_on);
    }

    /**
     * 点击搜索按钮开始Route搜索
     */
    public void searchRoute() {
        strStart = startTextView.getText().toString().trim();
        strEnd = endTextView.getText().toString().trim();
        if (strStart == null || strStart.length() == 0) {
            ToastUtil.show(RouteSearchActivity.this, "请选择起点");
            return;
        }
        if (strEnd == null || strEnd.length() == 0) {
            ToastUtil.show(RouteSearchActivity.this, "请选择终点");
            return;
        }
        if (strStart.equals(strEnd)) {
            ToastUtil.show(RouteSearchActivity.this, "起点与终点距离很近，您可以步行前往");
            return;
        }

        startSearchResult();// 开始搜终点
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 查询路径规划起点
     */
    public void startSearchResult() {
        strStart = startTextView.getText().toString().trim();
        if (startPoint != null && strStart.equals("地图上的起点")) {
            endSearchResult();
        } else {
            showProgressDialog();
            startSearchQuery = new PoiSearch.Query(strStart, "", "010"); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
            startSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
            startSearchQuery.setPageSize(20);// 设置每页返回多少条数据
            PoiSearch poiSearch = new PoiSearch(RouteSearchActivity.this, startSearchQuery);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();// 异步poi查询
        }
    }

    /**
     * 查询路径规划终点
     */
    public void endSearchResult() {
        strEnd = endTextView.getText().toString().trim();
        if (endPoint != null && strEnd.equals("地图上的终点")) {
            searchRouteResult(startPoint, endPoint);
        } else {
            showProgressDialog();
            endSearchQuery = new PoiSearch.Query(strEnd, "", "010"); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
            endSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
            endSearchQuery.setPageSize(20);// 设置每页返回多少条数据

            PoiSearch poiSearch = new PoiSearch(RouteSearchActivity.this, endSearchQuery);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn(); // 异步poi查询
        }
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        if (routeType == 1) {// 公交路径规划
            BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode, "0571", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            routeSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == 2) {// 驾车路径规划
            DriveRouteQuery query = new DriveRouteQuery(fromAndTo, drivingMode, null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == 3) {// 步行路径规划
            WalkRouteQuery query = new WalkRouteQuery(fromAndTo, walkMode);
            routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }

    @Override
    public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {

    }

    /**
     * POI搜索结果回调
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {// 返回成功
            if (result != null && result.getQuery() != null && result.getPois() != null && result.getPois().size() > 0) {// 搜索poi的结果
                if (result.getQuery().equals(startSearchQuery)) {
                    List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
                    RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(RouteSearchActivity.this, poiItems);
                    dialog.setTitle("您要找的起点是:");
                    dialog.show();
                    dialog.setOnListClickListener(new OnListItemClick() {
                        @Override
                        public void onListItemClick(RouteSearchPoiDialog dialog, PoiItem startpoiItem) {
                            startPoint = startpoiItem.getLatLonPoint();
                            strStart = startpoiItem.getTitle();
                            startTextView.setText(strStart);
                            endSearchResult();// 开始搜终点
                        }

                    });
                } else if (result.getQuery().equals(endSearchQuery)) {
                    List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
                    RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(RouteSearchActivity.this, poiItems);
                    dialog.setTitle("您要找的终点是:");
                    dialog.show();
                    dialog.setOnListClickListener(new OnListItemClick() {
                        @Override
                        public void onListItemClick(RouteSearchPoiDialog dialog, PoiItem endpoiItem) {
                            endPoint = endpoiItem.getLatLonPoint();
                            strEnd = endpoiItem.getTitle();
                            endTextView.setText(strEnd);
                            searchRouteResult(startPoint, endPoint);// 进行路径规划搜索
                        }

                    });
                }
            } else {
                ToastUtil.show(RouteSearchActivity.this, R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(RouteSearchActivity.this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(RouteSearchActivity.this, R.string.error_key);
        } else {
            ToastUtil.show(RouteSearchActivity.this, getString(R.string.error_other) + rCode);
        }
    }

    /**
     * 公交路线查询回调
     */
    @Override
    public void onBusRouteSearched(BusRouteResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {
            if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
                busRouteResult = result;
                BusPath busPath = busRouteResult.getPaths().get(0);
                StringBuilder busPathResult = new StringBuilder();
                busPathResult.append("距离(m) : " + busPath.getDistance()).append("\n");
                busPathResult.append("时间(s) : " + busPath.getDuration()).append("\n");
                int i = 0;
                for (BusStep busStep : busPath.getSteps()) {
                    RouteBusLineItem busLine = busStep.getBusLine();
                    if (busLine == null) {
                        continue;
                    }
                    busPathResult.append((i++) + ": [" + busLine.getBusLineName() + "]").append("]\n");
                }
                this.displaySearchResult(busPathResult.toString());
            } else {
                ToastUtil.show(RouteSearchActivity.this, R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(RouteSearchActivity.this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(RouteSearchActivity.this, R.string.error_key);
        } else {
            ToastUtil.show(RouteSearchActivity.this, getString(R.string.error_other) + rCode);
        }
    }

    /**
     * 驾车结果回调
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {
            if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
                driveRouteResult = result;
                DrivePath drivePath = driveRouteResult.getPaths().get(0);

                StringBuilder drivePathResult = new StringBuilder();
                drivePathResult.append("距离(m) : " + drivePath.getDistance()).append("\n");
                drivePathResult.append("时间(s) : " + drivePath.getDuration()).append("\n");
                int i = 0;
                for (DriveStep driveStep : drivePath.getSteps()) {
                    drivePathResult.append((i++) + ":" + driveStep.getRoad()).append("\n");
                }
                this.displaySearchResult(drivePathResult.toString());
            } else {
                ToastUtil.show(RouteSearchActivity.this, R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(RouteSearchActivity.this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(RouteSearchActivity.this, R.string.error_key);
        } else {
            ToastUtil.show(RouteSearchActivity.this, getString(R.string.error_other) + rCode);
        }
    }

    /**
     * 步行路线结果回调
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {
            if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
                walkRouteResult = result;
                WalkPath walkPath = walkRouteResult.getPaths().get(0);
                StringBuilder walkPathResult = new StringBuilder();
                walkPathResult.append("距离(m) : " + walkPath.getDistance()).append("\n");
                walkPathResult.append("时间(s) : " + walkPath.getDuration()).append("\n");
                int i = 0;
                for (WalkStep walkStep : walkPath.getSteps()) {
                    walkPathResult.append((i++) + ":" + walkStep.getRoad()).append("\n");
                }
                this.displaySearchResult(walkPathResult.toString());
            } else {
                ToastUtil.show(RouteSearchActivity.this, R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(RouteSearchActivity.this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(RouteSearchActivity.this, R.string.error_key);
        } else {
            ToastUtil.show(RouteSearchActivity.this, getString(R.string.error_other) + rCode);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imagebtn_roadsearch_tab_transit:
            busRoute();
            break;
        case R.id.imagebtn_roadsearch_tab_driving:
            drivingRoute();
            break;
        case R.id.imagebtn_roadsearch_tab_walk:
            walkRoute();
            break;
        case R.id.imagebtn_roadsearch_search:
            searchRoute();
            break;
        default:
            break;
        }
    }

    private void displaySearchResult(String result) {
        TextView view = (TextView) this.findViewById(R.id.routeQueryResult);
        view.setText(result);
    }
}
