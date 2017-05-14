package org.nzbhydra.mockserver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.nzbhydra.mapping.gtihub.Asset;
import org.nzbhydra.mapping.gtihub.Release;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class MockGithub {

    private Release releasev1current;
    private Release releasev2;
    private List<Release> releases;

    public MockGithub() {
        releasev2 = new Release();
        releasev2.setBody("Changes in version 2.0.0");
        releasev2.setTagName("v2.0.0");
        Asset asset = new Asset();
        asset.setBrowserDownloadUrl("http://127.0.0.1:5080/NzbHydra-v2.0.0-windows.zip");
        asset.setName("NzbHydra-v2.0.0-windows.zip");
        asset.setSize(163L);
        releasev2.setAssets(Arrays.asList(asset));

        releasev1current = new Release();
        releasev1current.setBody("Changes in version 10.0");
        releasev1current.setTagName("v1.0.0");

        releases = Arrays.asList(releasev1current, releasev2);
    }


    @RequestMapping(value = "/repos/theotherp/nzbhydra2/releases/latest", method = RequestMethod.GET)
    public Release latestRelease() throws Exception {
        return releasev2;
    }

    @RequestMapping(value = "/repos/theotherp/nzbhydra2/releases", method = RequestMethod.GET)
    public List<Release> releases() throws Exception {
        return releases;
    }

    @RequestMapping(value = "/changelog.md", method = RequestMethod.GET, produces = org.springframework.http.MediaType.TEXT_HTML_VALUE)
    public String changelog() throws Exception {
        return "changelog";
    }

    @Configuration
    public class JacksonConfiguration {

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

            return mapper;
        }
    }
}