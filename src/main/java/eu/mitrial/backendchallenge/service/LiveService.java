package eu.mitrial.backendchallenge.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import eu.mitrial.backendchallenge.beans.LiveResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LiveService {

    private static final Logger logger = LoggerFactory.getLogger(LiveService.class);
    public static final String SORT = "sort";
    public static final String ORDER = "order";
    public static final String STARS = "stars";
    public static final String DESC = "desc";
    public static final String PER_PAGE = "per_page";
    private final String gitHubUrl;
    private final Client client;

    public LiveService(@Value("${github.url}") String gitHubUrl) {
        this.gitHubUrl = gitHubUrl;
        final JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        client = ClientBuilder.newClient(new ClientConfig(jacksonJsonProvider));
    }

    @Cacheable("LiveResponses")
    public LiveResponse search(String startDate, String language, int limit) {
        try {
            String q = buildQ(startDate, language);
            WebTarget target = client.target(gitHubUrl)
                    .queryParam("q", q)
                    .queryParam(SORT, STARS)
                    .queryParam(ORDER, DESC)
                    .queryParam(PER_PAGE, limit);
            Response response = target
                    .request("application/vnd.github.mercy-preview+json")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
                    .get();
            return response.readEntity(LiveResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    private String buildQ(String startDate, String language) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(language)) {
            stringBuilder.append("language:").append(language).append(" ");
        }
        stringBuilder.append("created:>").append(startDate);
        return stringBuilder.toString();
    }

}
