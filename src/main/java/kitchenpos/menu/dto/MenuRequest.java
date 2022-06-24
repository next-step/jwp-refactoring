package kitchenpos.menu.dto;

public class MenuRequest {
    private String name;
    private long price;
    private Long menuGroupId;
    private Long productId;
    private long quantity;

    public MenuRequest() {}

    public MenuRequest(String name, long price, Long menuGroupId, Long productId, long quantity) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
