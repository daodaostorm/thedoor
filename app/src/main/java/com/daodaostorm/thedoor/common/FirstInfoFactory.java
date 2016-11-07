package com.daodaostorm.thedoor.common;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.util.JsonReader;


public class FirstInfoFactory extends AssertJsonFactoryBase<ArrayList<ItemInfo>> {


	public FirstInfoFactory(Context context) {
		super(context);
	}

	@Override
	protected String getFileName() {
		return "firstlist.json";
	}
	
	@Override
	protected ArrayList<ItemInfo> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<ItemInfo> list = new ArrayList<ItemInfo>(8);
		reader.beginObject();
		while (reader.hasNext()) {
			String str = reader.nextName();
			if (str.equals("data")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					ItemInfo info = new ItemInfo();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equals("img")) {
							 info.img = reader.nextString();
						} else if (name.equals("text")) {
							 info.text = reader.nextString();
						} else if (name.equals("title")) {
							 info.title = reader.nextString();
						} else if (name.equals("type")) {
							 info.type = reader.nextString();
						} else if (name.equals("id")) {
							 info.id = reader.nextString();
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

}
