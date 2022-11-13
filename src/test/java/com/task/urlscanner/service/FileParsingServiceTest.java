package com.task.urlscanner.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
class FileParsingServiceTest {

    public static final String LONG_LINK_WITH_PARAMS = "https://www.google.com/search?q=google&rlz=1C5GCEM_en&oq=google&aqs=chrome.0.35i39j46i67i199i465j35i39j0i67l3j69i60l2.5749j0j7&sourceid=chrome&ie=UTF-8";
    public static final String SHORT_LINK = "https://www.youtube.com/watch?v=BTsHmYsVAVk";
    public static final String LINK_1_TO_NORMALIZE_1 = "www.flickr.com";
    public static final String LINK_1_NORMALIZED = "http://www.flickr.com";

    @Autowired
    private com.task.urlscanner.service.FileParsingService parsingService;

    @Test
    void shouldParseAndReturnSimpleUrl() throws IOException {
        String testUrl = "https://www.google.com";
        String content = testUrl;

        List<String> result = parsingService.parseUrls(new ByteArrayInputStream(content.getBytes()));

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(testUrl);
    }

    @Test
    void shouldParseAndReturnLongUrl() {
        String testUrl = LONG_LINK_WITH_PARAMS;
        String content = testUrl;

        List<String> result = parsingService.parseUrls(new ByteArrayInputStream(content.getBytes()));

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(testUrl);
    }    
    @Test
    void shouldParseAndReturnLongUrl2() {
        String testUrl = "https://www.google.com/maps/dir/Espoo/Vantaa/WithSecure,+Tammasaarenkatu+7,+00180+Helsinki/@60.2300093,24.69501,11z/data=!3m1!4b1!4m20!4m19!1m5!1m1!1s0x468df2faa43d4be1:0xf0b890b45d33539f!2m2!1d24.6559!2d60.2054911!1m5!1m1!1s0x469207b83a3845b5:0xe2b7cd7632a1804f!2m2!1d25.0377686!2d60.2933524!1m5!1m1!1s0x46920bca929cc719:0x5d02c5b684b0f2a0!2m2!1d24.9068597!2d60.1614827!3e0";
        String content = testUrl;

        List<String> result = parsingService.parseUrls(new ByteArrayInputStream(content.getBytes()));

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(testUrl);
    }
    
    @Test
    void shouldParseAndReturnFiveUrlsFromExampleDoc() {
        String content = "For example, if a client app sends a text file with the following text:\n" +
                "Visit photo hosting sites such as www.flickr.com,\n" +
                "\n" +
                "500px.com, www.freeimagehosting.net and https://postimage.io, and upload these two image files, picture.dog.png and picture.cat.jpeg, there. After that share their links\n" +
                "at https://www.facebook.com/.";

        List<String> validUrls = Arrays.asList(
                "http://www.flickr.com",
                "http://500px.com",
                "http://www.freeimagehosting.net",
                "https://postimage.io",
                "https://www.facebook.com/"
        ).stream().sorted().toList();

        List<String> result = parsingService.parseUrls(new ByteArrayInputStream(content.getBytes()));
        List<String> sortedResult = result.stream().sorted().toList();
        assertThat(result.size()).isEqualTo(5);
        for (int i = 0; i < result.size(); i++) {
            System.out.println(i + " " + sortedResult.get(i));
            assertThat(sortedResult.get(i)).isEqualTo(validUrls.get(i));
        }
    }


    @Test
    void shouldCorrectlyNormalizeUrl() {
        String normalizedUrl = parsingService.normalizeUrl(LINK_1_TO_NORMALIZE_1);
        assertThat(normalizedUrl).isEqualTo(LINK_1_NORMALIZED);
    }
}