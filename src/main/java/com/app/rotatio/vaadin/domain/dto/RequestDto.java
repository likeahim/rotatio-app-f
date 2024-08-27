package com.app.rotatio.vaadin.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestDto {
    private LocalDate date;
    @JsonProperty("html")
    private String html;
    @JsonProperty("name")
    private String name;
    @JsonProperty("margins")
    private String margins = "40px 5px 40px 5px";
    @JsonProperty("paperSize")
    private String paperSize = "A4";
    @JsonProperty("orientation")
    private String orientation = "Portrait";
    @JsonProperty("printBackground")
    private boolean printBackground = true;
    @JsonProperty("header")
    private String header = "<div style='width:100%; text-align:center; font-size:10px;'" +
                             ">Workflow List</div>";
    @JsonProperty("footer")
    private String footer = "<div style='width:100%; text-align:right; font-size:10px;'>Good Luck. Page " +
            "<span class='pageNumber'></span> of <span class='totalPages'></span></div>";
    @JsonProperty("async")
    private boolean async = false;
}
