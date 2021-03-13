package vdt.hospital.patient

import com.mongodb.MongoClientSettings.Builder
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.bson.UuidRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import com.mongodb.MongoClientSettings

import com.mongodb.ConnectionString
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration

@Configuration
class MongoClientConfig(
    @Value("\${MONGO_HOST}") private val host: String,
    @Value("\${MONGO_PORT}") private val port: Int,
    @Value("\${MONGO_USERNAME}") private val username: String,
    @Value("\${MONGO_PASSWORD}") private val password: String,
    @Value("\${MONGO_DATABASE}") private val database: String,
    @Value("\${MONGO_USER_DATABASE}") private val userDatabase: String, //database where users are stored
) : AbstractReactiveMongoConfiguration() {
    override fun getDatabaseName(): String {
        return database
    }

    override fun configureClientSettings(builder: Builder) {
        builder
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .credential(MongoCredential.createCredential(username, userDatabase, password.toCharArray()))
            .applyToClusterSettings { settings -> settings.hosts(listOf(ServerAddress(host, port))) }
    }

}