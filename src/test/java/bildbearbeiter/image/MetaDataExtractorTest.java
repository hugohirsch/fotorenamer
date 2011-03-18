package bildbearbeiter.image;

import bildbearbeiter.MetaDataExtractor;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Read properties from test images.
 *
 * @author hirsch
 */
public class MetaDataExtractorTest {
    final static private Logger LOG = Logger.getLogger(MetaDataExtractorTest.class);
    final static String PATH2TESTIMG = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "data" + File.separator + "IMG_7559_mini.JPG";

    @Test
    public final void readDirectoryContent() throws Exception {
        File f = new File(PATH2TESTIMG);
        LOG.debug("Extracting metadata from " + f.getAbsolutePath());
        assertEquals("20110130_131102_IMG_7559_mini.JPG", MetaDataExtractor.generateCreationDateInCorrectFormat(f));
    }

}
