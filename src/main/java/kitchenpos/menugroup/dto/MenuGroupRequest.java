package kitchenpos.menugroup.dto;

public class MenuGroupRequest {
    private Long id;
    private String name;

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

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
