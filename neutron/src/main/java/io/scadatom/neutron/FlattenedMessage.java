package io.scadatom.neutron;

import static io.scadatom.neutron.OpResult.FAILURE;
import static io.scadatom.neutron.OpResult.SUCCESS;
import static io.scadatom.neutron.OpResult.TIMEOUT;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

public class FlattenedMessage {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  static {
    OBJECT_MAPPER.registerModule(new JavaTimeModule());
  }
  /**
   * for request, title is the string of intent for response, title is composed of
   * result:explanation1|explanation2...
   */
  private String title;

  private String payload;

  /** for jackson to use */
  public FlattenedMessage() {}

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
   *
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
   * Given the Object returned from rabbitTemplate.xxReceive() and parse the inner object
   *
   * @param resp
   * @param clazz
   * @param <T>
   * @return
   * @throws OpException
   */
  public static <T> T parseResp(Object resp, Class<T> clazz) throws OpException {
    if (resp == null) {
      throw new OpException(TIMEOUT);
    }
    try {
      FlattenedMessage flattenedMessage = FlattenedMessage.inflate(resp.toString());
      if (!flattenedMessage.getTitle().equals(SUCCESS)) {
        throw new OpException(flattenedMessage.getTitle()); // specific error
      } else if (clazz != Void.class) {
        return flattenedMessage.peel(clazz);
      }
      return null;
    } catch (IOException e) {
      throw new OpException(FAILURE + ":can not parse payload");
    }
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

  public String getTitle() {
    return title;
  }

  /**
   * to get the payload in object by given type
   *
   * @param clazz
   * @param <T>
   * @return
   * @throws IOException
   */
  public <T> T peel(Class<T> clazz) throws IOException {
    return OBJECT_MAPPER.readValue(payload, clazz);
  }

  public void setTitle(String title) {
    this.title = title;
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

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }
}
