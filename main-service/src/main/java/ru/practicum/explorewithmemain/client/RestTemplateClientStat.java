package ru.practicum.explorewithmemain.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmemain.dto.EndpointHitDto;


@Service
public class RestTemplateClientStat extends RestTemplateClient {

    private static final String API_PREFIX = "/hit";

    @Autowired
    public RestTemplateClientStat(@Value("${stats-service.url}") String serverUrl) {
        super(serverUrl + API_PREFIX);
    }

    public ResponseEntity<Object> createEndpointHitStatistics(EndpointHitDto endpointHit) {
        return post("", endpointHit);
    }
}
