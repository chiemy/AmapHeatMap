package com.example.amapdemo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.TileProvider;
import com.example.amapdemo.heatmap.Gradient;
import com.example.amapdemo.heatmap.HeatmapTileProvider;
import com.example.amapdemo.heatmap.WeightedLatLng;

public class MainActivity extends Activity implements LocationSource,AMapLocationListener {
	private MapView mapView;
	private AMap aMap;
	private TileOverlay tileOverlay;
	/**
     * Maps name of data set to data (list of LatLngs)
     * Also maps to the URL of the data set for attribution
     */
    private HashMap<String, DataSet> mLists = new HashMap<String, DataSet>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		startDemo();
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setLocationSource(this);
			aMap.setMyLocationEnabled(true);
			UiSettings uiSet = aMap.getUiSettings();
			uiSet.setMyLocationButtonEnabled(true);
			uiSet.setCompassEnabled(true);
			uiSet.setRotateGesturesEnabled(true);
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.918503, 116.42546), 10));
			showTileOverlay();
		}
	}
	/**
	 */
	private void showTileOverlay() {
		if (tileOverlay != null) {
			tileOverlay.remove();
		}
		//weightedData 有权重的数据
		TileProvider tileProvider = new HeatmapTileProvider.Builder().radius(15).weightedData(
                mLists.get(getString(R.string.police_stations)).getData()).build();
		if (tileProvider != null) {
			String cache = getExternalCacheDir().getAbsolutePath();
			//设置不缓存，否侧出现黑色瓦片？？
			tileOverlay = aMap.addTileOverlay(new TileOverlayOptions()
					.tileProvider(tileProvider)
					.diskCacheDir(cache + "/amap").diskCacheEnabled(false)
					.diskCacheSize(10000));
		}

	}
	
    private void startDemo() {
        try {
            mLists.put(getString(R.string.police_stations), new DataSet(readItems(R.raw.costume),
                    getString(R.string.police_stations_url)));
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }

    }
	
	
	private ArrayList<WeightedLatLng> readItems(int resource) throws JSONException {
        ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            double count = object.getDouble("count");
            LatLng latlng = new LatLng(lat, lng);
            list.add(new WeightedLatLng(latlng, count/100));
        }
        return list;
    }
	
	/**
     * Helper class - stores data sets and sources.
     */
    private class DataSet {
        private ArrayList<WeightedLatLng> mDataset;
        private String mUrl;

        public DataSet(ArrayList<WeightedLatLng> dataSet, String url) {
            this.mDataset = dataSet;
            this.mUrl = url;
        }

        public ArrayList<WeightedLatLng> getData() {
            return mDataset;
        }

        public String getUrl() {
            return mUrl;
        }
    }

	@Override
	public void onLocationChanged(Location location) {
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
	@Override
	public void onLocationChanged(AMapLocation arg0) {
		
	}
	@Override
	public void activate(OnLocationChangedListener arg0) {
		
	}
	@Override
	public void deactivate() {
		
	}
}
