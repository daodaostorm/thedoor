package com.daodaostorm.thedoor;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.daodaostorm.thedoor.utils.CrashHandler;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

public class MyApp extends Application {
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		initImageLoader(getApplicationContext());
		super.onCreate();
	}
	
		private static void initImageLoader(Context context) {

			String strFolder = Environment.getExternalStorageDirectory() + "/daodaostorm/images";
			File directory = new File(strFolder);
			if (!directory.exists()) {
				directory.mkdir();
			}

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.discCache(
				new TotalSizeLimitedDiscCache(directory,
						100 * 1024 * 1024))
						// .threadPoolSize(6)
						// .writeDebugLogs() // Remove for release app
						.build();

		ImageLoader.getInstance().init(config);
	}

	/*public File getImageCacheDirectory() {

		return directory;
	}*/
}
