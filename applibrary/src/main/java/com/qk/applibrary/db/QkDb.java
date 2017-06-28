/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qk.applibrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.qk.applibrary.db.sqlite.CursorUtils;
import com.qk.applibrary.db.sqlite.SqlBuilder;
import com.qk.applibrary.db.sqlite.SqlInfo;
import com.qk.applibrary.db.table.KeyValue;
import com.qk.applibrary.db.table.TableInfo;
import com.qk.applibrary.exception.DbException;
import com.qk.applibrary.listener.DbUpdateListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对数据库增删查改
 */
public class QkDb {

	private static final String TAG = "QkDb";
	private SQLiteDatabase db;
	private static HashMap<String, QkDb> daoMap = new HashMap<String, QkDb>();
	private DaoConfig config;

	private QkDb(DaoConfig config) {
		if(config == null) {
			throw new DbException("daoConfig is null");
		} else if(config.getContext() == null) {
			throw new DbException("android context is null");
		} else {
			if(config.getTargetDirectory() != null && config.getTargetDirectory().trim().length() > 0) {
				/**
				 * 将数据库放在sd卡
				 */
				this.db = this.createDbFileOnSDCard(config.getTargetDirectory(), config.getDbName());
			} else {
				this.db = (new SqliteDbHelper(config.getContext().getApplicationContext(), config.getDbName(), config.getDbVersion(), config.getDbUpdateListener())).getWritableDatabase();
			}
			this.config = config;
		}
	}

	private synchronized static QkDb getInstance(DaoConfig daoConfig) {
		QkDb dao = daoMap.get(daoConfig.getDbName());
		if (dao == null) {
			dao = new QkDb(daoConfig);
			daoMap.put(daoConfig.getDbName(), dao);
		}
		return dao;
	}

	/**
	 * 创建数据库
	 * @param context 上下文
	 */
	public static QkDb create(Context context) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		return create(config);
	}

	/**
	 * 创建数据库
	 * @param context 上下文
	 * @param isDebug
	 *            是否是debug模式（debug模式进行数据库操作的时候将会打印sql语句）
	 */
	public static QkDb create(Context context, boolean isDebug) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDebug(isDebug);
		return create(config);

	}

	/**
	 * 创建数据库
	 * @param context 上下文
	 * @param dbName
	 *            数据库名称
	 */
	public static QkDb create(Context context, String dbName) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		return create(config);
	}

	/**
	 * 创建数据库
	 * @param context 上下文
	 * @param dbName 数据库名称
	 * @param isDebug 是否为debug模式（debug模式进行数据库操作的时候将会打印sql语句）
	 */
	public static QkDb create(Context context, String dbName, boolean isDebug) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		config.setDebug(isDebug);
		return create(config);
	}

	/**
	 * 创建数据库
	 * @param context 上下文
	 * @param targetDirectory  保存数据库到sd卡路径
	 * @param dbName 数据库名字
	 * @return
	 */
	public static QkDb create(Context context, String targetDirectory,
							  String dbName) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		config.setTargetDirectory(targetDirectory);
		return create(config);
	}

	/**
	 * 创建数据库
	 * @param context 上下文
	 * @param targetDirectory  保存数据库到sd卡路径
	 * @param dbName
	 *            数据库名称
	 * @param isDebug
	 *            是否为debug模式（debug模式进行数据库操作的时候将会打印sql语句）
	 */
	public static QkDb create(Context context, String targetDirectory,
							  String dbName, boolean isDebug) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setTargetDirectory(targetDirectory);
		config.setDbName(dbName);
		config.setDebug(isDebug);
		return create(config);
	}

	/**
	 * 创建数据库
	 * @param context 上下文
	 * @param dbName
	 *            数据库名字
	 * @param isDebug
	 *            是否是调试模式：调试模式会log出sql信息
	 * @param dbVersion
	 *            数据库版本信息
	 * @param dbUpdateListener
	 *            数据库升级监听器：如果监听器为null，升级的时候将会清空所所有的数据
	 * @return
	 */
	public static QkDb create(Context context, String dbName,
							  boolean isDebug, int dbVersion, DbUpdateListener dbUpdateListener) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		config.setDebug(isDebug);
		config.setDbVersion(dbVersion);
		config.setDbUpdateListener(dbUpdateListener);
		return create(config);
	}

	/**
	 * 创建数据库
	 * @param context
	 *            上下文
	 * @param targetDirectory
	 *            db文件路径，可以配置为sdcard的路径
	 * @param dbName
	 *            数据库名字
	 * @param isDebug
	 *            是否是调试模式：调试模式会log出sql信息
	 * @param dbVersion
	 *            数据库版本信息
	 * @param dbUpdateListener 数据库升级监听器
	 *            ：如果监听器为null，升级的时候将会清空所所有的数据
	 * @return
	 */
	public static QkDb create(Context context, String targetDirectory,
							  String dbName, boolean isDebug, int dbVersion,
							  DbUpdateListener dbUpdateListener) {
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setTargetDirectory(targetDirectory);
		config.setDbName(dbName);
		config.setDebug(isDebug);
		config.setDbVersion(dbVersion);
		config.setDbUpdateListener(dbUpdateListener);
		return create(config);
	}

	/**
	 * 创建数据库
	 *
	 * @param daoConfig
	 * @return
	 */
	public static QkDb create(DaoConfig daoConfig) {
		return getInstance(daoConfig);
	}

	/**
	 * 先检查是否有这张表,如果没有表先创建表 再插入数据
	 * @param entity 表对应的对像
	 */
	public void save(Object entity) {
		checkTableExist(entity.getClass());
		synchronized (entity) {
			exeSqlInfo(SqlBuilder.buildInsertSql(entity));
		}

	}

	/**
	 * 保存数据到数据库<br />
	 * <b>注意：</b><br />
	 * 保存成功后，entity的主键将被赋值（或更新）为数据库的主键， 只针对自增长的id有效
	 *
	 * @param entity
	 *            要保存的数据
	 * @return ture： 保存成功 false:保存失败
	 */
	public boolean saveBindId(Object entity) {
		this.checkTableExist(entity.getClass());
		List entityKvList = SqlBuilder.getSaveKeyValueListByEntity(entity);
		if(entityKvList != null && entityKvList.size() > 0) {
			TableInfo tf = TableInfo.get(entity.getClass());
			ContentValues cv = new ContentValues();
			this.insertContentValues(entityKvList, cv);
			Long id = Long.valueOf(this.db.insert(tf.getTableName(), (String)null, cv));
			if(id.longValue() == -1L) {
				return false;
			} else {
				tf.getId().setValue(entity, id);
				return true;
			}
		} else {
			return false;
		}

	}

	/**
	 * 把List<KeyValue>数据存储到ContentValues
	 *
	 * @param list
	 * @param cv
	 */
	private void insertContentValues(List<KeyValue> list, ContentValues cv) {
		if (list != null && cv != null) {
			for (KeyValue kv : list) {
				cv.put(kv.getKey(), kv.getValue().toString());
			}
		} else {
			Log.w(TAG,
					"insertContentValues: List<KeyValue> is empty or ContentValues is empty!");
		}

	}

	/**
	 * 更新数据 （主键ID必须不能为空）
	 *
	 * @param entity  表对应的对像
	 */
	public void update(Object entity) {
		checkTableExist(entity.getClass());
		synchronized (entity) {
			exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity));
		}
	}

	/**
	 *批处理更新
	 * @param list 对像list
	 * @param clazz 对像类
	 */
	public <T>  void batchUpdate(List<T> list,Class<T> clazz) {
		checkTableExist(clazz);
		synchronized (clazz) {
			db.beginTransaction();
			try {
				for (Object entity:list) {
					exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity));
				}
				// 设置事务标志为成功，当结束事务时就会提交事务
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 结束事务
				db.endTransaction();
			}

		}
	}


	/**
	 *批处理保存
	 * @param list 对像list
	 * @param clazz 对像类
	 */
	public <T>  void batchSave(List<T> list,Class<T> clazz) {
		checkTableExist(clazz);
		synchronized (clazz) {
			db.beginTransaction();
			try {
				for (Object entity:list) {
					exeSqlInfo(SqlBuilder.buildInsertSql(entity));
				}
				// 设置事务标志为成功，当结束事务时就会提交事务
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 结束事务
				db.endTransaction();
			}
		}


	}
	/**
	 *批处理删除
	 * @param list 对像list
	 * @param clazz 对像类
	 */
	public <T>  void batchDelete(List<T> list,Class<T> clazz) {
		checkTableExist(clazz);
		synchronized (clazz) {
			db.beginTransaction();
			try {
				for (Object entity:list) {
					exeSqlInfo(SqlBuilder.buildDeleteSql(entity));
				}
				// 设置事务标志为成功，当结束事务时就会提交事务
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 结束事务
				db.endTransaction();
			}
		}


	}

	/**
	 * 根据id批处理删除数据
	 * @param list
	 * @param clazz 对像类
	 */
	public void batchDeletById(List<Integer> list,Class<?> clazz) {
		checkTableExist(clazz);
		synchronized (clazz) {
			db.beginTransaction();
			try {
				for (int id:list) {
					exeSqlInfo(SqlBuilder.buildDeleteSql(clazz, id));
				}
				// 设置事务标志为成功，当结束事务时就会提交事务
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 结束事务
				db.endTransaction();
			}
		}

	}

	/**
	 * 根据条件删除数据
	 * @param map 条件集合
	 * @param clazz 对像类
	 */
	public void deletByWhere(Map<String,String> map,Class<?> clazz) {
		checkTableExist(clazz);
		synchronized (clazz) {
			exeSqlInfo(SqlBuilder.buildDeleteSqlByMap(clazz, map));
		}

	}



	/**
	 * 根据条件更新数据
	 *
	 * @param entity 表对应的对像
	 * @param strWhere
	 *            条件为空的时候，将会更新所有的数据
	 */
	public void update(Object entity, String strWhere) {
		checkTableExist(entity.getClass());
		synchronized (entity) {
			exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity, strWhere));
		}

	}

	/**
	 * 删除数据
	 *
	 * @param entity
	 *            表对应的对像
	 */
	public void delete(Object entity) {
		checkTableExist(entity.getClass());
		synchronized (entity) {
			exeSqlInfo(SqlBuilder.buildDeleteSql(entity));
		}

	}

	/**
	 * 根据主键删除数据
	 *
	 * @param clazz
	 *            表对应的对像
	 * @param id
	 *            主键值
	 */
	public void deleteById(Class<?> clazz, Object id) {
		checkTableExist(clazz);
		synchronized (clazz) {
			exeSqlInfo(SqlBuilder.buildDeleteSql(clazz, id));
		}

	}

	/**
	 * 根据条件删除数据
	 * @param clazz 表对应的对像
	 * @param strWhere
	 *            条件为空的时候 将会删除所有的数据
	 */
	public void deleteByWhere(Class<?> clazz, String strWhere) {
		this.checkTableExist(clazz);
		String sql = SqlBuilder.buildDeleteSql(clazz, strWhere);
		this.debugSql(sql);
		this.db.execSQL(sql);

	}

	/**
	 * 删除表的所有数据
	 * @param clazz 表对应的对像
	 */
	public void deleteAll(Class<?> clazz) {
		this.checkTableExist(clazz);
		String sql = SqlBuilder.buildDeleteSql(clazz, (String)null);
		this.debugSql(sql);
		this.db.execSQL(sql);

	}

	/**
	 * 删除指定的表
	 * @param clazz 表对应的对像
	 */
	public void dropTable(Class<?> clazz) {
		this.checkTableExist(clazz);
		TableInfo table = TableInfo.get(clazz);
		String sql = "DROP TABLE " + table.getTableName();
		this.debugSql(sql);
		this.db.execSQL(sql);
	}

	/**
	 * 删除所有数据表
	 */
	public void dropDb() {
		Cursor cursor = this.db.rawQuery("SELECT name FROM sqlite_master WHERE type =\'table\' AND name != \'sqlite_sequence\'", (String[])null);
		if(cursor != null) {
			while(cursor.moveToNext()) {
				this.db.execSQL("DROP TABLE " + cursor.getString(0));
			}
		}

		if(cursor != null) {
			cursor.close();
			cursor = null;
		}
	}

	/**
	 * 执行sql语句
	 * @param sqlInfo sql对像
	 */
	private void exeSqlInfo(SqlInfo sqlInfo) {
		if(sqlInfo != null) {
			this.debugSql(sqlInfo.getSql());
			this.db.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
		} else {
			Log.e("FinalDb", "sava error:sqlInfo is null");
		}

	}

	/**
	 * 查找符合条件个数
	 * @param clazz 表对像
     * @return 个数
     */
	public <T> int  getCount(Class<T> clazz, Map<String,String> map) {
		int count = -1;
		Cursor cursor = this.db.rawQuery(SqlBuilder.getSelectSqlInfo(clazz,map).getSql(),null);
		if(cursor != null) {
			if(cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}

		if(cursor != null) {
			cursor.close();
			cursor = null;
		}
		return count;

	}





	/**
	 * 根据主键查找数据
	 *
	 * @param id 主键id
	 * @param clazz 表对应的对像
	 * @return 符合条件值对像列表
	 */
	public <T> T findById(Object id, Class<T> clazz) {
		checkTableExist(clazz);
		synchronized (clazz) {
			SqlInfo sqlInfo = SqlBuilder.getSelectSqlAsSqlInfo(clazz, id);
			if (sqlInfo != null) {
				debugSql(sqlInfo.getSql());
				Cursor cursor = this.db.rawQuery(sqlInfo.getSql(), sqlInfo.getBindArgsAsStringArray());
				try {
					if (cursor.moveToNext()) {
						return CursorUtils.getEntity(cursor, clazz, this);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (cursor != null) {
						cursor.close();
						cursor = null;
					}
				}
			}
			return null;
		}

	}



	/**
	 * 查找所有的数据
	 * @param clazz 表对应的对像
	 * @return 对像列表
	 */
	public <T> List<T> findAll(Class<T> clazz) {
		checkTableExist(clazz);
		synchronized (clazz) {
			return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz));
		}

	}

	/**
	 * 排序查找所有数据
	 *
	 * @param clazz 表对应的对像
	 * @param orderBy 排序的字段
	 * @return 对像列表
	 */
	public <T> List<T> findAll(Class<T> clazz, String orderBy) {
		checkTableExist(clazz);
		synchronized (clazz) {
			return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz)
					+ " ORDER BY " + orderBy);
		}

	}



	/**
	 * 根据条件查找所有数据
	 *
	 * @param clazz 表对应的对像
	 * @param strWhere 条件为空的时候查找所有数据
	 * @return 符合条件值对像列表
	 */
	public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere) {
		checkTableExist(clazz);
		synchronized (clazz) {
			return findAllBySql(clazz,
					SqlBuilder.getSelectSQLByWhere(clazz, strWhere));
		}

	}

	/**
	 * 根据条件查找所有数据
	 *
	 * @param clazz 表对应的对像
	 * @param map 条件集合
	 * @return 符合条件值对像列表
	 */
	public <T> List<T> findAllByWhere(Class<T> clazz, Map<String,String> map) {
		checkTableExist(clazz);
		synchronized (clazz) {
			SqlInfo sqlInfo = SqlBuilder.getSelectSqlInfo(clazz, map);
			return findAllBySql(clazz,sqlInfo.getSql(),sqlInfo.getWheres());
		}

	}

	/**
	 * 分页查询
	 * @param clazz 表对应的对像
	 * @param map 条件集合
	 * @param offset 页码
	 * @param pageSize 每页显示多少条
	 * @return 符合条件值对像列表
	 */
	public <T> List<T> pagingQueryByWhere(Class<T> clazz, Map<String,String> map,int offset,int pageSize) {
		checkTableExist(clazz);
		synchronized (clazz) {
			SqlInfo sqlInfo = SqlBuilder.getSelectSqlInfo(clazz, map);
			return findAllBySql(clazz,sqlInfo.getSql()+" Limit "+String.valueOf(pageSize)+ " Offset " +String.valueOf(offset*pageSize),sqlInfo.getWheres());
		}

	}

	/**
	 * 分页模糊查询
	 * @param clazz 表对应的对像
	 * @param map 条件集合
	 * @param offset 页码
	 * @param pageSize 每页显示多少条
	 * @return 符合条件值对像列表
	 */
	public <T> List<T> pagingLikeQueryByWhere(Class<T> clazz, Map<String,String> map,int offset,int pageSize) {
		checkTableExist(clazz);
		synchronized (clazz) {
			SqlInfo sqlInfo = SqlBuilder.getLikeSelectSqlInfo(clazz, map);
			return findAllBySql(clazz,sqlInfo.getSql()+" Limit "+String.valueOf(pageSize)+ " Offset " +String.valueOf(offset*pageSize),sqlInfo.getWheres());
		}
	}

	/**
	 * 根据条件查找所有数据
	 *
	 * @param clazz 表对应的对像
	 * @param strWhere	条件为空的时候查找所有数据
	 * @param orderBy	 排序字段
	 * @return 符合条件值对像列表
	 */
	public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere,
									  String orderBy) {
		checkTableExist(clazz);
		synchronized (clazz) {
			return findAllBySql(clazz,
					SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
							+ orderBy);
		}
	}

	/**
	 * 根据条件查找所有数据
	 * @param clazz 表对应的对像
	 * @param strSQL 查询sql语句
	 * @return 符合条件值对像列表
	 */
	private <T> List<T> findAllBySql(Class<T> clazz, String strSQL) {
		checkTableExist(clazz);
		synchronized (clazz) {
			debugSql(strSQL);
			Cursor cursor = db.rawQuery(strSQL, null);
			try {
				List<T> list = new ArrayList<T>();
				while (cursor.moveToNext()) {
					T t = CursorUtils.getEntity(cursor, clazz, this);
					list.add(t);
				}
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
				cursor = null;
			}
			return null;
		}

	}

	/**
	 * 根据条件查找数据
	 * @param clazz 表对应的对像
	 * @param strSQL 查询sql语句
	 * @param wheres 条件数组值
	 * @return 符合条件值对像列表
	 */
	private <T> List<T> findAllBySql(Class<T> clazz, String strSQL,String[] wheres) {
		checkTableExist(clazz);
		synchronized (clazz) {
			debugSql(strSQL);
			Cursor cursor = db.rawQuery(strSQL, wheres);
			try {
				List<T> list = new ArrayList<T>();
				while (cursor.moveToNext()) {
					T t = CursorUtils.getEntity(cursor, clazz, this);
					list.add(t);
				}
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
				cursor = null;
			}
			return null;
		}

	}

	/**
	 * 检查表是否存在,如果不存在就创建表
	 * @param clazz 表对应的对像
	 */
	private void checkTableExist(Class<?> clazz) {
		/**
		 * 加上同步锁 并用完就关闭数据库 防止报锁异常
		 */
		synchronized (clazz) {
			if (!tableIsExist(TableInfo.get(clazz),db)) {
				/**
				 * 表不存 创建表
				 */
				String sql = SqlBuilder.getCreatTableSQL(clazz);
				debugSql(sql);
				db.execSQL(sql);
			}
		}

	}

	/**
	 * 检查表是否存在
	 * @param table
	 * @return
	 */
	private boolean tableIsExist(TableInfo table,SQLiteDatabase db) {
		synchronized (db) {
			if (table.isCheckDatabese())
				return true;
			Cursor cursor = null;
			try {
				String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='"
						+ table.getTableName() + "' ";
				debugSql(sql);
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToNext()) {
					int count = cursor.getInt(0);
					if (count > 0) {
						table.setCheckDatabese(true);
						return true;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
				cursor = null;
			}

			return false;
		}

	}

	/**
	 * 打印sql语句
	 * @param sql
	 */
	private void debugSql(String sql) {
		if (config != null && config.isDebug())
			Log.d("Debug SQL", ">>>>>>  " + sql);
	}

	/**
	 * 数据库配置
	 */
	public static class DaoConfig {
		private Context mContext = null; // android上下文
		private String mDbName = "afinal.db"; // 数据库名字
		private int dbVersion = 1; // 数据库版本
		private boolean debug = true; // 是否是调试模式（调试模式 增删改查的时候显示SQL语句）
		private DbUpdateListener dbUpdateListener;
		// private boolean saveOnSDCard = false;//是否保存到SD卡
		private String targetDirectory;// 数据库文件在sd卡中的目录

		public Context getContext() {
			return mContext;
		}

		public void setContext(Context context) {
			this.mContext = context;
		}

		public String getDbName() {
			return mDbName;
		}

		public void setDbName(String dbName) {
			this.mDbName = dbName;
		}

		public int getDbVersion() {
			return dbVersion;
		}

		public void setDbVersion(int dbVersion) {
			this.dbVersion = dbVersion;
		}

		public boolean isDebug() {
			return debug;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
		}

		public DbUpdateListener getDbUpdateListener() {
			return dbUpdateListener;
		}

		public void setDbUpdateListener(DbUpdateListener dbUpdateListener) {
			this.dbUpdateListener = dbUpdateListener;
		}

		// public boolean isSaveOnSDCard() {
		// return saveOnSDCard;
		// }
		//
		// public void setSaveOnSDCard(boolean saveOnSDCard) {
		// this.saveOnSDCard = saveOnSDCard;
		// }

		public String getTargetDirectory() {
			return targetDirectory;
		}

		public void setTargetDirectory(String targetDirectory) {
			this.targetDirectory = targetDirectory;
		}
	}

	/**
	 * 在SD卡的指定目录上创建文件
	 *
	 * @param sdcardPath
	 * @param dbfilename
	 * @return
	 */
	private SQLiteDatabase createDbFileOnSDCard(String sdcardPath,
												String dbfilename) {
		File dbf = new File(sdcardPath, dbfilename);
		if (!dbf.exists()) {
			try {
				if (dbf.createNewFile()) {
					return SQLiteDatabase.openOrCreateDatabase(dbf, null);
				}
			} catch (IOException ioex) {
				throw new DbException("数据库文件创建失败", ioex);
			}
		} else {
			return SQLiteDatabase.openOrCreateDatabase(dbf, null);
		}

		return null;
	}

	class SqliteDbHelper extends SQLiteOpenHelper {

		private DbUpdateListener mDbUpdateListener;

		public SqliteDbHelper(Context context, String name, int version,
							  DbUpdateListener dbUpdateListener) {
			super(context, name, null, version);
			this.mDbUpdateListener = dbUpdateListener;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (mDbUpdateListener != null) {
				mDbUpdateListener.onUpgrade(db, oldVersion, newVersion);
			} else { // 清空所有的数据信息
				dropDb();
			}
		}

	}

}
