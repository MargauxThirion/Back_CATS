package com.back_cats.services;

import com.back_cats.models.Borne;
import com.back_cats.models.TypeBorne;
import com.back_cats.repositories.BorneRepository;
import com.back_cats.repositories.TypeBorneRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BorneService {

    @Autowired
    private BorneRepository borneRepository;

    @Autowired
    private TypeBorneRepository typeBorneRepository;

    public Borne saveBorne(Borne borne) {
        if (borne.getTypeBorne() != null && borne.getTypeBorne().getId() != null) {
            ObjectId typeBorneId = new ObjectId(borne.getTypeBorne().getId());
            TypeBorne typeBorne = typeBorneRepository.findById(typeBorneId)
                    .orElseThrow(() -> new RuntimeException("TypeBorne not found: " + borne.getTypeBorne().getId()));
            borne.setTypeBorne(typeBorne);
        }
        return borneRepository.save(borne);
    }


    public List<Borne> getAllBornes() {
        return borneRepository.findAll();
    }

    public Optional<Borne> getBorneById(ObjectId id) {
        return borneRepository.findById(id);
    }

    public void deleteBorne(ObjectId id) {
        borneRepository.deleteById(id);
    }
}
