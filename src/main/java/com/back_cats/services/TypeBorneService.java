package com.back_cats.services;

import com.back_cats.exceptions.TypeBorneException;
import com.back_cats.models.TypeBorne;
import com.back_cats.repositories.TypeBorneRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeBorneService {

    @Autowired
    private TypeBorneRepository typeBorneRepository;

    public List<TypeBorne> findAllTypes() {
        return typeBorneRepository.findAll();
    }

    public TypeBorne findTypeBorneById(ObjectId id) {
        return typeBorneRepository.findById(id)
                .orElseThrow(() -> new TypeBorneException("TypeBorne not found with id: " + id.toHexString()));
    }

    public TypeBorne saveTypeBorne(TypeBorne typeBorne) {
        try {
            return typeBorneRepository.save(typeBorne);
        } catch (DataIntegrityViolationException e) {
            throw new TypeBorneException("A TypeBorne with the name '" + typeBorne.getNom() + "' already exists.");

        }
    }

    public TypeBorne updateTypeBorne(ObjectId id, TypeBorne updatedTypeBorne) {
        TypeBorne typeBorne = findTypeBorneById(id);
        typeBorne.setNom(updatedTypeBorne.getNom());
        return typeBorneRepository.save(typeBorne);
    }

    public void deleteTypeBorne(ObjectId id) {
        TypeBorne typeBorne = findTypeBorneById(id);
        typeBorneRepository.delete(typeBorne);
    }
}
