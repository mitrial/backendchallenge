package eu.mitrial.backendchallenge.components;

import com.fasterxml.jackson.databind.DeserializationFeature;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.springframework.stereotype.Component;

@Component
public class ClientProvider {

    public Client getClient() {
        final JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return ClientBuilder.newClient(new ClientConfig(jacksonJsonProvider));
    }

}
