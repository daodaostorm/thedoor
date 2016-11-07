package com.daodaostorm.thedoor.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;

public abstract class AssertJsonFactoryBase<T> {
	
	private Context context;
	private HttpEventHandler<T> httpEventHandler;
	
	public void setHttpEventHandler(HttpEventHandler<T> httpEventHandler) {
		this.httpEventHandler = httpEventHandler;
	}
	
	public AssertJsonFactoryBase(Context context) {
		this.context = context;
	}
	
	public void DownloadDatas() {
		LocalDownloadTask downloadTask = new LocalDownloadTask();
		downloadTask.execute();
	}
	
	protected abstract String getFileName();
	protected abstract T AnalysisData(JsonReader reader) throws IOException;
	
	protected T AnalysisContent() throws IOException {
		InputStream stream = context.getResources().getAssets().open(getFileName());;
		InputStreamReader streamReader = new InputStreamReader(stream, "GBK");

		JsonReader jsonReader = new JsonReader(streamReader);
		try {
			return AnalysisData(jsonReader);
		} finally {
			jsonReader.close();
			streamReader.close();
			stream.close();			
		}
	}
	
	private class LocalDownloadTask extends AsyncTask<Object, Integer, T> {

		@Override
		protected T doInBackground(Object... params) {
			try {
				return AnalysisContent();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(T result) {
			if (result == null) {
				if (httpEventHandler != null) {
					httpEventHandler.HttpFailHandler();
				}
			} else {
				if (httpEventHandler != null) {
					httpEventHandler.HttpSucessHandler(result);
				}
			}
		}
	}
}
