[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

# P2Radio

P2Radio ist ein Programm zum Online-Radio hören. Es verwendet die Senderliste mit Online-Radiosendern von: www.radio-browser.info und listet die gefundenen Sender auf. Die Liste kann mit verschiedenen Filtern nach Sendern durchsucht werden. Mit einem Programm eigener Wahl können die Sender dann abgespielt werden. Es lassen sich "Lieblingssender" in eigenen Sammlungen ablegen und so archivieren.

<br />

## Infos
Das Programm nutzt den Ordner ".p2Radio" unter Linux und den versteckten Ordner "p2Radio" unter Windows als
Konfig-Ordner. Man kann dem Programm auch einen Ordner für die Einstellungen mitgeben (und es z.B. auf einem USB-Stick
verwenden):

```
java -jar P2Radio.jar ORDNER
```

Weitere Infos über das Programm können auf der Website nachgelsesen werden.

https://www.p2tools.de/p2radio/

<br />

## Systemvoraussetzungen
Unterstützt wird Windows und Linux.

Das Programm benötigt unter Windows und Linux eine aktuelle Java-VM ab Version: Java 11. Für Linux-Benutzer wird
OpenJDK11 empfohlen. (FX-Runtime bringt das Programm bereits mit und muss nicht installiert werden).

<br />

## Download
Das Programm wird in drei Paketen angeboten. Diese unterscheiden sich nur im "Zubehör", das Programm selbst ist in allen
Paketen identisch:

- **P2Radio-XX.zip**  
  Das Programmpaket bringt nur das Programm und die benötigten "Hilfsprogramme" aber kein Java mit. Auf dem Rechner muss
  eine Java-Laufzeitumgebung ab Java11 installiert sein. Dieses Programmpaket kann auf allen Betriebssystemen verwendet
  werden. Es bringt Startdateien für Linux und Windows mit.

- **P2Radio-XX__Linux+Java.zip**  
  **P2Radio-XX__Win+Java.zip**  
  Diese Programmpakete bringen die Java-Laufzeitumgebung mit und sind nur für das angegebene Betriebssystem: Linux oder
  Windows. Es muss kein Java auf dem System installiert sein. (Die Java-Laufzeitumgebung liegt im Ordner "Java" und
  kommt von jdk.java.net).

zum Download: [github.com/xaverW/P2Radio/releases](https://github.com/xaverW/P2Radio/releases)

<br />

## Website
[www.p2tools.de/p2radio/]( https://www.p2tools.de/p2radio/)
