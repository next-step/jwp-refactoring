package kitchenpos.dto;

public class MenuGroupDto {
    private Long id;
    private String name;

    protected MenuGroupDto() {
    }

    private MenuGroupDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupDto of(Long id, String name) {
        return new MenuGroupDto(id, name);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
