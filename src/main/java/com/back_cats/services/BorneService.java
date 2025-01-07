package com.back_cats.services;

import com.back_cats.exceptions.CarteException;
import com.back_cats.exceptions.TypeBorneException;
import com.back_cats.models.Borne;
import com.back_cats.models.Carte;
import com.back_cats.models.Reservation;
import com.back_cats.models.TypeBorne;
import com.back_cats.repositories.BorneRepository;
import com.back_cats.repositories.CarteRepository;
import com.back_cats.repositories.ReservationRepository;
import com.back_cats.repositories.TypeBorneRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class BorneService {

    @Autowired
    private BorneRepository borneRepository;

    @Autowired
    private TypeBorneRepository typeBorneRepository;

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;

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

    public List<Borne> getBornesByStatus(String status) {
        return borneRepository.findByStatus(status);
    }

    public Borne updateBorne(ObjectId id, Borne borne) {
        Borne existingBorne = borneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borne not found"));

        if (borne.getNumero() != null) {
            existingBorne.setNumero(borne.getNumero());
        }
        if (borne.getStatus() != null) {
            existingBorne.setStatus(borne.getStatus());
        }
        if (borne.getTypeBorne() != null) {
            existingBorne.setTypeBorne(borne.getTypeBorne());
        }
        if (borne.getCarte() != null) {
            existingBorne.setCarte(borne.getCarte());
        }

        return borneRepository.save(existingBorne);
    }

    public String normalizeKey(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }
    public Map<String, List<Borne>> getBornesStatus() {
        Date now = new Date();
        Date inThreeHours = new Date(now.getTime() + 3 * 60 * 60 * 1000);
        List<Borne> allBornes = borneRepository.findAll();
        List<Reservation> activeReservations = reservationService.getActiveReservationsForDate(now, inThreeHours);
        Set<String> occupiedIds = new HashSet<>();
        for (Reservation reservation : activeReservations) {
            occupiedIds.add(reservation.getBorne().getId());
        }

        Map<String, List<Borne>> statusMap = new HashMap<>();
        statusMap.put("disponible", new ArrayList<>());
        statusMap.put("occupee", new ArrayList<>());
        statusMap.put("hs", new ArrayList<>());
        statusMap.put("signalee", new ArrayList<>());

        for (Borne borne : allBornes) {
            String statusKey = normalizeKey(borne.getStatus()); // Utilisez la fonction de normalisation ici
            if (!statusMap.containsKey(statusKey)) {
                statusMap.put(statusKey, new ArrayList<>());
            }

            if (statusKey.equals("hs")) {
                statusMap.get(statusKey).add(borne);
            } else if (statusKey.equals("signalee")) {
                statusMap.get(statusKey).add(borne);
            } else if (statusKey.equals("fonctionnelle")) {
                if (occupiedIds.contains(borne.getId())) {
                    statusMap.get("occupee").add(borne);
                } else {
                    statusMap.get("disponible").add(borne);
                }
            }
        }
        statusMap.remove("fonctionnelle");
        return statusMap;
    }

    public Map<String, List<Borne>> getBornesStatusByDate(Date start, Date end) throws ParseException {
         List<Borne> allBornes = borneRepository.findAll();
        List<Reservation> activeReservations = reservationService.getActiveReservationsForDate(start, end);
        Set<String> occupiedIds = new HashSet<>();
        for (Reservation reservation : activeReservations) {
            occupiedIds.add(reservation.getBorne().getId());
        }

        Map<String, List<Borne>> statusMap = new HashMap<>();
        statusMap.put("disponible", new ArrayList<>());
        statusMap.put("occupee", new ArrayList<>());
        statusMap.put("hs", new ArrayList<>());
        statusMap.put("signalee", new ArrayList<>());

        for (Borne borne : allBornes) {
            String statusKey = normalizeKey(borne.getStatus());
            if (!statusMap.containsKey(statusKey)) {
                statusMap.put(statusKey, new ArrayList<>());
            }

            if (statusKey.equals("hs")) {
                statusMap.get(statusKey).add(borne);
            } else if (statusKey.equals("signalee")) {
                statusMap.get(statusKey).add(borne);
            } else if (statusKey.equals("fonctionnelle")) {
                if (occupiedIds.contains(borne.getId())) {
                    statusMap.get("occupee").add(borne);
                } else {
                    statusMap.get("disponible").add(borne);
                }
            }
        }
        statusMap.remove("fonctionnelle");
        return statusMap;
    }
}
