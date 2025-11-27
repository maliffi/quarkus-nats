package org.example.quarkus.nats.resource;

import com.fasterxml.jackson.annotation.JsonView;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.api.PublishMessageMetadata;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import org.example.quarkus.nats.model.Data;
import org.example.quarkus.nats.model.IncludeTimestamps;
import org.example.quarkus.nats.service.DataService;

import java.util.HashMap;
import java.util.List;

@Path("/data")
@Produces("application/json")
@ApplicationScoped
public class DataResource {

    @Inject
    DataService service;

    @Channel("data-producer")
    Emitter<Data> emitter;

    @GET
    @Path("/last")
    public Data getLast() {
        return service.getLast().orElseGet(Data::new);
    }

    @GET
    @Path("/last-with-timestamp")
    @JsonView(IncludeTimestamps.class)
    public Data getLastWithTimestamp() {
        return service.getLast().orElseGet(Data::new);
    }

    @POST
    @Consumes("application/json")
    @Path("/{messageId}")
    public Uni<Void> produceData(@PathParam("messageId") String messageId, Data data) {
        return Uni.createFrom().item(() -> emitData(messageId, data))
                .onItem().ignore().andContinueWithNull();
    }

    private Message<Data> emitData(String messageId, Data data) {
        final var headers = new HashMap<String, List<String>>();
        headers.put("RESOURCE_ID", List.of(data.getResourceId()));
        final var message = Message.of(data,
                Metadata.of(PublishMessageMetadata.builder().messageId(messageId).headers(headers).build()));
        emitter.send(message);
        return message;
    }
}