package kitchenpos.menu.dto;

public class MenuProductResponse {
    private Long seq;
    private String productName;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, String productName, long quantity) {
        this.seq = seq;
        this.productName = productName;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public String getProductName() {
        return productName;
    }

    public long getQuantity() {
        return quantity;
    }
}
