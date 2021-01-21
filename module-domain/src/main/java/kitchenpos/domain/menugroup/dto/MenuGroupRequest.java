package kitchenpos.domain.menugroup.dto;

public class MenuGroupRequest {
    private Long id;
    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public MenuGroupRequest setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuGroupRequest setName(final String name) {
        this.name = name;
        return this;
    }
}
