package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {
    private final Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();
        var beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Beer 1")
                .beerStyle(BeerStyle.ALE)
                .upc("1234134123")
                .price(new BigDecimal("20.20"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        var beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Beer 2")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("43252352")
                .price(new BigDecimal("10.10"))
                .quantityOnHand(221)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        var beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Beer 3")
                .beerStyle(BeerStyle.LAGER)
                .upc("45627809")
                .price(new BigDecimal("30.30"))
                .quantityOnHand(50)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Getting beer by id - {}", id.toString());
        return beerMap.get(id);
    }

    @Override
    public List<Beer> listBeers() {
        log.debug("Returning a list of all available beers. Qty - {}", beerMap.size());
        return new ArrayList<>(beerMap.values());
    }
}
