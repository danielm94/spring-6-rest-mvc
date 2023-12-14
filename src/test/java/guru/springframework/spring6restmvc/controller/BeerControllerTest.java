package guru.springframework.spring6restmvc.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static guru.springframework.spring6restmvc.controller.BeerController.BEER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
    public static final String BEER_NAME_KEY = "beerName";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BeerService beerService;

    private BeerServiceImpl beerServiceImpl;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    private ArgumentCaptor<Beer> beerArgumentCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(getEndpointWithBeerId(UUID.randomUUID())))
               .andExpect(status().isNotFound());
    }

    @Test
    void patchBeerWithSinglePropertyTest() throws Exception {
        var beer = beerServiceImpl.listBeers()
                                  .getFirst();
        var endpoint = getEndpointWithBeerId(beer.getId());

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put(BEER_NAME_KEY, "New Name");

        mockMvc.perform(patch(endpoint)
                       .accept(MediaType.APPLICATION_JSON)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(beerMap)))
               .andExpect(status().isNoContent());

        verify(beerService).patchById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get(BEER_NAME_KEY)).isEqualTo(beerArgumentCaptor.getValue()
                                                                           .getBeerName());
    }

    @Test
    void patchBeerWithAllPropertiesTest() throws Exception {
        var beer = beerServiceImpl.listBeers()
                                  .getFirst();
        var endpoint = getEndpointWithBeerId(beer.getId());

        mockMvc.perform(patch(endpoint)
                       .accept(MediaType.APPLICATION_JSON)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(beer)))
               .andExpect(status().isNoContent());

        verify(beerService).patchById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beer).isEqualTo(beerArgumentCaptor.getValue());
    }

    @Test
    void deleteBeerTest() throws Exception {
        var beer = beerServiceImpl.listBeers()
                                  .getFirst();
        var endpoint = getEndpointWithBeerId(beer.getId());

        mockMvc.perform(delete(endpoint)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());

        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void updateBeerTest() throws Exception {
        var beer = beerServiceImpl.listBeers()
                                  .getFirst();
        var endpoint = getEndpointWithBeerId(beer.getId());

        mockMvc.perform(put(endpoint)
                       .accept(MediaType.APPLICATION_JSON)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(beer)))
               .andExpect(status().isNoContent());


        verify(beerService).updateBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void testCreateNewBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers()
                                   .getFirst();
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.saveNewBeer(any(Beer.class)))
                .willReturn(beerServiceImpl.listBeers()
                                           .get(1));

        mockMvc.perform(post(BEER_PATH)
                       .accept(MediaType.APPLICATION_JSON)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(beer)))
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"));
    }

    @Test
    void getBeerById() throws Exception {
        Beer beer = beerServiceImpl.listBeers()
                                   .getFirst();
        var endpoint = getEndpointWithBeerId(beer.getId());
        given(beerService.getBeerById(beer.getId()))
                .willReturn(Optional.of(beer));

        mockMvc.perform(get(endpoint)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", is(beer.getId()
                                                  .toString())))
               .andExpect(jsonPath("$.beerName", is(beer.getBeerName())));
    }

    @Test
    void listBeersTest() throws Exception {
        var beerList = beerServiceImpl.listBeers();

        given(beerService.listBeers())
                .willReturn(beerList);

        mockMvc.perform(get(BEER_PATH)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.length()", is(beerList.size())));
    }

    private static String getEndpointWithBeerId(UUID id) {
        return BEER_PATH + "/" + id;
    }
}