package kitchenpos.menugroup.dto;

import java.math.BigDecimal;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

public class MenuGroupResponse {

    private Long id;
    private String name;



    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup){
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName().value());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
