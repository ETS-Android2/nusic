TODOs - Features, enhancements, refactoring, ...

Target API 27:
- Notifcation: Use of stream types is deprecated for operations other than volume control
- min API changed to 14 -> Remove dead code
- Shared Button No longer Works on physical phone. Works on emu?!
- Icons on physical phone with API 27 are never round when background is transparent image?!

- Drop Status Dialog an ServiceConnection?
- Drop Support < Android 4?



- Modernize UI: Use Snackbar instead of Toast
- Modernize UI: Provide Burger Menu for List entries --> Intuitive Way of hiding releases
- Drop Support < Android 4.0 (minSdkVersion 14), Clean Up Code

- Setup Github Pull Request Jenkins Plugin: https://www.theguild.nl/building-github-pull-requests-with-jenkins
- Setup Master Build in Jenkins with SonarQube
- No longer check artists that were removed

- Rename "constants.xml" to "donottranslate.xml" (best practice: See http://tools.android.com/recent/non-translatablestrings)
- Download Artist Image? http://stackoverflow.com/questions/28458654/musicbrainz-artist-image-and-information
Or use placeholder image (just like in k9): http://stackoverflow.com/questions/23122088/colored-boxed-with-letters-a-la-gmail

- Use Transifex for translation? E.g. https://www.transifex.com/vanilla_music/vanilla-music-1/

- Gradle: Dont repeat yourself - Set common compileJava, android and sonarqube (for android)  options only once
See e.g https://discuss.gradle.org/t/how-do-i-detect-if-a-sub-project-has-applied-a-given-plugin-e-g-jar-or-war/4974

- Specify which files to backup in manifest: https://developer.android.com/training/backup/autosyncapi.html

- SonarQube
- Fix Javadoc warnings

- A new view that shows icon of the release and a link to MB on tap ? --> Remove webview

- Copy images & Logs to /sdcard instead of /data: 
http://www.androidsnippets.com/download-an-http-file-to-sdcard-with-progress-notification
http://developer.android.com/guide/topics/data/data-storage.html#filesExternal

LOGGING 
- Output statistic after finishing daily update
- Manage to grab Logcat output from android to to SLF4J

- Show progress in a snack bar instead of the dialog?
- Move launcher icons to mipmap: http://developer.android.com/guide/topics/resources/providing-resources.html

LIST
- circleimageview?
- Use recycler view for better performance in list view? https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html

- Show artwork in context menu (on long tab)

- Create internal structure diagram, displaying module dependencies with an appropriate open source tool
.....Build
- change digestalg?
- Restructure APK project to use maven default structure. Does this even work with eclipse?
- Insert Built timestamp to version
- Refactoring: Maven Use profiles for release (signing...)

- Covers: Download using HTTPS. Problem with certificate chain at coverartarchive.org ONLY on android. In addition, links to images are HTTP. Force using HTTPS?

- Implement an Artwork Entity using a "proxy" that takes care of the writing from/to FS?
- Replace today's date with "today"? How to refresh at midnight (http://stackoverflow.com/questions/4928570/need-to-know-when-its-a-new-day-i-e-when-the-time-is-000000)?
- Use WeakReferences in ReleaseRefreshService in order to allow longer time ranges

- Feature: Tablet optimization: Layout + Screens for 7" + 10"

.... Even more ideas
- Feature: Kind of releases: Album, Release, Live ...
- Feature: Check connectivity while refreshing and cancel with error when lost: http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
- Feature: Error reporter; Or a simpler workaround: Log errors to SD (microlog4android? http://stackoverflow.com/a/13479675/1845976)
- Feature: Show number of artists and releases, show date of last refresh (in status/statistic dialog?)
- Feature: Clean up DB, remove older entries
- Refactoring: Delete LoadNewServiceBinding and split its functionality to MainActivity and  ServiceConnection?
- Feature: Schedule frequence configurable via preferences (at the moment always once per day). When changed: Adapt interval.
- Feature: Preference Time period: infinitely. TODO Refactor refreshing release to decrease memory consumption.

.... Lower priority ideas
- Feature: Update only when sync is enabled: http://stackoverflow.com/questions/11252876/check-if-sync-is-enabled-within-android-app
(- Fix: Connect to Service and Start Dialog when Service started itself (e.g. after boot: wait for service to start, then tap refresh)) 
- Fix: After service was killed: Continue from last artists instead of starting new



