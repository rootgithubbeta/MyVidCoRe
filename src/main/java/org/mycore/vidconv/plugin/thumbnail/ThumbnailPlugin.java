/*
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * This program is free software; you can use it, redistribute it
 * and / or modify it under the terms of the GNU General Public License
 * (GPL) as published by the Free Software Foundation; either version 3
 * of the License or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program, in a file called gpl.txt or license.txt.
 * If not, write to the Free Software Foundation Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */
package org.mycore.vidconv.plugin.thumbnail;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.vidconv.backend.encoder.FFMpegImpl;
import org.mycore.vidconv.backend.service.ConverterService;
import org.mycore.vidconv.backend.service.ConverterService.ConverterJob;
import org.mycore.vidconv.common.event.Event;
import org.mycore.vidconv.common.util.Executable;
import org.mycore.vidconv.frontend.entity.probe.ProbeWrapper;
import org.mycore.vidconv.plugin.ListenerPlugin;
import org.mycore.vidconv.plugin.annotation.Plugin;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
@Plugin(name = "Thumbnail Plugin", description = "Generates Thumbnails from Videos.")
public class ThumbnailPlugin extends ListenerPlugin {

    private static final Logger LOGGER = LogManager.getLogger();

    private static int NUM_THUMBS = 10;

    private static int NUM_RETRIES = 3;

    private static final long PROCESS_TIMEOUT_VALUE = 1;

    private static final TimeUnit PROCESS_TIMEOUT_UNIT = TimeUnit.MINUTES;

    /* (non-Javadoc)
     * @see org.mycore.vidconv.common.event.Listener#handleEvent(org.mycore.vidconv.common.event.Event)
     */
    @Override
    public void handleEvent(Event<?> event) throws Exception {
        if (ConverterService.EVENT_CONVERT_DONE.equals(event.getType())
                && event.getSource().equals(ConverterService.class)) {
            final ConverterJob job = (ConverterJob) event.getObject();

            if (job.exitValue() == 0) {
                ProbeWrapper probe = job.outputs().stream().map(cj -> {
                    try {
                        return FFMpegImpl.probe(cj.getOutputPath());
                    } catch (Exception e) {
                        return null;
                    }
                }).filter(pw -> pw != null && !pw.getStreams().isEmpty())
                        .reduce((pw1, pw2) -> (Optional.ofNullable(pw1.getStreams().get(0).getCodedWidth()).orElse(0)
                                + Optional.ofNullable(pw1.getStreams().get(0).getCodedHeight())
                                        .orElse(0)) > (Optional.ofNullable(pw2.getStreams().get(0).getCodedWidth())
                                                .orElse(0)
                                                + Optional.ofNullable(pw2.getStreams().get(0).getCodedHeight())
                                                        .orElse(0)) ? pw1 : pw2)
                        .orElse(null);

                if (probe != null) {
                    String fName = job.inputPath().getFileName().toString();
                    float duration = Float.parseFloat(probe.getFormat().getDuration());
                    int posThumbs = Math.min(NUM_THUMBS, Math.round((float) Math.floor(duration)));

                    for (int ti = 1; ti < posThumbs + 1; ti++) {
                        long time = Math.round((ti - 0.5) * duration / posThumbs);
                        String tName = job.outputPath()
                                .resolve(fName.substring(0, fName.lastIndexOf(".")) + "-thumb-" + formatIndex(ti)
                                        + ".jpg")
                                .toAbsolutePath()
                                .toString();
                        String cmd = "ffmpeg -ss " + time + " -i \"" + probe.getFormat().getFilename()
                                + "\" -vf select='eq(pict_type\\,I)' -vframes 1 \"" + tName + "\"";

                        LOGGER.info("generate thumbnail " + tName + "...");
                        LOGGER.debug(cmd);

                        int retry = 0;
                        int res = -1;
                        do {
                            Executable exec = new Executable(cmd);

                            res = exec.runAndWait(PROCESS_TIMEOUT_VALUE, PROCESS_TIMEOUT_UNIT);

                            if (res == 0) {
                                LOGGER.info("...done.");
                            } else if (res == Executable.PROCESS_TIMEOUT) {
                                LOGGER.warn("...timeout reached!");
                            }

                            retry++;
                        } while (res != 0 && retry < NUM_RETRIES);
                    }

                    if (posThumbs < NUM_THUMBS) {
                        LOGGER.warn("not enough length ({} sec.) to generate {} thumbnails for {}.",
                                String.format(Locale.ROOT, "%.2f", duration), NUM_THUMBS, fName);
                    }
                }
            }
        }
    }

    private static String formatIndex(int index) {
        StringBuffer sb = new StringBuffer();
        IntStream.range(0, Integer.toString(NUM_THUMBS).length() - Integer.toString(index).length())
                .forEach(i -> sb.append("0"));
        return sb.append(Integer.toString(index)).toString();
    }

}
