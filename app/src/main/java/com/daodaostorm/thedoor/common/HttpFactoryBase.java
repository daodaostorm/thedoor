package com.daodaostorm.thedoor.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import android.os.AsyncTask;


public abstract class HttpFactoryBase<T> {

	private HttpDownloadTask task;
	private HttpEventHandler<T> httpEventHandler;

	private HttpURLConnection mHttpConection;
	private HttpPost mHttpPost;

	public void setHttpEventHandler(HttpEventHandler<T> httpEventHandler) {
		this.httpEventHandler = httpEventHandler;
	}


	/**/
	public void DownloaDatas(Object... args) {
		cancel();
		task = new HttpDownloadTask();
		task.execute(args);
		//task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args);
	}


	public T syncDownloaDatas(Object... args) {
		return doNetworkRequest(args);
	}

	public void cancel() {
		try {
			if (task != null) {
				task.cancel(true);
				task = null;
			}
			if (mHttpConection != null) {
				mHttpConection.disconnect();
				mHttpConection = null;
			}
			if (mHttpPost != null) {
				mHttpPost.abort();
				mHttpPost = null;
			}
		} catch (Exception e) {
			//LogUtils.e("httpfactorybase", e.toString());
		}
	}

	protected int getConnectTimeout() {
		return 6000;
	}

	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}


	protected abstract String CreateUri(Object... args);


	protected abstract T AnalysisContent(InputStream stream) throws IOException;

	protected T doNetworkRequest(Object... params) {
		try {
			String uri = CreateUri(params);

			if (getPostArgs() != null) {
				BasicHttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						getConnectTimeout());
				HttpConnectionParams.setSoTimeout(httpParameters,
						getConnectTimeout());
				DefaultHttpClient httpclient = new DefaultHttpClient(
						httpParameters);
				mHttpPost = new HttpPost(uri);
				mHttpPost.setEntity(new UrlEncodedFormEntity(getPostArgs(),
						HTTP.UTF_8));
				HttpResponse response = httpclient.execute(mHttpPost);
				int code = response.getStatusLine().getStatusCode();
				if (code == HttpStatus.SC_OK) {
					String responseData = EntityUtils.toString(
							response.getEntity(), HTTP.UTF_8);
					ByteArrayInputStream stream = new ByteArrayInputStream(
							responseData.getBytes());
					try {
						return AnalysisContent(stream);
					} finally {
						stream.close();
					}
				}
			} else {
				mHttpConection = (HttpURLConnection) new URL(uri)
						.openConnection();
				mHttpConection.setReadTimeout(getConnectTimeout());
				mHttpConection.setConnectTimeout(getConnectTimeout());
				mHttpConection.connect();
				int code = mHttpConection.getResponseCode();
				if (code == HttpStatus.SC_OK) {
					InputStream stream = mHttpConection.getInputStream();
					try {
						return AnalysisContent(stream);
					} finally {
						stream.close();
					}
				}
			}
		} catch (MalformedURLException e) {
			//LogUtils.e("httpfactorybasehi1", e.toString());
		} catch (IOException e) {
			///LogUtils.e("httpfactorybasehi2", e.toString());
		}
		return null;
	}

	private class HttpDownloadTask extends AsyncTask<Object, Integer, T> {

		@Override
		protected T doInBackground(Object... params) {
			if (isCancelled()) {
				return null;
			}
			return doNetworkRequest(params);
		}

		@Override
		protected void onPostExecute(T result) {
			if (isCancelled()) {
				return;
			}
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
