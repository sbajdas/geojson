package com.bajdas.geojson.service;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.model.CityMetaData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@EnableConfigurationProperties
@SpringBootTest(classes = CityIdService.class)
@TestPropertySource(properties = "idsearch=localhost")
//@ContextConfiguration(classes = CityIdService.class)
@RunWith(MockitoJUnitRunner.class)
public class CityIdServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CityIdService cityIdService;

    @Test
    public void shouldReturnCityMetadataObject() throws RestApiException {
//        ReflectionTestUtils.setField(cityIdService,"apiQueryUrl","localhost");
        when(restTemplate.getForObject(any(URI.class), eq(CityMetaData[].class))).thenReturn(prepareCityMetaData());
        CityMetaData krakow = cityIdService.getCityMetaData("krakow");
        assertEquals("1111", krakow.getOsm_id());
    }

    private CityMetaData[] prepareCityMetaData() {
        CityMetaData krakow = CityMetaData.builder().osm_id("1111").display_name("krakow").build();
        return new CityMetaData[]{krakow};
    }

    @Test(expected = RestApiException.class)
    public void shouldThrowException() throws RestApiException {
        // given
//        ReflectionTestUtils.setField(cityIdService,"apiQueryUrl","localhost");
        when(restTemplate.getForObject(any(URI.class), eq(CityMetaData[].class))).thenThrow(new RestClientException("ups"));
        // when
        cityIdService.getCityMetaData("krakow");
    }
}