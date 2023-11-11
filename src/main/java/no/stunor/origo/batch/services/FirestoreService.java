package no.stunor.origo.batch.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
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

    public void updateRegion(Region region){
        firestore.collection("regions").document(region.getId()).set(region);
    }

    public void updateOrganisation(Organisation organisation){
        firestore.collection("organisations").document(organisation.getId()).set(organisation);
    }

    public void deleteNotUpdatedRegions(@Nonnull Timestamp deleteLimit) throws InterruptedException, ExecutionException{
        Query query = firestore.collection("regions").whereLessThan("lastUpdated", deleteLimit);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            firestore.collection("regions").document(document.getId()).delete();
        }
    }
    public void deleteNotUpdatedOrganisation(@Nonnull Timestamp deleteLimit) throws InterruptedException, ExecutionException{
        Query query = firestore.collection("organisations").whereLessThan("lastUpdated", deleteLimit);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            firestore.collection("organisations").document(document.getId()).delete();
        }
    }
}
