package com.back_cats.services;


import com.back_cats.exceptions.BorneException;
import com.back_cats.models.Borne;
import com.back_cats.repositories.BorneRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BorneService {

    @Autowired
    private BorneRepository borneRepository;

    public List<Borne> findAllBornes() {
        return borneRepository.findAll();
    }

    public Borne saveBorne(Borne borne) {
        return borneRepository.save(borne);
    }

    public Borne updateBorne(ObjectId id, Borne updatedBorne) throws BorneException {
        Borne borne = borneRepository.findById(id).orElseThrow(() ->
                new BorneException("Borne not found with id: " + id));

        borne.setStatus(updatedBorne.getStatus());
        borne.setCoord_x(updatedBorne.getCoord_x());
        borne.setCoord_y(updatedBorne.getCoord_y());
        borne.setTypeBorne(updatedBorne.getTypeBorne());

        return borneRepository.save(borne);
    }

    public Borne findBorneById(ObjectId id) throws BorneException {
        return borneRepository.findById(id).orElseThrow(() ->
                new BorneException("Borne not found with id: " + id));
    }

    public void deleteBorne(ObjectId id) throws BorneException {
        Borne borne = borneRepository.findById(id).orElseThrow(() ->
                new BorneException("Borne not found with id: " + id));
        borneRepository.delete(borne);
    }
}

