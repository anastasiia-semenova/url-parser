package com.task.urlscanner.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class FileParsingService {

    public List<String> parseUrls(final InputStream stream) {
        Set<String> parsedUrls = new HashSet<>();
        Scanner s = new Scanner(stream);
        String url;
        Pattern pattern = Pattern.compile(
                "((http?|https|ftp|file)://)?((W|w){3}.)?[a-zA-Z0-9]+\\.[a-zA-Z]+(/)?(\\.[a-zA-Z]+)?(/)?([^\\s]+)?(([^\\s.,]+))");

        while (null != (url = s.findWithinHorizon(pattern, 0))) {
            if(!url.toLowerCase().matches(".*(\\w)+\\.(png|gif|jpeg|jpg|doc|txt|html|xls|pdf)")){
                parsedUrls.add(normalizeUrl(url));
            } 
        }
        return parsedUrls.stream().toList();
    }

    protected String normalizeUrl(String parsedUrl) {


        if (!parsedUrl.matches("^\\w+?://.*")) {
            parsedUrl = "http://" + parsedUrl;
        }
        URI uri = URI.create(parsedUrl);
        return uri.normalize().toString();
    }
}
