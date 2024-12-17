package com.back_cats.services;

import com.back_cats.exceptions.CarteException;
import com.back_cats.exceptions.TypeBorneException;
import com.back_cats.models.Borne;
import com.back_cats.models.Carte;
import com.back_cats.models.TypeBorne;
import com.back_cats.repositories.BorneRepository;
import com.back_cats.repositories.CarteRepository;
import com.back_cats.repositories.TypeBorneRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BorneService {

    @Autowired
    private BorneRepository borneRepository;

    @Autowired
    private TypeBorneRepository typeBorneRepository;

    @Autowired
    private CarteRepository carteRepository;

    public Borne saveBorne(Borne borne) {
        if (borne.getTypeBorne() != null && borne.getTypeBorne().getId() != null) {
            ObjectId typeBorneId = new ObjectId(borne.getTypeBorne().getId());
            TypeBorne typeBorne = typeBorneRepository.findById(typeBorneId)
                    .orElseThrow(() -> new TypeBorneException("TypeBorne not found: " + borne.getTypeBorne().getId()));
            borne.setTypeBorne(typeBorne);
        }
        if (borne.getCarte() == null || borne.getCarte().getId() == null) {
            throw new CarteException("Carte must be provided and not null");
        }

        ObjectId carteId = new ObjectId(borne.getCarte().getId());
        Carte carte = carteRepository.findById(carteId)
                .orElseThrow(() -> new CarteException("Carte not found: " + borne.getCarte().getId()));
        borne.setCarte(carte);

        if (borne.getNumero() == null) {
            borne.setNumero(findAvailableNumero(carteId));
        }
        return borneRepository.save(borne);
    }

    public Integer findAvailableNumero(ObjectId carteId) {
        List<Borne> bornes = borneRepository.findByCarteId(carteId);
        if (bornes.isEmpty()) {
            return 1; // Commencez la numérotation à 1 si aucune borne n'est présente
        }

        // Triez les bornes par numéro de manière ascendante
        bornes.sort(Comparator.comparing(Borne::getNumero));

        // Trouvez le premier numéro "manquant" dans la séquence
        int expectedNumero = 1;
        for (Borne borne : bornes) {
            if (borne.getNumero() != expectedNumero) {
                return expectedNumero;
            }
            expectedNumero++;
        }

        // Si aucun numéro n'est manquant, assignez le prochain numéro disponible
        return expectedNumero;
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
