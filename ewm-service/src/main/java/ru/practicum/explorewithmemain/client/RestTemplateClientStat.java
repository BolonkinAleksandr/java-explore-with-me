package ru.practicum.explorewithmemain.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithmemain.dto.EndpointHitDto;


@Service
public class RestTemplateClientStat extends RestTemplateClient {

    @Autowired
    public RestTemplateClientStat(@Value("${stats-server.url}") String serverUrl, @Value("${hit.url}") String hitUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + hitUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> createEndpointHitStatistics(EndpointHitDto endpointHit) {
        return post("", endpointHit);
    }
}
