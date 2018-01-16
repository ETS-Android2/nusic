/**
 * Copyright (C) 2013 Johannes Schnatterer
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
 *
 * nusic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with nusic.  If not, see <http://www.gnu.org/licenses/>.
 */
package info.schnatterer.nusic.core.impl;

import info.schnatterer.nusic.core.DeviceMusicService;
import info.schnatterer.nusic.core.ServiceException;
import info.schnatterer.nusic.core.i18n.CoreMessageKey;
import info.schnatterer.nusic.data.model.Artist;

import javax.inject.Inject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.ArtistColumns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides access to music stored on the device via android's APIs.
 *
 * @author schnatterer
 *
 */
public class DeviceMusicServiceAndroid implements DeviceMusicService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceMusicServiceAndroid.class);

    @Inject
    private Context context;
    private static final String ARTIST_SORT_ORDER = Audio.Artists.DEFAULT_SORT_ORDER;
    private static final String[] ARTIST_PROJECTION = ArtistProjection
            .getArtistProjection();
    private static final Uri ARTIST_URI = Audio.Artists.EXTERNAL_CONTENT_URI;

    @Override
    public Artist[] getArtists() throws ServiceException {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        Artist[] artists = null;
        try {

            cursor = contentResolver.query(ARTIST_URI, ARTIST_PROJECTION, null,
                    null, ARTIST_SORT_ORDER);
            if (cursor != null) {
                artists = new Artist[cursor.getCount()];
                int i = 0;
                while (cursor.moveToNext()) {
                    Artist artist = new Artist(cursor.getLong(ArtistProjection.ID.getIndex()));
                    artist.setArtistName(cursor.getString(ArtistProjection.ARTIST.getIndex()));
                    artists[i++] = artist;
                }
            }
        } catch (Exception e) {
            throw new AndroidServiceException(
                    CoreMessageKey.ERROR_LOADING_ARTISTS, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        LOG.info("artists={}", artists);
        return artists;
        //return new Artist[] { createArtist(316, "Radiohead") };
    }

    private Artist createArtist(int androidAudioArtistId, String artistName) {
        Artist artist = new Artist(Long.valueOf(androidAudioArtistId));
        artist.setArtistName(artistName);
        return artist;
    }


    public enum ArtistProjection {
        ID(0, BaseColumns._ID), ARTIST(1, ArtistColumns.ARTIST);
        private static final String[] artistProjection = { ID.getColumnKey(),
                ARTIST.getColumnKey() };
        private int index;
        private String columnKey;

        private ArtistProjection(int index, String columnKey) {
            this.setIndex(index);
            this.setColumnKey(columnKey);
        }

        public int getIndex() {
            return index;
        }

        public String getColumnKey() {
            return columnKey;
        }

        private void setIndex(int index) {
            this.index = index;
        }

        private void setColumnKey(String columnKey) {
            this.columnKey = columnKey;
        }

        public static String[] getArtistProjection() {
            return artistProjection;
        }
    }
}
