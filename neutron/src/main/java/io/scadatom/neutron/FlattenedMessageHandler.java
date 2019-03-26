package io.scadatom.neutron;

import static io.scadatom.neutron.OpResult.FAILURE;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

public class FlattenedMessageHandler {
  private final Map<String, Function<FlattenedMessage, FlattenedMessage>> inboundRequestHandlers;

  public FlattenedMessageHandler(
      Map<String, Function<FlattenedMessage, FlattenedMessage>> inboundRequestHandlers) {
    this.inboundRequestHandlers = inboundRequestHandlers;
  }

  public String handle(String message) throws IOException {
    FlattenedMessage response;
    FlattenedMessage request = FlattenedMessage.inflate(message);
    Function<FlattenedMessage, FlattenedMessage> handler =
        inboundRequestHandlers.get(request.getTitle());
    if (handler != null) {
      response = handler.apply(request);
    } else {
      response = new FlattenedMessage(FAILURE + ":unknown intent " + request.getTitle());
    }
    return response.flat();
  }
}
