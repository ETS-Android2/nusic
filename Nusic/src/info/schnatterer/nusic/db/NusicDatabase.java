/* Copyright (C) 2013 Johannes Schnatterer
 * 
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *  
 * This file is part of nusic.
 * 
 * nusic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * nusic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with nusic.  If not, see <http://www.gnu.org/licenses/>.
 */
package info.schnatterer.nusic.db;

import info.schnatterer.nusic.Application;
import info.schnatterer.nusic.Constants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Basic database abstraction. Handles execution of DDL scripts.
 * 
 * @author schnatterer
 * 
 */
public class NusicDatabase extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "nusic";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "CREATE TABLE ";

	private static final String DATABASE_FOREIGN_KEY = ", FOREIGN KEY(";
	private static final String DATABASE_REFERENCES = ") REFERENCES ";

	/* Table Artist */
	public static final String TABLE_ARTIST = "artist";

	public static final String COLUMN_ARTIST_ID = BaseColumns._ID;
	public static final String TYPE_COLUMN_ARTIST_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
	public static final int INDEX_COLUMN_ARTIST_ID = 0;

	public static final String COLUMN_ARTIST_ANDROID_ID = "androidId";
	public static final String TYPE_COLUMN_ARTIST_ANDROID_ID = "INTEGER";
	public static final int INDEX_COLUMN_ARTIST_ANDROID_ID = 1;

	public static final String COLUMN_ARTIST_MB_ID = "mbId";
	public static final String TYPE_COLUMN_ARTIST_MB_ID = "STRING";
	public static final int INDEX_COLUMN_ARTIST_MB_ID = 2;

	public static final String COLUMN_ARTIST_NAME = "name";
	public static final String TYPE_COLUMN_ARTIST_NAME = "TEXT NOT NULL";
	public static final int INDEX_COLUMN_ARTIST_NAME = 3;

	public static final String COLUMN_ARTIST_DATE_CREATED = "dateCreated";
	public static final String TYPE_COLUMN_ARTIST_DATE_CREATED = "INTEGER NOT NULL";
	public static final int INDEX_COLUMN_ARTIST_DATE_CREATED = 4;

	/* Table Releases */
	public static final String TABLE_RELEASE = "release";

	public static final String COLUMN_RELEASE_ID = BaseColumns._ID;
	public static final String TYPE_COLUMN_RELEASE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
	public static final int INDEX_COLUMN_RELEASE_ID = 0;

	public static final String COLUMN_RELEASE_MB_ID = "mbId";
	public static final String TYPE_COLUMN_RELEASE_MB_ID = "INTEGER";
	public static final int INDEX_COLUMN_RELEASE_MB_ID = 1;

	public static final String COLUMN_RELEASE_NAME = "name";
	public static final String TYPE_COLUMN_RELEASE_NAME = "TEXT NOT NULL";
	public static final int INDEX_COLUMN_RELEASE_NAME = 2;

	public static final String COLUMN_RELEASE_DATE_RELEASED = "dateReleased";
	public static final String TYPE_COLUMN_RELEASE_DATE_RELEASED = "INTEGER";
	public static final int INDEX_COLUMN_RELEASE_DATE_RELEASED = 3;

	public static final String COLUMN_RELEASE_DATE_CREATED = "dateCreated";
	public static final String TYPE_COLUMN_RELEASE_DATE_CREATED = "INTEGER NOT NULL";
	public static final int INDEX_COLUMN_RELEASE_DATE_CREATED = 4;

	public static final String COLUMN_RELEASE_ARTWORK_PATH = "artworkPath";
	public static final String TYPE_COLUMN_RELEASE_ARTWORK_PATH = "TEXT";
	public static final int INDEX_COLUMN_RELEASE_ARTWORK_PATH = 5;

	public static final String COLUMN_RELEASE_FK_ID_ARTIST = "fkIdArtist";
	public static final String TYPE_COLUMN_RELEASE_FK_ID_ARTIST = "INTEGER";
	public static final String TYPE_COLUMN_RELEASE_FK_ID_ARTIST_FK = TYPE_COLUMN_RELEASE_FK_ID_ARTIST
			+ DATABASE_FOREIGN_KEY
			+ COLUMN_RELEASE_FK_ID_ARTIST
			+ DATABASE_REFERENCES + TABLE_ARTIST + "(" + COLUMN_ARTIST_ID + ")";
	public static final int INDEX_COLUMN_RELEASE_FK_ID_ARTIST = 6;

	public static final String[] TABLE_RELEASE_COLUMNS = { COLUMN_RELEASE_ID,
			COLUMN_RELEASE_MB_ID, COLUMN_RELEASE_NAME,
			COLUMN_RELEASE_DATE_RELEASED, COLUMN_RELEASE_DATE_CREATED,
			COLUMN_RELEASE_ARTWORK_PATH, COLUMN_RELEASE_FK_ID_ARTIST };

	private static final NusicDatabase instance = new NusicDatabase(
			Application.getContext());

	protected NusicDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static NusicDatabase getInstance() {
		return instance;
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	@Override
	public synchronized void close() {
		Log.d(Constants.LOG, "Closing database");
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTable(TABLE_ARTIST, COLUMN_ARTIST_ID,
				TYPE_COLUMN_ARTIST_ID, COLUMN_ARTIST_ANDROID_ID,
				TYPE_COLUMN_ARTIST_ANDROID_ID, COLUMN_ARTIST_MB_ID,
				TYPE_COLUMN_ARTIST_MB_ID, COLUMN_ARTIST_NAME,
				TYPE_COLUMN_ARTIST_NAME, COLUMN_ARTIST_DATE_CREATED,
				TYPE_COLUMN_ARTIST_DATE_CREATED));
		db.execSQL(createTable(TABLE_RELEASE, COLUMN_RELEASE_ID,
				TYPE_COLUMN_RELEASE_ID, COLUMN_RELEASE_MB_ID,
				TYPE_COLUMN_RELEASE_MB_ID, COLUMN_RELEASE_NAME,
				TYPE_COLUMN_RELEASE_NAME, COLUMN_RELEASE_DATE_RELEASED,
				TYPE_COLUMN_RELEASE_DATE_RELEASED, COLUMN_RELEASE_DATE_CREATED,
				TYPE_COLUMN_RELEASE_DATE_CREATED, COLUMN_RELEASE_ARTWORK_PATH,
				TYPE_COLUMN_RELEASE_ARTWORK_PATH, COLUMN_RELEASE_FK_ID_ARTIST,
				TYPE_COLUMN_RELEASE_FK_ID_ARTIST_FK));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Nothing yet
	}

	/**
	 * Facilitates creation of a createTable String
	 * 
	 * @param tableName
	 *            the table name
	 * @param columnsAndTypeTuples
	 *            a multiple of two (at least two) containing the column name
	 *            and the type (can contain additional constraints)
	 * @return an SQL string that creates the table
	 */
	protected String createTable(String tableName,
			String... columnsAndTypeTuples) {
		StringBuffer sql = new StringBuffer(DATABASE_CREATE).append(tableName)
				.append("(");
		int index = 0;
		sql.append(columnsAndTypeTuples[index++]).append(" ")
				.append(columnsAndTypeTuples[index++]);
		while (index < columnsAndTypeTuples.length) {
			sql.append(", ");
			sql.append(columnsAndTypeTuples[index++]).append(" ")
					.append(columnsAndTypeTuples[index++]);
		}
		sql.append(");");
		return sql.toString();
	}
}
