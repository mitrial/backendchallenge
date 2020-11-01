package eu.mitrial.backendchallenge.service;

import eu.mitrial.backendchallenge.beans.LiveResponse;
import eu.mitrial.backendchallenge.components.ClientProvider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public static final String APPLICATION_VND_GITHUB_MERCY_PREVIEW_JSON = "application/vnd.github.mercy-preview+json";
    public static final String Q = "q";
    private final String gitHubUrl;
    private final Client client;

    public LiveService(@Value("${github.url}") String gitHubUrl, @Autowired ClientProvider clientProvider) {
        this.gitHubUrl = gitHubUrl;
        this.client = clientProvider.getClient();
    }

    @Cacheable("LiveResponses")
    public LiveResponse search(String startDate, String language, int limit) {
        try {
            String q = buildQ(startDate, language);
            WebTarget target = client.target(gitHubUrl)
                    .queryParam(Q, q)
                    .queryParam(SORT, STARS)
                    .queryParam(ORDER, DESC)
                    .queryParam(PER_PAGE, limit);
            return target
                    .request(APPLICATION_VND_GITHUB_MERCY_PREVIEW_JSON)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
                    .get(LiveResponse.class);
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
