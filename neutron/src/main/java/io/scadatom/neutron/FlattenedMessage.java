package io.scadatom.neutron;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class FlattenedMessage {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private String title;
  private String payload;

  public FlattenedMessage() {}

  public FlattenedMessage(String title, String payload) {
    this.title = title;
    this.payload = payload;
  }

  public FlattenedMessage(String title) {
    this(title,null);
  }

  public FlattenedMessage(String title, Object objectPayload) throws JsonProcessingException {
    this.title = title;
    this.payload = OBJECT_MAPPER.writeValueAsString(objectPayload);
  }

  public static FlattenedMessage inflate(String data) throws IOException {
    return OBJECT_MAPPER.readValue(data, FlattenedMessage.class);
  }

  public String flat() throws JsonProcessingException {
    return OBJECT_MAPPER.writeValueAsString(this);
  }

  public <T> T peel(Class<T> clazz) throws IOException {
    return OBJECT_MAPPER.readValue(payload, clazz);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }
}
