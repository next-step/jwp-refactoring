package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.StringUtils;

@Embeddable
public class OrderLineItemMenuName {

    @Column(name = "menu_name", length = 255, nullable = false)
    private String menuName;

    protected OrderLineItemMenuName() {
    }

    public OrderLineItemMenuName(String menuName) {
        if (!StringUtils.hasText(menuName)) {
            throw new IllegalArgumentException("메뉴의 이름은 반드시 입력되어야 합니다[menuName:" + menuName + "]");
        }
        this.menuName = menuName;
    }

    public String getMenuName() {
        return menuName;
    }
}
