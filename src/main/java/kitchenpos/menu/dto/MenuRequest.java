package kitchenpos.menu.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {

    @NotNull(message = "메뉴 이름은 필수값입니다.")
    private String name;
    @NotNull(message = "메뉴 가격은 필수값입니다.")
    private BigDecimal price;
    @NotNull(message = "메뉴 그룹정보는 필수입니다.")
    private Long menuGroupId;
    @Valid
    @NotNull
    @NotEmpty
    private List<ProductInfo> productInfos;

    public MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<ProductInfo> productInfos) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productInfos = productInfos;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<ProductInfo> getProductInfos() {
        return productInfos;
    }

    public List<Long> getProductIds() {
        return productInfos.stream()
                .map(ProductInfo::getProductId)
                .distinct()
                .collect(Collectors.toList());
    }


    public static class ProductInfo {
        @NotNull(message = "상품 정보는 필수값입니다.")
        private Long productId;
        @Positive(message = "상품 수량은 0 이상이어야 합니다.")
        private int quantity;

        public ProductInfo() {
        }

        public ProductInfo(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
