package com.app.rotatio.vaadin.html;

import com.app.rotatio.vaadin.domain.WorkerData;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

public class HtmlGenerator {
    private TemplateEngine templateEngine;

    public HtmlGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String generateHtml(List<WorkerData> workerDataList) {
        Context context = new Context();
        context.setVariable("workerDataList", workerDataList);

        return templateEngine.process("worker-template", context);
    }
}
