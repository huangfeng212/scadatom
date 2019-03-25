package io.scadatom.neutron;

import java.io.Serializable;
import java.util.Objects;

public class EntityRef implements Serializable {

  private Long id;
  private String name;

  public EntityRef() {
  }

  public EntityRef(Long id) {
    this.id = id;
  }

  public EntityRef(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final EntityRef entityRef = (EntityRef) o;
    return Objects.equals(id, entityRef.id) && Objects.equals(name, entityRef.name);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EntityRef{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
