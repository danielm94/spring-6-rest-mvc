package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public Optional<Beer> getBeerById(UUID id) {
        log.debug("Getting beer by id - {}", id.toString());
        return Optional.of(beerMap.get(id));
    }

    @Override
    public List<Beer> listBeers() {
        log.debug("Returning a list of all available beers. Qty - {}", beerMap.size());
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        var savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public void updateBeerById(UUID beerId, Beer beer) {
        var savedBeer = beerMap.get(beerId);
        savedBeer.setBeerName(beer.getBeerName());
        savedBeer.setPrice(beer.getPrice());
        savedBeer.setBeerStyle(beer.getBeerStyle());
        savedBeer.setUpc(beer.getUpc());
        savedBeer.setQuantityOnHand(beer.getQuantityOnHand());
        savedBeer.setUpdateDate(LocalDateTime.now());
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void patchById(UUID beerId, Beer beer) {
        var existing = beerMap.get(beerId);
        var changesMade = false;

        if (StringUtils.hasText(beer.getBeerName())) {
            existing.setBeerName(beer.getBeerName());
            changesMade = true;
        }

        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
            changesMade = true;
        }

        if (StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
            changesMade = true;
        }

        if (beer.getQuantityOnHand() != null) {
            existing.setQuantityOnHand(beer.getQuantityOnHand());
            changesMade = true;
        }

        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
            changesMade = true;
        }

        if (changesMade) {
            existing.setUpdateDate(LocalDateTime.now());
        }
    }
}
