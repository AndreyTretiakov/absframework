package com.tretiakov.absframework.views.text;

import androidx.annotation.NonNull;

/**
 * @author Andrey Tretiakov
 */
public enum Font implements AssetFont {
    ROBOTO_BLACK("Roboto-Black"),
    ROBOTO_BLACK_ITALIC("Roboto-BlackItalic"),
    ROBOTO_BLACK_BOLD("Roboto-Bold"),
    ROBOTO_BLACK_BOLD_ITALIC("Roboto-BoldItalic"),
    ROBOTO_ITALIC("Roboto-Italic"),
    ROBOTO_LIGHT("Roboto-Light"),
    ROBOTO_LIGHT_ITALIC("Roboto-LightItalic"),
    ROBOTO_MEDIUM("Roboto-Medium"),
    ROBOTO_MEDIUM_ITALIC("Roboto-MediumItalic"),
    ROBOTO_REGULAR("Roboto-Regular"),
    ROBOTO_THIN("Roboto-Thin"),
    ROBOTO_THIN_ITALIC("Roboto-ThinItalic");

    @NonNull
    final String PATH;

    Font(String name) {
        PATH = BASE + name + TTF;
    }

    @NonNull
    public String getPath() {
        return PATH;
    }
}