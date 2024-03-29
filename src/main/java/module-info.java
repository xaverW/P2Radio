module p2radio {
    opens de.p2tools.p2radio;
    exports de.p2tools.p2radio;

    opens de.p2tools.p2radio.controller.data.station;
    opens de.p2tools.p2radio.controller.data;

    requires de.p2tools.p2lib;
    requires javafx.controls;
    requires org.controlsfx.controls;

    requires java.logging;
    requires java.desktop;

    requires commons.cli;
    requires com.fasterxml.jackson.core;
    requires org.tukaani.xz;

    requires okhttp3;
    requires org.apache.commons.io;
}

