package info.schnatterer.newsic.service.impl;

import info.schnatterer.newsic.R;
import info.schnatterer.newsic.db.model.Artist;
import info.schnatterer.newsic.db.model.Release;
import info.schnatterer.newsic.service.QueryMusicMetadataService;
import info.schnatterer.newsic.service.ServiceException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.model.entity.ReleaseWs2;
import org.musicbrainz.model.searchresult.ReleaseResultWs2;

import android.annotation.SuppressLint;

public class QueryMusicMetadataServiceMusicBrainz implements QueryMusicMetadataService {
	/**
	 * See http://musicbrainz.org/doc/Development/XML_Web_Service/Version_2#
	 * Release_Type_and_Status
	 */
	private static final String SEARCH_BASE = "type:album";
	private static final String SEARCH_DATE_1 = " AND date:[";
	private static final String SEARCH_DATE_2 = " TO ????-??-??]";
	private static final String SEARCH_ARTIST_1 = " AND artist:\"";
	private static final String SEARCH_ARTIST_2 = "\"";

	@SuppressLint("SimpleDateFormat")
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Artist findReleases(Artist artist, Date fromDate)
			throws ServiceException {
		if (artist == null || artist.getArtistName() == null) {
			return null;
		}
		String artistName = artist.getArtistName();
		Map<String, Release> releases = new HashMap<String, Release>();
		try {
			// List<ReleaseResultWs2> releaseResults = findReleases();
			org.musicbrainz.controller.Release releaseSearch = createReleaseSearch();
			releaseSearch.search(new StringBuffer(SEARCH_BASE)
					.append(SEARCH_DATE_1).append(dateFormat.format(fromDate))
					.append(SEARCH_DATE_2).append(SEARCH_ARTIST_1)
					.append(artistName).append(SEARCH_ARTIST_2).toString());
			processReleaseResults(artistName, artist, releases,
					releaseSearch.getFirstSearchResultPage());

			while (releaseSearch.hasMore()) {
				// TODO check if internet connection still there?
				processReleaseResults(artistName, artist, releases,
						releaseSearch.getNextSearchResultPage());
			}
		} catch (MBWS2Exception mBWS2Exception) {
			throw new ServiceException(
					R.string.QueryMusicMetadataService_errorQueryingMusicBrainz,
					mBWS2Exception, artistName);
		} catch (SecurityException securityException) {
			throw securityException;
		} catch (Throwable t) {
			throw new ServiceException(
					R.string.QueryMusicMetadataService_errorFindingReleasesArtist, t,
					artistName);
		}
		return artist;
	}

	protected org.musicbrainz.controller.Release createReleaseSearch() {
		return new org.musicbrainz.controller.Release();
	}

	protected void processReleaseResults(String artistName, Artist artist,
			Map<String, Release> releases, List<ReleaseResultWs2> releaseResults) {
		for (ReleaseResultWs2 releaseResultWs2 : releaseResults) {
			// Make sure not to add other artists albums
			ReleaseWs2 releaseResult = releaseResultWs2.getRelease();
			if (releaseResult.getArtistCredit().getArtistCreditString()
					.equals(artistName)) {
				// Use only the release with the "oldest" date of a release
				// group
				Release existingRelease = releases.get(releaseResult.getTitle()
						.trim());
				Date newDate = releaseResult.getDate();
				if (existingRelease == null) {
					Release release = new Release();
					release.setArtist(artist);
					release.setReleaseName(releaseResult.getTitle());
					release.setReleaseDate(newDate);
					release.setMusicBrainzId(releaseResult.getId());

					releases.put(releaseResult.getTitle().trim(), release);
					artist.getReleases().add(release);
					// TODO store all release dates and their countries?
				} else if (existingRelease.getReleaseDate().after(newDate)) {
					// Change date of existing release
					existingRelease.setReleaseDate(newDate);
				}
			}
		}

	}

	// /**
	// * Calls MusicBrainz API.
	// *
	// * This search walks over the release page by page, returning all
	// releases.
	// * It might return different artist with similar name. It also returns
	// * different release of the same name (i.e. releases of the same release
	// * group in different countries). <b>Note: It is more effective to process
	// * the releases page by page</b>
	// *
	// * @param searchText
	// * @return
	// * @throws ServiceException
	// */
	// protected List<ReleaseResultWs2> findReleases(String searchText) throws
	// ServiceException {
	// org.musicbrainz.controller.Release releaseSearch = new
	// org.musicbrainz.controller.Release();
	// releaseSearch.search(searchText);
	// List<ReleaseResultWs2> releaseResults = null;
	// try {
	// releaseResults = releaseSearch.getFullSearchResultList();
	// // while (releaseSearch.hasMore()) {
	// // releaseResults.addAll(releaseSearch.getNextSearchResultPage());
	// // }
	// } catch (MBWS2Exception e) {
	// throw new ServiceException(
	// R.string.QueryMusicMetadataService_errorQueryingMusicBrainz, e);
	// }
	//
	// return releaseResults;
	// }
}