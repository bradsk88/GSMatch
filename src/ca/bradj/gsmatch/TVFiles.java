package ca.bradj.gsmatch;

import java.util.Collection;

import com.google.common.collect.Lists;

public class TVFiles {

    private static final Collection<String> JUNK = Lists.newArrayList("x264", "xvid", "480p", "720p", "1080p", "dl", "dd5", "web", "repack", "hdtv",
            "web-dl", "mkv", "torrent", "the", "2hd", "aac2");

    public static Collection<String> getPotentialName(String string) {

        Collection<String> pn = Lists.newArrayList();
        for (String i : string.split("[\\.|-|_|\\s]")) {
            if (EpisodeID.isA(i)) {
                continue;
            }
            if (JUNK.contains(i.toLowerCase())) {
                continue;
            }
            pn.add(i);
        }
        return pn;
    }

    public static String stripJunk(String string) {
        StringBuilder sb = new StringBuilder();
        for (String i : string.split("[\\.|\\-|_|\\s]")) {
            if (JUNK.contains(i.toLowerCase())) {
                continue;
            }
            sb.append(i + ".");
        }
        return sb.toString();
    }

}
