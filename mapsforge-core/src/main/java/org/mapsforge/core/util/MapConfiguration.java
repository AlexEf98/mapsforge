package org.mapsforge.core.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by ruinalef on 10/6/15.
 */
public class MapConfiguration {

    public static final String MERCATOR_PROJECTION = "mercator";
    public static final String GEO_PROJECTION = "geo";

    private int mapWidth;
    private int mapHeight;
    private int tileXCount;
    private int tileYCount;
    private double mapUpperLat;
    private double mapRightLon;
    private double mapBottomLat;
    private double mapLeftLon;
    private String projection;

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getTileXCount() {
        return tileXCount;
    }

    public int getTileYCount() {
        return tileYCount;
    }

    public double getMapUpperLat() {
        return mapUpperLat;
    }

    public double getMapRightLon() {
        return mapRightLon;
    }

    public double getMapBottomLat() {
        return mapBottomLat;
    }

    public double getMapLeftLon() {
        return mapLeftLon;
    }

    /**
     * @return returns width of the calibration rectangle in pixels
     */
    public int widthInPixels()
    {
        return mapWidth;
    }


    /**
     * @return returns height of the calibration rectangle in pixels
     */
    public int heightInPixels()
    {
        return mapHeight;
    }


    /**
     * @return Returns width of the calibration rectangle in degrees;
     */
    public double widthInDegrees()
    {
        return mapRightLon - mapLeftLon;
    }

    /**
     * @return Returns height of the calibration rectangle in degrees;
     */
    public double heightInDegrees()
    {
        return mapUpperLat - mapBottomLat;
    }

    public String getProjection() {
        return projection;
    }

    private static class Builder {
        private int mapWidth = 256;
        private int mapHeight = 256;
        private int tileXCount = 1;
        private int tileYCount = 1;
        private double mapUpperLat = 85.05112877980659;
        private double mapRightLon = 180;
        private double mapBottomLat = -85.05112877980659;
        private double mapLeftLon = -180;
        private String projection = MERCATOR_PROJECTION;

        public Builder setMapWidth(int mapWidth) {
            this.mapWidth = mapWidth;
            return this;
        }

        public Builder setMapHeight(int mapHeight) {
            this.mapHeight = mapHeight;
            return this;
        }

        public Builder setTileXCount(int tileXCount) {
            this.tileXCount = tileXCount;
            return this;
        }

        public Builder setTileYCount(int tileYCount) {
            this.tileYCount = tileYCount;
            return this;
        }

        public Builder setMapUpperLat(double mapUpperLat) {
            this.mapUpperLat = mapUpperLat;
            return this;
        }

        public Builder setMapRightLon(double mapRightLon) {
            this.mapRightLon = mapRightLon;
            return this;
        }

        public Builder setMapBottomLat(double mapBottomLat) {
            this.mapBottomLat = mapBottomLat;
            return this;
        }

        public Builder setMapLeftLon(double mapLeftLon) {
            this.mapLeftLon = mapLeftLon;
            return this;
        }

        public Builder setProjection(String projection) {
            this.projection = projection;
            return this;
        }

        public MapConfiguration build() {
            // TODO check that data is correct
            MapConfiguration config = new MapConfiguration();
            config.mapWidth = mapWidth;
            config.mapHeight = mapHeight;
            config.tileXCount = tileXCount;
            config.tileYCount = tileYCount;
            config.mapUpperLat = mapUpperLat;
            config.mapRightLon = mapRightLon;
            config.mapBottomLat = mapBottomLat;
            config.mapLeftLon = mapLeftLon;
            config.projection = projection;
            return config;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(MapConfiguration.class.getName());
    private static final String XPATH_EXPRESSION_MAP_WIDTH = "/map-config/@map-width";
    private static final String XPATH_EXPRESSION_MAP_HEIGHT = "/map-config/@map-height";
    private static final String XPATH_EXPRESSION_TILE_X_COUNT = "/map-config/@tile-x-count";
    private static final String XPATH_EXPRESSION_TILE_Y_COUNT = "/map-config/@tile-y-count";
    private static final String XPATH_EXPRESSION_MAP_UPPER_LAT = "/map-config/@map-upper-lat";
    private static final String XPATH_EXPRESSION_MAP_BOTTOM_LAT = "/map-config/@map-bottom-lat";
    private static final String XPATH_EXPRESSION_MAP_LEFT_LON = "/map-config/@map-left-lon";
    private static final String XPATH_EXPRESSION_MAP_RIGHT_LON = "/map-config/@map-right-lon";
    private static final String XPATH_EXPRESSION_PROJECTION = "/map-config/@projection";

    public static String getDefaultMapConfigString() {
        URL url = MapConfiguration.class.getClassLoader().getResource("map-config.xml");
        return getMapConfigString(url);
    }

    public static String getMapConfigString(URL url) {
        try {
            return readToString(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException("Cannot read map config" + url, e);
        }
    }

    private static String readToString(InputStream is)
            throws IOException {
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[8192];
            int read;
            while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, read);
            }
            return builder.toString();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }

    public static MapConfiguration getDefaultMapConfig() {
        InputStream is = MapConfiguration.class.getClassLoader().getResourceAsStream("map-config.xml");
        return getMapConfig(is);
    }

    public static MapConfiguration getMapConfig(String mapConfig) {
        InputStream is = new ByteArrayInputStream(mapConfig.getBytes(Charset.forName("utf8")));
        return getMapConfig(is);
    }

    private static MapConfiguration getMapConfig(InputStream is) {
        try {
            MapConfigXmlParser parser = new MapConfigXmlParser(is);
            MapConfiguration.Builder configBuilder = new MapConfiguration.Builder();
            configBuilder.setMapWidth(parser.getInt(XPATH_EXPRESSION_MAP_WIDTH))
                    .setMapHeight(parser.getInt(XPATH_EXPRESSION_MAP_HEIGHT))
                    .setTileXCount(parser.getInt(XPATH_EXPRESSION_TILE_X_COUNT))
                    .setTileYCount(parser.getInt(XPATH_EXPRESSION_TILE_Y_COUNT))
                    .setMapUpperLat(parser.getDouble(XPATH_EXPRESSION_MAP_UPPER_LAT))
                    .setMapBottomLat(parser.getDouble(XPATH_EXPRESSION_MAP_BOTTOM_LAT))
                    .setMapLeftLon(parser.getDouble(XPATH_EXPRESSION_MAP_LEFT_LON))
                    .setMapRightLon(parser.getDouble(XPATH_EXPRESSION_MAP_RIGHT_LON))
                    .setProjection(parser.getString(XPATH_EXPRESSION_PROJECTION));
            return configBuilder.build();
        } catch (SAXParseException spe) {
            LOGGER.severe("\n** Parsing error, line " + spe.getLineNumber() + ", uri " + spe.getSystemId());
            throw new IllegalStateException(spe);
        } catch (SAXException sxe) {
            throw new IllegalStateException(sxe);
        } catch (ParserConfigurationException pce) {
            throw new IllegalStateException(pce);
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static class MapConfigXmlParser {
        private Document mDocument;
        private XPath mXPath;
        MapConfigXmlParser(InputStream is) throws ParserConfigurationException, IOException, SAXException {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            mDocument = builder.parse(is);
            mXPath = XPathFactory.newInstance().newXPath();
        }

        public int getInt(String key) throws XPathExpressionException {
            XPathExpression xe = mXPath.compile(key);
            return Integer.parseInt((String) xe.evaluate(mDocument, XPathConstants.STRING));
        }

        public double getDouble(String key) throws XPathExpressionException {
            XPathExpression xe = mXPath.compile(key);
            return Double.parseDouble((String) xe.evaluate(mDocument, XPathConstants.STRING));
        }

        public String getString(String key) throws XPathExpressionException {
            XPathExpression xe = mXPath.compile(key);
            return (String)xe.evaluate(mDocument, XPathConstants.STRING);
        }
    }
}
