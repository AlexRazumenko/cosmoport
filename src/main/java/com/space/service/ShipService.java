package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


import java.util.List;

public interface ShipService {
    Page<Ship> getAllShips(Specification<Ship> specification, Pageable pageable);

    List<Ship> getAllShips(Specification<Ship> specification);

    Ship createShip (Ship ship);

    Ship updateShip (Long id, Ship ship);

    void deleteShipById (Long id);

    Ship getShipById (Long id);

    Double calculateRating (Ship ship);

    boolean isIdValid (Long id);

    boolean isShipExistsById (Long id);

    boolean isParamSetValid (Ship ship);
}
