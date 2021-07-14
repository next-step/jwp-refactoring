package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroupEntity;

import java.util.Objects;

public class MenuGroupRequest {
  private String name;

  public MenuGroupRequest() {
  }

  public MenuGroupRequest(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public MenuGroupEntity toEntity() {
    return new MenuGroupEntity(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MenuGroupRequest that = (MenuGroupRequest) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
