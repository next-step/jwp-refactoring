package kitchenpos.order.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    @NotNull(message = "주문 테이블 정보는 필수잆니다.")
    private Long orderTableId;
    @Valid
    @NotNull(message = "메뉴 수량 정보는 필수값입니다.")
    @NotEmpty(message = "메뉴 수량 정보는 필수값입니다.")
    private List<MenuInfo> menuInfos;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<MenuInfo> menuInfos) {
        this.orderTableId = orderTableId;
        this.menuInfos = menuInfos;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<MenuInfo> getMenuInfos() {
        return menuInfos;
    }

    public List<Long> getMenuIds() {
        return menuInfos.stream()
                .map(MenuInfo::getMenuId)
                .distinct()
                .collect(Collectors.toList());
    }

    public static class MenuInfo {
        @NotNull(message = "메뉴정보는 필수값잆니다.")
        private Long menuId;
        @Positive(message = "메뉴 수량은 0 이상이어야 합니다.")
        private long quantity;

        public MenuInfo() {
        }

        public MenuInfo(Long menuId, long quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public Long getMenuId() {
            return menuId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
