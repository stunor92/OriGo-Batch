package no.stunor.origo.batch.services

import no.stunor.origo.batch.model.Eventor
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimestampConverter {
    companion object {
        private fun getTimeZone(eventor: Eventor): ZoneId {
            if (eventor.id == "AUS") {
                return ZoneId.of("Australia/Sydney")
            }
            return ZoneId.of("Europe/Paris")
        }

        private fun parseTimestamp(time: String, eventor: Eventor): ZonedDateTime {
            val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return ZonedDateTime.parse(time, sdf.withZone(getTimeZone(eventor)))

        }

        fun convertTimestamp(time: org.iof.eventor.ModifyDate, eventor: Eventor): Timestamp {
            val timeString = time.date.content + " " + time.clock.content
            val zdt = parseTimestamp(timeString, eventor)
            return Timestamp.from(zdt.toInstant())
        }
    }
}
