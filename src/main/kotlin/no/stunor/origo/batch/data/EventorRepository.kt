package no.stunor.origo.batch.data

import no.stunor.origo.batch.model.Eventor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventorRepository : CrudRepository<Eventor, String>