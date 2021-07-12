package kitchenpos.menu.dto.dto;

import java.util.List;
import kitchenpos.menu.dto.CreateMenuDto;

import static java.util.stream.Collectors.toList;

public class CreateMenuRequest {

    private String name;
    private Long price;
    private Long menuGroupId;
    private List<CreateMenuProductRequest> menuProducts;

    public CreateMenuRequest() { }

    public CreateMenuRequest(String name, Long price, Long menuGroupId, List<CreateMenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<CreateMenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public CreateMenuDto toDomainDto() {
        return new CreateMenuDto(name, price, menuGroupId, menuProducts.stream()
                                                                       .map(CreateMenuProductRequest::toDomainDto)
                                                                       .collect(toList()));
    }
}
