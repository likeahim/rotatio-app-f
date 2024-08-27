package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.domain.dto.*;
import com.app.rotatio.vaadin.mapper.WorkerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;
    private final RestTemplate restTemplate;
    private final WorkerMapper workerMapper;
    private final TaskViewService taskViewService;
    private final WorkplaceViewService workplaceViewService;
    private final PlanViewService planViewService;

    public ResponseDto generatePdfFromWorkerData(List<WorkerDto> workers) {
        TaskDto task = new TaskDto(null, "No task", "", true);
        WorkplaceDto workplace = new WorkplaceDto(null, "No workplace", true, true);
        List<WorkerListDto> workersList = new ArrayList<>();
        LocalDate headerDate = null;

        for (WorkerDto worker : workers) {
            if (worker.getTaskId() != null) {
                task = taskViewService.getTaskById(worker.getTaskId());
            }
            if (worker.getWorkplaceId() != null) {
                workplace = workplaceViewService.getWorkplaceById(worker.getWorkplaceId());
            }
            WorkerListDto listWorker = workerMapper.mapToWorkerListDto(worker, task.getName(), workplace.getDesignation());
            workersList.add(listWorker);
        }
        Context context = new Context();
        context.setVariable("workerDataList", workersList);

        String filledTemplate = templateEngine.process("worker-template", context);
        RequestDto requestDto = new RequestDto();
        requestDto.setHtml(filledTemplate);
        requestDto.setName("workflowList.pdf");
        requestDto.setDate(headerDate);

        return sendHtmlToPdfCo(requestDto);
    }

    private ResponseDto sendHtmlToPdfCo(RequestDto requestDto) {
        String url = "http://localhost:8080/v1/rotatio/pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestDto> requestEntity = new HttpEntity<>(requestDto, headers);
        return restTemplate.postForObject(url, requestEntity, ResponseDto.class);
    }
}
