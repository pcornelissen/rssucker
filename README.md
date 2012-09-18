rssucker
========

Downloads episodes from configured streams

just call:
java -jar rssucker-1.0.jar

to configure you need to create/edit (it will be created if it doesn't exist) the file ~/.rssucker

#########################
```json
{
  "feeds" : {
    "Enter Description here" : {
      "address" : "http://enter.url/that/leads/to/desired/rss.xml/feed",
      "lastEpisode" : 0,
      "lastSeason" : 1
    },
	...
 },
  "downloaddir" : "/home/example/downloads"
}
```
#########################
The file get's rewritten on each run with the new episode and season.
The Downloaddir needs to be writable.
If you want rssucker to download all episodes including the first you can set the episode to 0.
The episode and season always describes the last downloaded file.

To build you need a JDK 7+ and maven 3.
Just call
   mvn package

The jar file will be located in the target directory. Copy it wherever you like.
