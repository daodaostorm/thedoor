package com.daodaostorm.thedoor.common;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.util.JsonReader;
import android.util.Log;


public class SkinFactory extends HttpJsonFactoryBase<ArrayList<SkinInfo>> {

	@SuppressLint({ "NewApi", "NewApi" })
	@Override
	protected ArrayList<SkinInfo> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<SkinInfo> list = new ArrayList<SkinInfo>(8);
		reader.beginObject();
		while (reader.hasNext()) {
			String str = reader.nextName();
			if (str.equals("data")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					SkinInfo info = new SkinInfo();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equals("skin_name")) {
							 info.skin_name = reader.nextString();
						} else if (name.equals("download_url")) {
							 info.download_url = reader.nextString();
						} else if (name.equals("folder_name")) {
							 info.folder_name = reader.nextString();
						} else if (name.equals("md5")) {
							 info.md5 = reader.nextString();
						} else if (name.equals("skin_version")) {
							 info.skin_version = reader.nextString();
						} else {
							reader.skipValue();
						}
					}

					list.add(info);
					reader.endObject();
				}
				reader.endArray();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return list;
	}

	@Override
	protected String CreateUri(Object... args) {

		return String.format("http://www.tofuos.com/tencent/skins/skin.t-skin.version");
		
	}

}
