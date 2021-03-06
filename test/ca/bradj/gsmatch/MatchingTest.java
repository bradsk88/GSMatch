package ca.bradj.gsmatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import ca.bradj.common.base.Failable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class MatchingTest {

    private static ImmutableList<String> prefs;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        Collection<String> shows = Lists.newArrayList();
        shows.add("The Daily Show");
        shows.add("NOVA");
        prefs = ImmutableList.copyOf(shows);
    }

    @Test
    public void testDailyShowLowerCastExpectMatch() {
        String testName = "the.daily.show.2014.01.30.rep.nancy.pelosi.720p.hdtv.x264-2hd";
        Failable<Match> strongestMatch = Matching.getStrongestMatch(Lists.newArrayList(testName), testName);
        assertTrue(strongestMatch.isSuccess());
    }

    @Test
    public void testNOVAShouldntMatchRayDonovan() {
        String testName = "Ray.Donovan.S01E08.720p.BluRay.X264-REWARD";
        Failable<Match> strongestMatch = Matching.getStrongestMatch(prefs, testName);
        assertFalse(strongestMatch.isSuccess());
    }

    @Test
    public void testDailyShowExpectMatch() {
        String testName = "The.Daily.Show.2014.01.30.Rep.Nancy.Pelosi.720p.HDTV.x264-2HD";
        Failable<Match> strongestMatch = Matching.getStrongestMatch(prefs, testName);
        assertTrue(strongestMatch.isSuccess());
    }

    @Test
    public void testIdentifiesEntireSeason() {
        Collection<String> names = Lists.newArrayList();
        names.add("The Walking Dead S3 D2 NORDiC COMPLETE BLURAY-TRUSTED");
        names.add("The Walking Dead S3 D1 NORDiC COMPLETE BLURAY-TRUSTED ");
        names.add("Game of Thrones S03 (2013) Bluray 720Pad AAC x264 - aSOUL");
        names.add("Game Of Thrones S3 D2 MULTiSUBS COMPLETE BLURAY-CLASSiC ");
        names.add("Game Of Thrones S03 Season 3 EXTRAS 720p BluRay DTS x264-PHD");
        for (String testName : names) {
            assertTrue(Matching.isWholeSeason(testName));
        }
    }

    @Test
    public void testValidShowsNotIdentifiedAsEntireSeason() {
        Collection<String> names = Lists.newArrayList();
        names.add("The.Daily.Show.2014.01.30.Rep.Nancy.Pelosi.720p.HDTV.x264-2HD");
        names.add("Game of Thrones S03E10 720p BluRay x264 DEMAND");
        for (String testName : names) {
            assertFalse(Matching.isWholeSeason(testName));
        }
    }

    @Test
    public void testSameShowSameEpisodeDifferentFormatIsStrongMatch() {
        String toMatch = "Top_Gear.21x05.720p_HDTV_x264-FoV";
        String against = "Top.Gear.S21E05.720p.HDTV.x264.CLARTiTY.mkv";
        Failable<Double> matchPercent = Matching.getMatchPercent(toMatch, against);
        assertTrue(matchPercent.isSuccess());

        String toMatch2 = "Mythbusters.S13E08.1080p.WEB-DL.AAC2.0.H.264-NTb.mkv";
        String against2 = "MythBusters.S13E08.Supersonic.Ping.Pong.and.Ice.Cannon.720p.HDTV.x264-DHD";
        Failable<Double> matchPercent2 = Matching.getMatchPercent(toMatch2, against2);
        assertTrue(matchPercent2.isSuccess());

    }

    @Test
    public void testSameShowSameEpisodeDifferentFormatGivesSameMatchPercent() {
        String toMatch = "Top_Gear.21x05.720p_HDTV_x264-FoV";
        String against = "Top.Gear.S21E05.720p.HDTV.x264.CLARTiTY.mkv";
        Failable<Double> matchPercent = Matching.getMatchPercent(toMatch, against);
        Failable<Double> matchPercent2 = Matching.getMatchPercent(against, toMatch);
        assertTrue(matchPercent.isSuccess());
        assertTrue(matchPercent2.isSuccess());
        assertEquals(matchPercent.get(), matchPercent2.get(), 0.0);
    }

    @Test
    public void testNegationCausesWouldBeMatchToNotMatch() {
        String toMatch = "Top Gear -US";
        String testName = "Top.Gear.US.S03E16.DVDRip.x264-DEiMOS";
        Failable<Match> strongestMatch = Matching.getStrongestMatch(Lists.newArrayList(toMatch), testName);
        assertFalse(strongestMatch.isSuccess());
    }

    @Test
    public void testNegationStillMatchesWhereNegationHasNoEffect() {
        String toMatch = "Top Gear -US";
        String testName = "Top.Gear.S21E05.720p.HDTV.x264.CLARTiTY.mkv";
        Failable<Match> strongestMatch = Matching.getStrongestMatch(Lists.newArrayList(toMatch), testName);
        assertTrue(strongestMatch.isSuccess() ? null : strongestMatch.getReason(), strongestMatch.isSuccess());
    }

}
