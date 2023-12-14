package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(BeerController.BEER_PATH)
public class BeerController {
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_ID = "beerId";
    public static final String BEER_ID_PATH = "/{" + BEER_ID + "}";
    private final BeerService beerService;

    @PatchMapping(BEER_ID_PATH)
    public ResponseEntity<Void> updateBeerPatchById(@PathVariable(BEER_ID) UUID beerId, @RequestBody Beer beer) {
        beerService.patchById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_ID_PATH)
    public ResponseEntity<Void> deleteById(@PathVariable(BEER_ID) UUID beerId) {
        beerService.deleteBeerById(beerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_ID_PATH)
    public ResponseEntity<Void> updateById(@PathVariable(BEER_ID) UUID beerId, @RequestBody Beer beer) {
        beerService.updateBeerById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping()
    public ResponseEntity<Void> handlePost(@RequestBody Beer beer) {
        var savedBeer = beerService.saveNewBeer(beer);

        var headers = new HttpHeaders();
        var location = BEER_PATH + "/" + savedBeer.getId()
                                                  .toString();
        headers.add("Location", location);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping()
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }


    @GetMapping(value = BEER_ID_PATH)
    public Beer getBeerById(@PathVariable(BEER_ID) UUID id) {
        return beerService.getBeerById(id)
                          .orElseThrow(NotFoundException::new);
    }
}
