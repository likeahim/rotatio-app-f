package com.app.rotatio.vaadin.view.format;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.tabs.Tab;

public class FormatMethods {
    public static void formatFontLabel(NativeLabel forecastLabel, String fontSize, String fontWeight) {
        forecastLabel.getStyle()
                .set("font-size", fontSize)
                .set("font-weight", fontWeight);
    }

    public static void setNavigate(Tab tabName, String target) {
        tabName.getElement().addEventListener("click", event -> {
            UI.getCurrent().navigate(target);
        });
    }
}
