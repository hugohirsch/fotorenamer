package de.aikiit.bildbearbeiter.image;

import de.aikiit.bildbearbeiter.exception.InvalidDirectoryException;
import de.aikiit.bildbearbeiter.exception.NoFilesFoundException;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.File;

import static de.aikiit.bildbearbeiter.TestConstants.FULLPATH_IMAGES;
import static de.aikiit.bildbearbeiter.TestConstants.FULLPATH_TEST_IMG;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Test image renaming.
 *
 * @author hirsch
 * @version 2011-06-02, 13:41
 */
public class CreationDateFromExifImageRenamerTest {

    final static private Logger LOG = Logger.
            getLogger(CreationDateFromExifImageRenamerTest.class);

    /**
     * Ensure that no NullPointerException is thrown with null arguments.
     */
    @Test(expected = InvalidDirectoryException.class)
    public final void checkNPECorrectnessInConstructor() throws Exception {
        CreationDateFromExifImageRenamer imageRenamer = new
                CreationDateFromExifImageRenamer(null);
    }

    public final void checkNPECorrectnessWhenRenaming() throws NoFilesFoundException, InvalidDirectoryException {
        CreationDateFromExifImageRenamer imageRenamer = new
                CreationDateFromExifImageRenamer("tmp");
        imageRenamer.renameImage(null);
    }


    /**
     * Perform file renaming (while waiting for Thread to finish).
     */
    // TODO redesign application - component mingles function and GUI and is
    // not clearly testable
    @Test
    public final void renameTestImageAndDeleteFileAfterwards() throws
            Exception {

        LOG.info("Working on file " + FULLPATH_TEST_IMG);
        assertTrue("Test image directory has to exist, i.e. mvn filtering was correct",
                new File(FULLPATH_TEST_IMG).exists());
        assertTrue("Test image has to exist", new File(FULLPATH_TEST_IMG).exists());

        CreationDateFromExifImageRenamer renamer = new CreationDateFromExifImageRenamer(FULLPATH_IMAGES);
        Thread t = new Thread(renamer);
        t.start();
        assertTrue(t.isAlive());
        // wait until thread is finished
        t.join();
        assertEquals(Thread.State.TERMINATED, t.getState());

        // FIXME since MetadataExtractorTest renames the image,
        // it has twice the prefix
/*
        assertTrue("Renamed test image has to exist afterwards.",
                new File
                        (TestConstants.FULLPATH_IMAGES +
                                "20110130_131102_20110130_131102_IMG_7559_mini.JPG").exists());
                                */
    }

}