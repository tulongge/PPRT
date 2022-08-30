package com.harry.domain;

public class Mask {
    private String[] maskContent;
    private int length;

    public Mask() {
    }

    public Mask(int length) {
        this.length = length;
        this.maskContent = new String[length];
    }

    public Mask(String[] maskContent) {
        this.maskContent = maskContent;
        this.length = maskContent.length;
    }

    public String[] getMaskContent() {
        return maskContent;
    }

    public void setMaskContent(String[] maskContent) {
        this.maskContent = maskContent;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String subChar : maskContent) {
            stringBuilder.append(subChar);
        }
        return stringBuilder.toString();
    }
}
