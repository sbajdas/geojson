package com.bajdas.geojson.service;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.model.CityMetaData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityNameResolverServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CityNameResolverService cityNameResolverService;

    @Test
    public void shouldReturnCityMetadataObject() throws RestApiException {
        ReflectionTestUtils.setField(cityNameResolverService,"apiQueryUrl","localhost");
        when(restTemplate.getForObject(any(URI.class), eq(CityMetaData[].class))).thenReturn(prepareCityMetaData());
        CityMetaData krakow = cityNameResolverService.getCityMetaData("krakow");
        assertEquals("1111", krakow.getOsm_id());
    }

    private CityMetaData[] prepareCityMetaData() {
        CityMetaData krakow = CityMetaData.builder().osm_id("1111").display_name("krakow").build();
        return new CityMetaData[]{krakow};
    }

    @Test(expected = RestApiException.class)
    public void shouldThrowException() throws RestApiException {
        // given
        ReflectionTestUtils.setField(cityNameResolverService,"apiQueryUrl","localhost");
        when(restTemplate.getForObject(any(URI.class), eq(CityMetaData[].class))).thenThrow(new RestClientException("ups"));
        // when
        cityNameResolverService.getCityMetaData("krakow");
    }
}