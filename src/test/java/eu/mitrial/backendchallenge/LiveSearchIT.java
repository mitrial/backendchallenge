package eu.mitrial.backendchallenge;

import eu.mitrial.backendchallenge.beans.LiveResponse;
import eu.mitrial.backendchallenge.service.LiveService;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.core.HttpHeaders;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource
public class LiveSearchIT {

    @Autowired
    private LiveService service;

    @Rule
    public final MockServerRule mockServerRule = new MockServerRule(this, 1080);


    @Test
    public void contextLoads() {
        Assert.assertNotNull(service);
    }

    @Test
    public void searchTest() throws Exception {
        MockServerClient client = mockServerRule.getClient();

        client
                .when(HttpRequest.request().withPath("/github/"))
                .respond(HttpResponse.response()
                        .replaceHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withBody(FileUtils.readFileToString(
                                ResourceUtils.getFile(
                                        "src/test/resources/eu/mitrial/backendchallenge/created_2020_10_01.json"
                                ),
                                StandardCharsets.UTF_8)
                        )
                );

        LiveResponse search = service.search("2020-10-01", null, 10);
        Assert.assertNotNull(search);
        Assert.assertNotNull(search.getItems());
        Assert.assertEquals(10, search.getItems().size());
    }

}


