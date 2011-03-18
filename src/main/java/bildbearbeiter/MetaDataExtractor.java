package bildbearbeiter;

import org.apache.log4j.Logger;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Helper class to extract metadata from given images. This class uses Apache Sanslean classes
 * to perform the metadata extraction itself.
 */
public class MetaDataExtractor {
    final static private Logger LOG = Logger.getLogger(MetaDataExtractor.class);


    public static void metadataExample(File file) throws ImageReadException,
            IOException {
        //        get all metadata stored in EXIF format (ie. from JPEG or TIFF).
        //            org.w3c.dom.Node node = Sanselan.getMetadataObsolete(imageBytes);
        IImageMetadata metadata = Sanselan.getMetadata(file);

        if (metadata instanceof JpegImageMetadata) {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

            // Jpeg EXIF metadata is stored in a TIFF-based directory structure
            // and is identified with TIFF tags.
            // Here we look for the "x resolution" tag, but
            // we could just as easily search for any other tag.
            //
            // see the TiffConstants file for a list of TIFF tags.

            LOG.info("file: " + file.getPath());

            // print out various interesting EXIF tags.
            printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_XRESOLUTION);
            printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_DATE_TIME);
            printTagValue(jpegMetadata,
                    TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_CREATE_DATE);
            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_ISO);
            printTagValue(jpegMetadata,
                    TiffConstants.EXIF_TAG_SHUTTER_SPEED_VALUE);
            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_APERTURE_VALUE);
            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_BRIGHTNESS_VALUE);
            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LATITUDE);
            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LONGITUDE);

            // simple interface to GPS data
            TiffImageMetadata exifMetadata = jpegMetadata.getExif();
            if (null != exifMetadata) {
                TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                if (null != gpsInfo) {
                    String gpsDescription = gpsInfo.toString();
                    double longitude = gpsInfo.getLongitudeAsDegreesEast();
                    double latitude = gpsInfo.getLatitudeAsDegreesNorth();

                    LOG.info("    " + "GPS Description: " + gpsDescription);
                    LOG.info("    " + "GPS Longitude (Degrees East): " + longitude);
                    LOG.info("    " + "GPS Latitude (Degrees North): " + latitude);
                }
            }

            ArrayList items = jpegMetadata.getItems();

            for (Object item : items) {
                LOG.info("    " + "item: " + item);
            }
        }
    }

    private static void printTagValue(JpegImageMetadata jpegMetadata,
                                      TagInfo tagInfo) {
        TiffField field = jpegMetadata.findEXIFValue(tagInfo);
        if (field == null)
            LOG.info(tagInfo.name + ": " + "Not Found.");
        else
            LOG.info(tagInfo.name + ": "
                    + field.getValueDescription());
    }

    /**
     * Helper to extract the date this image was created to be used during the renaming process.
     *
     * @param image Image to extract metadata from.
     * @return the date this image was created if found, format is
     * @throws ParseException, ImageReadException, IOException - if an error occurs when accessing the image's metadata.
     */
    // TODO 2 methoden machen: 1 generische, die einen Metadatenteil zurückgibt, dann eine Methode, die das Datum richtig formatiert als String zurückgibt und nur ':' und ' ' entfernt
    public static String generateCreationDateInCorrectFormat(File image) throws ImageReadException,
            IOException, ParseException {
        IImageMetadata metadata = Sanselan.getMetadata(image);
        String dateValue = "";

        if (metadata instanceof JpegImageMetadata) {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

            TiffField field = jpegMetadata.findEXIFValue(TiffConstants.EXIF_TAG_CREATE_DATE);

            if (field != null) {
                LOG.info("Datumswert: " + field.getValueDescription());
                dateValue = field.getValueDescription();

                if (dateValue != null) {
                    assert dateValue.length() == 21 : "Invalid lenght of EXIF metadata, not complying to the standard";

                    // TODO check whether this can be done with a DateParser instead - after removing starting/trailing '-character
                    // replace special characters
                    dateValue = dateValue.replaceAll("'", "");
                    dateValue = dateValue.replaceAll(":", "");
                    dateValue = dateValue.replaceAll(" ", "_");
                    dateValue += "_";
                    //Datumswert: '2011:01:30 13:11:02' wird zu SimpleDateFormat("yyyyMMdd_HHmm_");
                    dateValue += image.getAbsoluteFile().getName();


// EXIF defines a string for the date itself: http://www.exif.org/samples/canon-ixus.html or http://www.exif.org/specifications.html
/*
D. Other Tags
DateTime
The date and time of image creation. In this standard it is the date and time the file was changed. The format is
"YYYY:MM:DD HH:MM:SS" with time shown in 24-hour format, and the date and time separated by one blank
character [20.H]. When the date and time are unknown, all the character spaces except colons (":") may be filled
with blank characters, or else the Interoperability field may be filled with blank characters. The character string
length is 20 bytes including NULL for termination. When the field is left blank, it is treated as unknown.
Tag = 306 (132.H)
Type = ASCII
Count = 20
Default = none
from http://www.exif.org/Exif2-2.PDF
*/

                    // TODO: Bug in Canon-exif meta data - date and time have ':' as separator
                    // schlägt fehl wegen : im Datum: formattedDate = PHOTO_DATE_FORMAT.parse(dateValue);
                    // schlägt fehl - System.out.println(PHOTO_DATE_FORMAT.parse(field.getValueDescription()));
//                String[] parsePatterns = new String[]{"yyyy:MM:dd HH:mm:ss", "dd-MM-yyyy HH:mm", "dd/MM/yyyy HH:mm", "dd.MM.yyyy HH:mm"};
//                String[] parsePatterns = new String[]{"yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm", "dd/MM/yyyy HH:mm", "dd.MM.yyyy HH:mm"};
//                formattedDate = DateUtils.parseDate(dateValue, parsePatterns);
                    //  formattedDate = PHOTO_DATE_FORMAT.format(PHOTO_DATE_FORMAT.parse(field.getValueDescription()));
                    // formattedDate = PHOTO_DATE_FORMAT.parse(dateValue);

                    LOG.info("Umgewandelt: " + dateValue);

                }
            }

        }

        return dateValue;
    }
}
