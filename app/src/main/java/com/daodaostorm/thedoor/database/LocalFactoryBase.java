package com.daodaostorm.thedoor.database;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;



public abstract class LocalFactoryBase<T> {

	protected LocalOpenHelper helper = null;
	
	public LocalFactoryBase(Context context) {
		helper = new LocalOpenHelper(context);
    }

	protected abstract String getTableName();
	protected abstract String getprimaryKey();
	protected abstract T createModel(Cursor cursor);
	protected abstract void insertRecord(SQLiteDatabase db, T record);
	
	protected String getOrderColumnName() {
		return null;
	}
	
	protected long getMaxCount() {
		return 0;
	}
	
	/**
	 */
	public void insertRecord(T record) {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			if (getMaxCount() > 0) {
				long curCount = DatabaseUtils.queryNumEntries(db, getTableName());
				if (curCount >= getMaxCount()) {
					deleteOldRecord(db);
				}
			}
			insertRecord(db, record);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	/**
	 */
	public void insertRecord(List<T> records) {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			int size = records.size();
			long max = getMaxCount();
			if (max > 0) {
				long curCount = DatabaseUtils.queryNumEntries(db, getTableName());
				long total = curCount + size; 
				if (total >= max) {
					long del = total - max + 1;
					for (int i = 0; i < del; i ++) {
						deleteOldRecord(db);
					}
				}
			}
			for (T record : records) {
				insertRecord(db, record);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	/**
	 * 闂傚倷绀侀幖顐ゆ偖椤愶箑纾块柟缁㈠櫘閺佸淇婇妶鍛櫣缂佺姰鍎甸弻銊╂偄鐠囨畫鎾剁磼閻樺啿娴柟顔款瀮閹峰懐鎲撮崟顓炲殥缂傚倸鍊哥粙鍕箯閿燂拷* @return
	 */
	public ArrayList<T> findRecords() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = null;
			if (!TextUtils.isEmpty(getOrderColumnName())) {
				sql = String.format("select * from %s order by %s ASC", getTableName(), getOrderColumnName());
			} else {
				sql = String.format("select * from %s", getTableName());
			}
			cursor = db.rawQuery(sql, null);
			if (cursor != null) {
				ArrayList<T> result = new ArrayList<T>();
				while (cursor.moveToNext()) {
					result.add(createModel(cursor));
				}
				return result;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return null;
	}
	
	/**
	 * 闂傚倷绀侀幉锛勬暜閹烘嚦娑樷攽鐎ｎ�銉ヮ熆閼搁潧濮囬柦鍐枛閺屾洘寰勫Ο鐓庡弗闂佹悶鍊ч幏锟�* @param firstResult
	 * @param maxResult
	 * @return
	 */
	public ArrayList<T> findRecords(int firstResult, int maxResult) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = findCursorRecords(db, firstResult, maxResult);
			if (cursor != null) {
				ArrayList<T> result = new ArrayList<T>();
				while (cursor.moveToNext()) {
					result.add(createModel(cursor));
				}
				return result;
			}
 		} finally {
 			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return null;
	}
	
	/**
	 * 闂備礁鎼ˇ鐑藉疾濠婂牆钃熼柕濞垮剭濞差亜鍐�い鎾跺仧椤︾兘姊虹化鏇炲⒉缂佸绻橀弫鎾寸鐎ｎ偄浠繛杈剧秬椤啰娆㈤懠顒傜＝鐎广儱鎳愭晥闂佽鍨伴崯鏉戠暦閻旂⒈鏁嗛柛灞炬皑閸橆剟姊绘担鍛婃儓缂佸娼欓湁闁告洦鍘介‖蹇旂節闂堟侗鍎愰柣鎺戭煼閺屾稑鐣濋敓鎴掔捕闁汇埄鍨抽悡鐨峳sorAdapter
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public Cursor findCursorRecords(SQLiteDatabase db, int firstResult, int maxResult) {
        String sql = null;
        if (!TextUtils.isEmpty(getOrderColumnName())) {
            sql = String.format("select * from %s order by %s desc limit ?,?", getTableName(), getOrderColumnName());
        } else {
            sql = String.format("select * from %s limit ?,?", getTableName());
        }
        try {
            return db.rawQuery(sql, new String[]{String.valueOf(firstResult), String.valueOf(maxResult)});
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return null;
    }

    /**
	 * 闂備礁鎼ˇ鐑藉疾濠婂牆钃熼柕濞垮剭濞差亜鍐�い鎾跺仧椤撳ジ姊虹捄銊ユ灆濠殿喓鍊楃划濠氬箳濡や礁浠繛杈剧秬椤啰娆㈤懠顒傜＝鐎广儱鎳愭晥闂佽鍨伴崯鏉戠暦閻旂⒈鏁嗛柛灞炬皑閸橆剟姊绘担鍛婃儓缂佸娼欓湁闁告洦鍘介‖蹇旂節闂堟侗鍎愰柣鎺戭煼閺屾稑鐣濋敓鎴掔捕闁汇埄鍨抽悡鐨峳sorAdapter
	 * @return
	 */
	public Cursor findCursorRecords(SQLiteDatabase db) {
		String sql = null;
		if (!TextUtils.isEmpty(getOrderColumnName())) {
			sql = String.format("select * from %s order by %s desc", getTableName(), getOrderColumnName());
		} else {
			sql = String.format("select * from %s", getTableName());
		}
		try {
			return db.rawQuery(sql, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * @return
	 */
	public T findRecord(int primaryKey) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = String.format("select * from %s where %s=?", getTableName(), getprimaryKey());
			cursor = db.rawQuery(sql, new String[] {String.valueOf(primaryKey)});
			T result = null;
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					result = createModel(cursor);
				}
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return null;
	}

    /**
     * @param property
     * @param val
     * @return
     */
    public T findByProperty(String property, String val) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            String sql = String.format("select * from %s where %s=?", getTableName(), property);
            cursor = db.rawQuery(sql, new String[] {val});
            T result = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    result = createModel(cursor);
                }
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }
    
    /**
	 * 闂傚倷绀侀幖顐ゆ偖椤愶箑纾块柟缁㈠櫘閺佸淇婇妶蹇斿闁绘挶鍎靛浠嬪炊椤掞拷顦存繝鐢靛У閼归箖鎷戦悢鍏肩厵闂侇叏缂氶柇顖炴煕閵堝拋鍎旈柡灞稿墲瀵板嫰宕遍鐔奉伓婵犵數鍋涘Λ搴ㄥ垂娴犲绠氶柛鎰靛枛缁�銇勯幒鐐村闁哄棴绻濆娲传閸曨厼顣哄銈忕畱濞硷繝銆佸▎鎾崇倞妞ゅ繐鍊峰Ч妤呮⒑鐠恒劌娅愰柟鍑ゆ嫹* @return
	 */
	public ArrayList<T> findRecordsByProperty(String property, String val) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = null;
			if (!TextUtils.isEmpty(getOrderColumnName())) {
				sql = String.format("select * from %s where %s=? order by %s ASC", getTableName(),property, getOrderColumnName());
			} else {
				sql = String.format("select * from %s where %s=?", getTableName(), property);
			}
			cursor = db.rawQuery(sql, new String[] {val});
			if (cursor != null) {
				ArrayList<T> result = new ArrayList<T>();
				while (cursor.moveToNext()) {
					result.add(createModel(cursor));
				}
				return result;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return null;
	}
	

	public void deletedRecordsByProperty(String property, String val) {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			String sql = String.format("delete from %s where %s=?", getTableName(), property);
            db.execSQL(sql, new String[]{val});
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	public void deletedRecords() {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			String sql = String.format("delete from %s", getTableName());
            db.execSQL(sql, new String[]{});
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	public void deletedRecord(int primaryKey) {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			String sql = String.format("delete from %s where %s=?", getTableName(), getprimaryKey());
			db.execSQL(sql, new Integer[] {primaryKey});
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	/**
	 * 闂備浇宕垫慨鎶芥倿閿曪拷鐔嗘俊顖濆亹缁�﹦锟介妷銉▎闁哄绀侀湁闁稿繐鍚嬬紞鎴炵箾閸滃啯瀚� * @return
	 */
	public long getRecordCount() {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			return DatabaseUtils.queryNumEntries(db, getTableName());
		} finally {
			if (db != null) {
				db.close();
			}
		}
    }

    /**
	 * 闂傚倷绀侀幉锛勬暜閻愬绠鹃柨鐔恍掗弸宥夊箹濞ｎ剙濡肩紒顐㈢Ф閹叉瓕绠涢幘顖涚亖闂佺懓顕慨鍨垔婵傚憡鐓涘璺哄绾墎绱掓径娑欏	 * @param db
	 */
	private void deleteOldRecord(SQLiteDatabase db) {
		String sql = null;
		if (!TextUtils.isEmpty(getOrderColumnName())) {
			sql = String.format("delete from %s where %s in (select %s from %s order by %s asc limit 1)", 
					getTableName(), getprimaryKey(), getprimaryKey(), getTableName(), getOrderColumnName());
		} else {
			sql = String.format("delete from %s where %s in (select %s from %s limit 1)", 
					getTableName(), getprimaryKey(), getprimaryKey(), getTableName());
		}
		db.execSQL(sql);
	}

    /**
     */
    public T findNewestRecord() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            String sql = String.format("select * from %s order by %s desc limit 1", getTableName(), getOrderColumnName());
            cursor = db.rawQuery(sql, new String[] {});
            T result = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    result = createModel(cursor);
                }
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }
    
    /**
     * 闂傚倷绀侀幖顐ょ矓閻戣В锟介柛鎰╁妿閺嗐倕銆掑鐐缂佺嫏鍥ㄧ厓闁靛鍎辩痪褎銇勯幇顏嗙煓闁哄矉绱曟禒锔炬嫚閹绘帒袚婵＄偑鍊栧ú姗��閳ユ剚鍤曞┑鐘宠壘缁犳稒銇勮箛鎾跺婵炲牊绻堥弻锝嗘償閵忊懇妾ㄥ銈冨妽缁诲牓銆佸▎鎾存櫢闁跨噦鎷�  * @param selectRow 闂備浇宕甸崰鎰洪幋锔藉�闁挎洖鍊归崑鍌炴煥濞戞ê顏悗姘哺閺屾洝绠涚�顏嗕痪闂佷紮缍佹禍鍫曞箖濡わ拷妲堟繛鍡樕戦鍫濐湤閵堝棙顥㈤柟鍑ゆ嫹    * @param whereRow	闂傚倷绀侀幖顐λ囬悜鑺ユ櫢闁芥ê顦烽弸搴ㄦ煏韫囷絾鐎婚柟鐑芥敱娣囧﹪濡堕崟顓＄獥闂佸摜鍎戦幏锟�   * @param whereValue	闂傚倷绀侀幖顐λ囬悜鑺ユ櫢闁芥ê顦烽弸搴ㄦ煥閻曞倹瀚�   * @return
     */
    public String findSelectRowByWhereRow(String selectRow,String whereRow,String whereValue){
    	SQLiteDatabase db = null;
		Cursor cursor = null;
		String value = null;
		try {
			db = helper.getReadableDatabase();
			String sql = null;
			if (!TextUtils.isEmpty(getOrderColumnName())) {
				sql = String.format("select %s from %s  where %s=? order by %s desc",selectRow, getTableName(),whereRow, getOrderColumnName());
			} else {
				sql = String.format("select %s from %s  where %s=?",selectRow, getTableName(),whereRow);
			}
			cursor = db.rawQuery(sql,  new String[] {whereValue});
			if (cursor != null) {
				while (cursor.moveToNext()) {
					value = cursor.getString(cursor.getColumnIndex(selectRow));
				}
				return value;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return value;
    }
    
    /**
     * drop table
     * */
    
    public static void dropTable(SQLiteDatabase db, String tableName){
    	try {
    		db.execSQL(String.format("DROP TABLE IF EXISTS %s", tableName));
		} catch (Exception e) {
			//LogUtils.e("LocalFactoryBase", "drop table:" + tableName + "error.");
		}
    }
    
	/**
     * 判断表是否存在
     * @param tabName 表名
     * @return
     */
    public boolean tabIsExist(){
            boolean result = false;
            String tabName = getTableName();
            if(tabName == null){
                    return false;
            }
            return helper.tabIsExist(tabName);
    }
    
}
