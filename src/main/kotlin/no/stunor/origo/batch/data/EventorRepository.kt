package no.stunor.origo.batch.data

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository
import no.stunor.origo.batch.model.Eventor
import org.springframework.stereotype.Repository

@Repository
interface EventorRepository : FirestoreReactiveRepository<Eventor>