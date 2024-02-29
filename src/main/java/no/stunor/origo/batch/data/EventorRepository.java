package no.stunor.origo.batch.data;

import org.springframework.stereotype.Repository;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;

import no.stunor.origo.batch.model.Eventor;

@Repository
public interface EventorRepository extends FirestoreReactiveRepository<Eventor> {
}