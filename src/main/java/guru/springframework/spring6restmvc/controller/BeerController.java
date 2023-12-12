package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerById(UUID id) {
        return beerService.getBeerById(id);
    }

}

