package com.space.controller;

import com.space.exception.NotValidDataException;
import com.space.exception.ShipNotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipServiceImpl;
import com.space.utils.SpecificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest")
public class ShipController {

    private ShipServiceImpl shipService;

    public ShipController() {
    }

    @Autowired
    public ShipController(ShipServiceImpl shipService) {
        this.shipService = shipService;
    }

    @RequestMapping(value = "/ships", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Ship>> getAllShips(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        Specification<Ship> shipSpecification = SpecificationFactory.getSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        List<Ship> ships = shipService.getAllShips(shipSpecification, pageable).getContent();

        return new ResponseEntity<>(ships, HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Integer> getAllShips(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating
    ) {
        Specification<Ship> shipSpecification = SpecificationFactory.getSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        Integer shipsCount = shipService.getAllShips(shipSpecification).size();

        return new ResponseEntity<>(shipsCount, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/ships", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {

        if (ship.getUsed() == null) ship.setUsed(false);
        if (!shipService.isParamSetValid(ship))
            throw new NotValidDataException("Parameter set is not valid");

        return new ResponseEntity<>(shipService.createShip(ship), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/ships/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> getShip(@PathVariable("id") Long id) {

        if (!shipService.isIdValid(id))
            throw new NotValidDataException("Id is not valid");

        if (!shipService.isShipExistsById(id))
            throw new ShipNotFoundException("Ship with defined id is not exist");

        return new ResponseEntity<>(shipService.getShipById(id), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/ships/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> updateShip(@PathVariable("id") Long id, @RequestBody Ship ship) {

        if (!shipService.isIdValid(id))
            throw new NotValidDataException("Id is not valid");

        if (!shipService.isShipExistsById(id))
            throw new ShipNotFoundException("Ship with defined id is not exist");

        return new ResponseEntity<>(shipService.updateShip(id, ship), HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") Long id) {

        if (!shipService.isIdValid(id))
            throw new NotValidDataException("Id is not valid");

        shipService.deleteShipById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
