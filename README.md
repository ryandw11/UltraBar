# UltraBar
UltraBar is a minecraft plugin that allows server admins to creation Boss Bars and use them as messages. It also allows the creation of titles and actionbars.  
  
Ultra Bar can be downloaded from [SpigotMC](https://www.spigotmc.org/resources/ultra-bar.20113/).  

# Images
![Example image from ingame](http://i.imgur.com/0YhxrJu.gif)
![Example typing in the command](https://i.imgur.com/k4wgWra.gif)

# Developer API
Ultra Bar has an expansive developer api to make it easier for developers to create meaningful boss bars while taking advantage of ultra bar's features.  
[The Javadocs can be viewed here](https://ryandw11.github.io/UltraBar/)  
  
Maven:
```xml
<repositories>
    <repository>
        <id>Ryandw11-Releases</id>
        <url>https://repo.ryandw11.com/repository/maven-releases/</url>
    </repository>
</repositories>

<dependency>
  <groupId>me.ryandw11</groupId>
  <artifactId>UltraBar</artifactId>
  <version>{version}</version>
</dependency>
```
`{version}` is the current version of the plugin, which can be found on the [SpigotMC](https://www.spigotmc.org/resources/ultra-bar.20113/) page.

# Contributing
In order to contibute to the project you must have all of the spigot versions from `1.12.2-1.16.4`. You can do this by running buildtools multiple times:
```
java -jar BuildTools.jar --rev 1.16.4
java -jar BuildTools.jar --rev 1.16.3
java -jar BuildTools.jar --rev 1.16.2
java -jar BuildTools.jar --rev 1.16.1
java -jar BuildTools.jar --rev 1.15.2
java -jar BuildTools.jar --rev 1.14.4
java -jar BuildTools.jar --rev 1.13.2
java -jar BuildTools.jar --rev 1.12.2
```
Then run `mvn clean install` to get all of the required dependencies.  

To build UltraBar you can either use `package` or `install` to do so.
