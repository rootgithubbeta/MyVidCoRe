/*
 * $Id$ 
 * $Revision$ $Date$
 *
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
package org.mycore.vidconv.frontend.entity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Ren\u00E9 Adler (eagle)
 *
 */
@XmlRootElement(name = "settings")
public class SettingsWrapper {

    private List<Output> output = new ArrayList<>();

    private List<Plugin> plugins = new ArrayList<>();

    /**
     * @return the output
     */
    public List<Output> getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(List<Output> output) {
        this.output = output;
    }

    /**
     * @return the plugins
     */
    public List<Plugin> getPlugins() {
        return plugins;
    }

    /**
     * @param plugins the plugins to set
     */
    public void setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }

    @XmlRootElement(name = "output")
    @XmlType(name = "Settings.Output")
    public static class Output {

        private String filenameAppendix;

        private String format;

        private Video video;

        private Audio audio;

        private Path inputPath;

        private Path outputPath;

        /**
         * @return the filenameAppendix
         */
        public String getFilenameAppendix() {
            return filenameAppendix;
        }

        /**
         * @param filenameAppendix the filenameAppend to set
         */
        public void setFilenameAppendix(String filenameAppendix) {
            this.filenameAppendix = filenameAppendix;
        }

        /**
         * @return the format
         */
        @XmlElement
        public String getFormat() {
            return format;
        }

        /**
         * @return the video
         */
        @XmlElement
        public Video getVideo() {
            return video;
        }

        /**
         * @return the audio
         */
        @XmlElement
        public Audio getAudio() {
            return audio;
        }

        /**
         * @param format the format to set
         */
        public void setFormat(String format) {
            this.format = format;
        }

        /**
         * @param video the video to set
         */
        public void setVideo(Video video) {
            this.video = video;
        }

        /**
         * @param audio the audio to set
         */
        public void setAudio(Audio audio) {
            this.audio = audio;
        }

        /**
         * @return the inputPath
         */
        @XmlTransient
        public Path getInputPath() {
            return inputPath;
        }

        /**
         * @param inputPath the inputPath to set
         */
        public void setInputPath(Path inputPath) {
            this.inputPath = inputPath;
        }

        /**
         * @return the outputPath
         */
        @XmlTransient
        public Path getOutputPath() {
            return outputPath;
        }

        /**
         * @param outputPath the outputPath to set
         */
        public void setOutputPath(Path outputPath) {
            this.outputPath = outputPath;
        }

        public Output getCopy() {
            Output out = new Output();
            out.audio = this.audio;
            out.video = this.video;
            out.filenameAppendix = this.filenameAppendix;
            out.format = this.format;
            out.inputPath = this.inputPath;
            out.outputPath = this.outputPath;

            return out;
        }
    }

    @XmlRootElement(name = "video")
    @XmlType(name = "Settings.Video")
    public static class Video {
        private String codec;

        private String scale;

        private Boolean upscale;

        private String framerate;

        private String framerateType;

        private String preset;

        private String tune;

        private String profile;

        private String level;

        private String pixelFormat;

        private Quality quality;

        private String advancedOptions;

        /**
         * @return the codec
         */
        public String getCodec() {
            return codec;
        }

        /**
         * @param codec the codec to set
         */
        public void setCodec(String codec) {
            this.codec = codec;
        }

        /**
         * @return the scale
         */
        public String getScale() {
            return scale;
        }

        /**
         * @param scale the scale to set
         */
        public void setScale(String scale) {
            this.scale = scale;
        }

        /**
         * @return the upscale
         */
        public Boolean getUpscale() {
            return Optional.ofNullable(upscale).orElse(false);
        }

        /**
         * @param upscale the upscale to set
         */
        public void setUpscale(Boolean upscale) {
            this.upscale = upscale;
        }

        /**
         * @return the framerate
         */
        public String getFramerate() {
            return framerate;
        }

        /**
         * @param framerate the framerate to set
         */
        public void setFramerate(String framerate) {
            this.framerate = framerate;
        }

        /**
         * @return the framerateType
         */
        public String getFramerateType() {
            return framerateType;
        }

        /**
         * @param framerateType the framerateType to set
         */
        public void setFramerateType(String framerateType) {
            this.framerateType = framerateType;
        }

        /**
         * @return the preset
         */
        public String getPreset() {
            return preset;
        }

        /**
         * @param preset the preset to set
         */
        public void setPreset(String preset) {
            this.preset = preset;
        }

        /**
         * @return the tune
         */
        public String getTune() {
            return tune;
        }

        /**
         * @param tune the tune to set
         */
        public void setTune(String tune) {
            this.tune = tune;
        }

        /**
         * @return the profile
         */
        public String getProfile() {
            return profile;
        }

        /**
         * @param profile the profile to set
         */
        public void setProfile(String profile) {
            this.profile = profile;
        }

        /**
         * @return the level
         */
        public String getLevel() {
            return level;
        }

        /**
         * @param level the level to set
         */
        public void setLevel(String level) {
            this.level = level;
        }

        /**
         * @return the pixelFormat
         */
        public String getPixelFormat() {
            return pixelFormat;
        }

        /**
         * @param pixelFormat the pixelFormat to set
         */
        public void setPixelFormat(String pixelFormat) {
            this.pixelFormat = pixelFormat;
        }

        /**
         * @return the quality
         */
        public Quality getQuality() {
            return quality;
        }

        /**
         * @param quality the quality to set
         */
        public void setQuality(Quality quality) {
            this.quality = quality;
        }

        /**
         * @return the advancedOptions
         */
        public String getAdvancedOptions() {
            return advancedOptions;
        }

        /**
         * @param advancedOptions the advancedOptions to set
         */
        public void setAdvancedOptions(String advancedOptions) {
            this.advancedOptions = advancedOptions;
        }

        @XmlRootElement(name = "quality")
        public static class Quality {
            private String type;

            private String rateFactor;

            private Integer scale;

            private Integer bitrate;

            /**
             * @return the type
             */
            public String getType() {
                return type;
            }

            /**
             * @param type the type to set
             */
            public void setType(String type) {
                this.type = type;
            }

            /**
             * @return the rateFactor
             */
            public String getRateFactor() {
                return rateFactor;
            }

            /**
             * @param rateFactor the rateFactor to set
             */
            public void setRateFactor(String rateFactor) {
                this.rateFactor = rateFactor;
            }

            /**
             * @return the scale
             */
            public Integer getScale() {
                return scale;
            }

            /**
             * @param scale the scale to set
             */
            public void setScale(Integer scale) {
                this.scale = scale;
            }

            /**
             * @return the bitrate
             */
            public Integer getBitrate() {
                return bitrate;
            }

            /**
             * @param bitrate the bitrate to set
             */
            public void setBitrate(Integer bitrate) {
                this.bitrate = bitrate;
            }

        }
    }

    @XmlRootElement(name = "audio")
    @XmlType(name = "Settings.Audio")
    public static class Audio {
        private String codec;

        private String mixdown;

        private Integer samplerate;

        private Integer bitrate;

        /**
         * @return the codec
         */
        public String getCodec() {
            return codec;
        }

        /**
         * @return the mixdown
         */
        public String getMixdown() {
            return mixdown;
        }

        /**
         * @return the samplerate
         */
        public Integer getSamplerate() {
            return samplerate;
        }

        /**
         * @return the bitrate
         */
        public Integer getBitrate() {
            return bitrate;
        }

        /**
         * @param codec the codec to set
         */
        public void setCodec(String codec) {
            this.codec = codec;
        }

        /**
         * @param mixdown the mixdown to set
         */
        public void setMixdown(String mixdown) {
            this.mixdown = mixdown;
        }

        /**
         * @param samplerate the samplerate to set
         */
        public void setSamplerate(Integer samplerate) {
            this.samplerate = samplerate;
        }

        /**
         * @param bitrate the bitrate to set
         */
        public void setBitrate(Integer bitrate) {
            this.bitrate = bitrate;
        }
    }

    @XmlRootElement(name = "plugin")
    @XmlType(name = "Settings.Plugin")
    public static class Plugin {
        private String name;

        private boolean enabled;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * @param enabled the enabled to set
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
