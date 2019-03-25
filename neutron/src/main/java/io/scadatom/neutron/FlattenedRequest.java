package io.scadatom.neutron;

public class FlattenedRequest {

  private String intent;
  private String payload;

  public FlattenedRequest() {}

  public FlattenedRequest(String intent, String payload) {
    this.intent = intent;
    this.payload = payload;
  }

  public String getIntent() {
    return intent;
  }

  public void setIntent(String intent) {
    this.intent = intent;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }
}
