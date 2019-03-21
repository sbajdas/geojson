package com.bajdas.geojson.service;

import com.bajdas.geojson.exception.RestApiException;
import com.bajdas.geojson.exception.RestApiNotFoundException;
import com.bajdas.geojson.model.CityGeography;
import com.bajdas.geojson.model.CityMetaData;
import org.geojson.GeoJsonObject;
import org.geojson.GeoJsonObjectVisitor;
import org.geojson.GeometryCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityBoundariesServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CityBoundariesService cityBoundariesService;

    @Test
    public void shouldReturnCityBoundaries() throws RestApiException {
        //given
        when(restTemplate.getForObject(any(URI.class),eq(GeometryCollection.class)))
                .thenReturn(getGeometryCollectionWithContent());
        ReflectionTestUtils.setField(cityBoundariesService,"geojsonApiQuery","localhost");
        //when
        CityGeography cityDetails = new CityGeography(new CityMetaData());
        GeometryCollection cityBoundariesFromApi = cityBoundariesService.getCityBoundariesFromApi(cityDetails);
        //then
        assertNotNull(cityBoundariesFromApi);

    }

    @Test(expected = RestApiNotFoundException.class)
    public void shouldThrowNotFoundException() throws RestApiException {
        //given
        when(restTemplate.getForObject(any(URI.class),eq(GeometryCollection.class)))
                .thenReturn(getGeometryCollectionWithoutContent());
        ReflectionTestUtils.setField(cityBoundariesService,"geojsonApiQuery","localhost");
        //when
        CityGeography cityDetails = new CityGeography(new CityMetaData());
        GeometryCollection cityBoundariesFromApi = cityBoundariesService.getCityBoundariesFromApi(cityDetails);

    }

    private GeometryCollection getGeometryCollectionWithoutContent() {
        return new GeometryCollection();
    }

    private GeometryCollection getGeometryCollectionWithContent() {
        GeometryCollection geometryCollection = new GeometryCollection();
        geometryCollection.setGeometries(List.of(new GeoJsonObject() {
            @Override
            public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
                return null;
            }
        }));
        return geometryCollection;
    }
}