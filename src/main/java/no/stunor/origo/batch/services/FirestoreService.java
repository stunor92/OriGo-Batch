package no.stunor.origo.batch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Organisation;
import no.stunor.origo.batch.model.Region;
    
@Slf4j
@Service
public record FirestoreService(Firestore firestore) {    


     public List<Eventor> getEventorList() throws InterruptedException, ExecutionException{
        log.info("Start to fetch evnetorList.");
        List<Eventor> eventorList = new ArrayList<>();

        ApiFuture<QuerySnapshot> future = firestore.collection("eventors").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            eventorList.add(document.toObject(Eventor.class));
        }
        return eventorList;
    }

        public List<Region> getRegionList(Eventor eventor){
        log.info("Start to fecth all regions.");

        List<Region> regionList = new ArrayList<>();
       
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("eventors").document(eventor.getId()).collection("regions").get();
            List<QueryDocumentSnapshot> documents;
            documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                regionList.add(document.toObject(Region.class));
            }
        } catch (Exception e) {
            log.warn("Problems to fettch regions grom Firestore");
        }
        
        return regionList;
    }

    public List<Organisation> getOrganisationList(Eventor eventor){
        log.info("Start to fecth all organisatopns.");

        List<Organisation> organisationList = new ArrayList<>();

        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("eventors").document(eventor.getId()).collection("organisations").get();
            List<QueryDocumentSnapshot> documents;
            documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                System.out.println(document.getId() + " => " + document.toObject(Organisation.class));
                organisationList.add(document.toObject(Organisation.class));
            }
        } catch (Exception e) {
            log.warn("Problems to fettch regions grom Firestore");
        }
        return organisationList;
    }

    public void updateRegion(Region region, Eventor eventor){
        firestore.collection("eventors").document(eventor.getId()).collection("regions").document(region.getId()).set(region);
    }

    public void deleteRegion(Region region, Eventor eventor){
        firestore.collection("eventors").document(eventor.getId()).collection("regions").document(region.getId()).delete();
    }

      public void updateOrganisation(Organisation organisation, Eventor eventor){
        firestore.collection("eventors").document(eventor.getId()).collection("organisations").document(organisation.getId()).set(organisation);
    }

    public void deleteOrganisation(Organisation organisation, Eventor eventor){
        firestore.collection("eventors").document(eventor.getId()).collection("organisations").document(organisation.getId()).delete();
    }
}
