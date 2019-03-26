package io.scadatom.neutron;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class FlattenedMessage {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  /**
   * for request, title is the string of intent for response, title is composed of
   * result:explanation1|explanation2...
   */
  private String title;

  private String payload;

  /**
   * to construct for outbound request
   *
   * @param title
   */
  public FlattenedMessage(String title) {
    this(title, null);
  }

  /**
   * to construct for outbound request, given string directly as payload
   * @param title
   * @param payload
   */
  public FlattenedMessage(String title, String payload) {
    this.title = title;
    this.payload = payload;
  }

  /**
   * to construct for outbound request, given object to be flattened inside
   *
   * @param title
   * @param objectPayload
   * @throws JsonProcessingException
   */
  public FlattenedMessage(String title, Object objectPayload) throws JsonProcessingException {
    this.title = title;
    this.payload = OBJECT_MAPPER.writeValueAsString(objectPayload);
  }

  /**
   * to construct from inbound text, payload is still text
   *
   * @param data
   * @return
   * @throws IOException
   */
  public static FlattenedMessage inflate(String data) throws IOException {
    return OBJECT_MAPPER.readValue(data, FlattenedMessage.class);
  }

  /**
   * to flat myself to text for outbound request
   *
   * @return
   * @throws JsonProcessingException
   */
  public String flat() throws JsonProcessingException {
    return OBJECT_MAPPER.writeValueAsString(this);
  }

  /**
   * to get the payload in object by given type
   * @param clazz
   * @param <T>
   * @return
   * @throws IOException
   */
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
