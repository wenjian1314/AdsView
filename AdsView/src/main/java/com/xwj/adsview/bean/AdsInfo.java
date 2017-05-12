package com.xwj.adsview.bean;

import java.util.List;

/**
 * Created by Xu Wenjian on 2017/5/8.
 */

public class AdsInfo {

    private static AdsInfo adsInfo;

    public AdsInfo() {}

    /**
     * 获取当前广告信息的实例
     * @return
     */
    public static AdsInfo getAdsInfo() {
        return adsInfo;
    }

    /**
     * 将当前的adsInfo与传入的参数进行比较，若内容相同，则返回true，否则返回false，并将传入的参数变成当前的adsInfo
     * @param adsInfo
     * @return
     */
    public static boolean compareAndSet(AdsInfo adsInfo) {
        if (AdsInfo.adsInfo == null) {
            AdsInfo.adsInfo = adsInfo;
            return false;
        }
        if (AdsInfo.adsInfo.equals(adsInfo)) return true;
        AdsInfo.adsInfo = adsInfo;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdsInfo adsInfo = (AdsInfo) o;


        if (partNum != adsInfo.partNum) return false;
        return content != null ? content.equals(adsInfo.content) : adsInfo.content == null;

    }

    private int partNum;
    public class Content {
        private int partIndex;
        private List<String> videoUrls;
        private List<String> picUrls;

        public int getPartIndex() {
            return partIndex;
        }

        public void setPartIndex(int partIndex) {
            this.partIndex = partIndex;
        }

        public List<String> getPicUrls() {
            return picUrls;
        }

        public void setPicUrls(List<String> picUrls) {
            this.picUrls = picUrls;
        }

        public List<String> getVideoUrls() {
            return videoUrls;
        }

        public void setVideoUrls(List<String> videoUrls) {
            this.videoUrls = videoUrls;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Content content = (Content) o;

            if (partIndex != content.partIndex) return false;
            if (videoUrls != null ? !videoUrls.equals(content.videoUrls) : content.videoUrls != null)
                return false;
            return picUrls != null ? picUrls.equals(content.picUrls) : content.picUrls == null;

        }

        @Override
        public int hashCode() {
            int result = partIndex;
            result = 31 * result + (videoUrls != null ? videoUrls.hashCode() : 0);
            result = 31 * result + (picUrls != null ? picUrls.hashCode() : 0);
            return result;
        }
    }
    private List<Content> content;

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }


    public int getPartNum() {
        return partNum;
    }

    public void setPartNum(int partNum) {
        this.partNum = partNum;
    }

}
