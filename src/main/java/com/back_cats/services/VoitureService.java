package com.back_cats.services;

import com.back_cats.exceptions.VoitureException;
import com.back_cats.models.Voiture;
import com.back_cats.repositories.VoitureRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VoitureService {

    @Autowired
    private VoitureRepository voitureRepository;

    public List<Voiture> findAllVoitures() {
        return voitureRepository.findAll();
    }

    public Voiture saveVoiture(Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    public Voiture updateVoiture(ObjectId id, Voiture updatedVoiture) {
        return voitureRepository.findById(id)
                .map(v -> {
                    v.setMarque(updatedVoiture.getMarque());
                    v.setModele(updatedVoiture.getModele());
                    v.setAnnee(updatedVoiture.getAnnee());
                    v.setConso(updatedVoiture.getConso());
                    return voitureRepository.save(v);
                })
                .orElseThrow(() -> new VoitureException("Voiture not found with id: " + id));
    }

    public Voiture findVoitureById(ObjectId id) {
        return voitureRepository.findById(id)
                .orElseThrow(() -> new VoitureException("Voiture not found with id: " + id));
    }

    public void deleteVoiture(ObjectId id) {
        Voiture voiture = findVoitureById(id);
        voitureRepository.delete(voiture);
    }
}
