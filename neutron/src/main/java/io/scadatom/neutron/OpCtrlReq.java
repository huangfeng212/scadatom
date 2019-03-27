package io.scadatom.neutron;

public class OpCtrlReq {

  private Long id;
  private String command;
  private String user;

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OpCtrlReq id(Long id) {
    setId(id);
    return this;
  }

  public OpCtrlReq command(String command) {
    setCommand(command);
    return this;
  }

  public OpCtrlReq user(String user) {
    setUser(user);
    return this;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("OpCtrlReq{");
    sb.append("id=").append(id);
    sb.append(", command='").append(command).append('\'');
    sb.append(", user='").append(user).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
