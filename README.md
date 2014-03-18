高德地图热点图
=============

用法

	TileProvider tileProvider = new HeatmapTileProvider
	.Builder()
	.radius(15) //半径
	.weightedData(WeightedLatLngCollection) 
	.build();
	//WeightedLatLngCollection是Collection<WeightedLatLng>集合
	
	tileOverlay = aMap.addTileOverlay(new TileOverlayOptions()
					.tileProvider(tileProvider)
					.diskCacheDir(cache + "/amap").diskCacheEnabled(false)
					.diskCacheSize(10000));