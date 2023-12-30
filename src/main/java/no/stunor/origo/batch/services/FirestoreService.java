package no.stunor.origo.batch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import lombok.extern.slf4j.Slf4j;
import no.stunor.origo.batch.model.Eventor;
import no.stunor.origo.batch.model.Organisation;
import no.stunor.origo.batch.model.Region;
    
@Slf4j
@Service
public record FirestoreService(Firestore firestore) {   
    
    public DocumentReference getEventorRefence(String eventorId) throws InterruptedException, ExecutionException{
        if (eventorId == null)
            return null;
        return firestore.collection("eventors").document(eventorId);
    }  

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

     public Organisation getOrganisation(DocumentReference eventorReference, String organisationNumber) throws InterruptedException, ExecutionException{
        Query query = firestore.collection("organisations")
                                .whereEqualTo("eventor", eventorReference)
                                .whereEqualTo("organisationNumber", organisationNumber);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        if(!querySnapshot.get().getDocuments().isEmpty()){
            return querySnapshot.get().getDocuments().get(0).toObject(Organisation.class);
        }
        return null;
    }

    public void createOrganisation(Organisation organisation) {
        firestore.collection("organisations").add(organisation);
    }

    public void updateOrganisation(Organisation organisation){
        firestore.collection("organisations").document(Objects.requireNonNull(organisation.getId())).set(organisation);
    }

    public void deleteNotUpdatedOrganisation(@Nonnull Timestamp deleteLimit) throws InterruptedException, ExecutionException{
        Query query = firestore.collection("organisations").whereLessThan("lastUpdated", deleteLimit);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            firestore.collection("organisations").document(document.getId()).delete();
        }
    }

    public Region getRegion(DocumentReference eventorReference, String organisationNumber) throws InterruptedException, ExecutionException{
        Query query = firestore.collection("regions")
                                .whereEqualTo("eventor", eventorReference)
                                .whereEqualTo("organisationNumber", organisationNumber);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        if(!querySnapshot.get().getDocuments().isEmpty()){
            return querySnapshot.get().getDocuments().get(0).toObject(Region.class);
        }
        
        return null;
    }

        public DocumentReference getRegionReference(DocumentReference eventorReference, String organisationNumber) throws InterruptedException, ExecutionException{
            Query query = firestore.collection("regions")
                                    .whereEqualTo("eventor", eventorReference)
                                    .whereEqualTo("organisationNumber", organisationNumber);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            if(!querySnapshot.get().getDocuments().isEmpty()){
                String regionId = querySnapshot.get().getDocuments().get(0).getId();
                if (regionId != null) {
                    return firestore.collection("regions").document(regionId);
                }
            }
            
            return null;
        }

    public void createRegion(Region region) {
        firestore.collection("regions").add(region);
    }

    public void updateRegion(Region region){
        firestore.collection("regions").document(Objects.requireNonNull(region.getId())).set(region);
    }

    public void deleteNotUpdatedRegions(@Nonnull Timestamp deleteLimit) throws InterruptedException, ExecutionException{
        Query query = firestore.collection("regions").whereLessThan("lastUpdated", deleteLimit);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            firestore.collection("regions").document(document.getId()).delete();
        }
    }
}
