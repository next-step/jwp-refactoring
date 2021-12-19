package kitchenpos.menugroup.dto;

public class MenuGroupDto {
	private Long id;
	private String name;

	public MenuGroupDto() {
	}

	public MenuGroupDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
