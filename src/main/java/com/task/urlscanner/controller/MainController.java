package com.task.urlscanner.controller;

import com.google.gson.Gson;
import com.task.urlscanner.service.FileParsingService;
import com.task.urlscanner.dto.TextDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class MainController {

    private final FileParsingService parsingService;

    @Autowired
    MainController(FileParsingService parsingService) {
        this.parsingService = parsingService;
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from My App!";
    }

    @GetMapping("/upload-form")
    public ModelAndView listUploadedFiles() throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("uploadForm");
        return modelAndView;
    }


    @RequestMapping(path = "/file/parse-urls", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String handleFileUpload(@RequestPart MultipartFile file) {
        try {
            return parsingService.parseUrls(file.getInputStream()).toString();
        } catch (Exception e) {
            e.getStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Can not parse file.", e);
        }
    }

    @PostMapping("/content/parse-urls")
    public String parseJson(@RequestBody TextDocumentDTO doc) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(doc.getContent().getBytes());
            List<String> parsedUrls = parsingService.parseUrls(stream);
            String response = new Gson().toJson(parsedUrls);
            return response;
        } catch (Exception e) {
            e.getStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Can not parse content.", e);
        }
    }
}
