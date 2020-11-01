package eu.mitrial.backendchallenge.service;

import static org.mockito.Mockito.when;

import eu.mitrial.backendchallenge.beans.LiveResponse;
import eu.mitrial.backendchallenge.components.ClientProvider;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LiveServiceTest {

    public static final String TESTURL = "TESTURL";
    @Mock
    private ClientProvider clientProvider;
    @Mock
    private Client client;
    @Mock
    private WebTarget webTarget;
    @Mock
    private Invocation.Builder builder;

    @Captor
    private ArgumentCaptor<String> urlCapture;
    @Captor
    private ArgumentCaptor<String> queryParamKeyCapture;
    @Captor
    private ArgumentCaptor<Object> queryParamBodyCapture;
    @Captor
    private ArgumentCaptor<String> acceptedResponseTypesCapture;
    @Captor
    private ArgumentCaptor<String> headerKeyCapture;
    @Captor
    private ArgumentCaptor<Object> headerBodyCapture;
    private LiveService liveService;
    public static final List<String> EXPECTED_HEADER = Arrays.asList(
            LiveService.Q,
            LiveService.SORT,
            LiveService.ORDER,
            LiveService.PER_PAGE);

    @BeforeEach
    public void initMock() {
        when(clientProvider.getClient())
                .thenReturn(client);
        when(client.target(urlCapture.capture()))
                .thenReturn(webTarget);
        when(webTarget.queryParam(queryParamKeyCapture.capture(), queryParamBodyCapture.capture()))
                .thenReturn(webTarget);
        when(webTarget.request(acceptedResponseTypesCapture.capture()))
                .thenReturn(builder);
        when(builder.header(headerKeyCapture.capture(), headerBodyCapture.capture()))
                .thenReturn(builder);
        when(builder.get(Mockito.eq(LiveResponse.class)))
                .thenReturn(new LiveResponse());
        liveService = new LiveService(TESTURL, clientProvider);
    }

    @Test
    public void startDateIsNull() {
        LiveResponse response = liveService.search(null, null, 0);
        Assertions.assertNotNull(response);
        assertCaptures("created:>null");

    }

    private void assertCaptures(String expectedQ) {
        Assertions.assertEquals(TESTURL, urlCapture.getValue());

        List<String> allParamKeys = queryParamKeyCapture.getAllValues();
        Assertions.assertIterableEquals(EXPECTED_HEADER, allParamKeys);

        List<Object> allParamValues = queryParamBodyCapture.getAllValues();
        String q = String.valueOf(allParamValues.get(allParamKeys.indexOf(LiveService.Q)));
        Assertions.assertEquals(expectedQ, q);

        Assertions.assertEquals(LiveService.APPLICATION_VND_GITHUB_MERCY_PREVIEW_JSON,
                acceptedResponseTypesCapture.getValue());
        Assertions.assertEquals(HttpHeaders.CONTENT_TYPE, headerKeyCapture.getValue());
        Assertions.assertEquals(MediaType.APPLICATION_FORM_URLENCODED, headerBodyCapture.getValue());


    }

    @Test
    public void checkStartDate() {
        LiveResponse response = liveService.search("START_DATE", null, 0);
        Assertions.assertNotNull(response);
        assertCaptures("created:>START_DATE");
    }

    @Test
    public void checkStartDateAndLanguage() {
        LiveResponse response = liveService.search("START_DATE", "LANG", 0);
        Assertions.assertNotNull(response);
        assertCaptures("language:LANG created:>START_DATE");
    }

    @Test
    void checkPaging() {
        LiveResponse response = liveService.search(null, null, 333);
        Assertions.assertNotNull(response);
        int indexOfPerPage = queryParamKeyCapture.getAllValues().indexOf("per_page");
        Integer integer = (Integer) queryParamBodyCapture.getAllValues().get(indexOfPerPage);
        Assertions.assertEquals(333, integer);
    }
}