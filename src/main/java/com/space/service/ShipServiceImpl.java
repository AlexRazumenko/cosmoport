package com.space.service;


import com.space.exception.NotValidDataException;
import com.space.exception.ShipNotFoundException;
import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {

    private ShipRepository shipRepository;

    public ShipServiceImpl() {
    }

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Page<Ship> getAllShips(Specification<Ship> specification, Pageable pageable) {
        return shipRepository.findAll(specification, pageable);
    }

    @Override
    public List<Ship> getAllShips(Specification<Ship> specification) {
        return shipRepository.findAll(specification);
    }

    @Override
    public Ship getShipById(Long id) {
        if (!shipRepository.existsById(id))
            throw new ShipNotFoundException("No ship present");
        return shipRepository.findById(id).get();
    }

    @Override
    public Ship updateShip(Long id, Ship ship) {
        if (!shipRepository.existsById(id))
            throw new NotValidDataException("Parameter set is not valid");

        Ship shipToUpdate = shipRepository.findById(id).get();

        if (ship.getName() != null) {
            if (ship.getName().isEmpty() || ship.getName().length() > 50)
                throw new NotValidDataException("Parameter set is not valid");
            shipToUpdate.setName(ship.getName());
        }

        if (ship.getPlanet() != null) {
            if (ship.getPlanet().isEmpty() || ship.getPlanet().length() > 50)
                throw new NotValidDataException("Parameter set is not valid");
            shipToUpdate.setPlanet(ship.getPlanet());
        }

        if (ship.getProdDate() != null) {
            Calendar prodDateAsCalendar = new GregorianCalendar();
            prodDateAsCalendar.setTime(ship.getProdDate());
            int prodYear = prodDateAsCalendar.get(Calendar.YEAR);
            if (prodYear < 2800 || prodYear > 3019)
                throw new NotValidDataException("Parameter set is not valid");
            shipToUpdate.setProdDate(ship.getProdDate());
        }

        if (ship.getShipType() != null)
            shipToUpdate.setShipType(ship.getShipType());

        if (ship.getUsed() != null)
            shipToUpdate.setUsed(ship.getUsed());

        if (ship.getSpeed() != null) {
            if (ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99)
                throw new NotValidDataException("Parameter set is not valid");
            shipToUpdate.setSpeed(ship.getSpeed());
        }

        if (ship.getCrewSize() != null) {
            if (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999)
                throw new NotValidDataException("Parameter set is not valid");
            shipToUpdate.setCrewSize(ship.getCrewSize());
        }

        shipToUpdate.setRating(calculateRating(shipToUpdate));
        return shipRepository.save(shipToUpdate);
    }

    @Override
    public void deleteShipById(Long id) {
        if (!shipRepository.existsById(id))
            throw new ShipNotFoundException("No ship present");
        else shipRepository.deleteById(id);
    }

    @Override
    public boolean isIdValid(Long id) {
        return id > 0;
    }

    @Override
    public boolean isShipExistsById(Long id) {
        return shipRepository.existsById(id);
    }

    @Override
    public Ship createShip(Ship ship) {
        if (ship.getName() == null
                || ship.getPlanet() == null
                || ship.getShipType() == null
                || ship.getProdDate() == null
                || ship.getSpeed() == null
                || ship.getCrewSize() == null)
            throw new NotValidDataException("Parameter set is not valid");

        if (ship.getUsed() == null)
            ship.setUsed(false);

        ship.setRating(calculateRating(ship));
        return shipRepository.saveAndFlush(ship);
    }

    @Override
    public Double calculateRating(Ship ship) {
        Calendar prodDateAsCalendar = new GregorianCalendar();
        prodDateAsCalendar.setTime(ship.getProdDate());
        BigDecimal ratingAsBigDecimal = new BigDecimal((80 * ship.getSpeed() * (ship.getUsed() ? 0.5 : 1.0)) / (3019d - prodDateAsCalendar.get(Calendar.YEAR) + 1));
        ratingAsBigDecimal = ratingAsBigDecimal.setScale(2, RoundingMode.HALF_UP);
        return ratingAsBigDecimal.doubleValue();
    }

    @Override
    public boolean isParamSetValid(Ship ship) {

        if ((ship.getName() == null) || (ship.getName().isEmpty()) || (ship.getName().length() > 50)) return false;
        if ((ship.getPlanet() == null) || (ship.getPlanet().isEmpty()) || (ship.getPlanet().length() > 50))
            return false;
        if (ship.getShipType() == null) ship.setUsed(false);
        if ((ship.getProdDate()) == null) return false;
        else {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(ship.getProdDate());
            int prodYear = calendar.get(Calendar.YEAR);
            if ((prodYear < 2800) || (prodYear > 3019)) return false;
        }
        if (ship.getUsed() == null) ship.setUsed(false);
        if ((ship.getSpeed() == null) || (ship.getSpeed() < 0.01) || (ship.getSpeed() > 0.99)) return false;
        if ((ship.getCrewSize() == null) || (ship.getCrewSize() < 1) || (ship.getCrewSize() > 9999)) return false;
        return true;
    }
}
