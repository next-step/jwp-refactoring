package kitchenpos.product.dto;

public class ProductResponse {
    private Long id;
    private String name;
    private Long price;

    @SuppressWarnings("unused")
    protected ProductResponse() { }

    public ProductResponse(String name, Long price) {
        this(null, name, price);
    }

    public ProductResponse(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(ProductDto dto) {
        return new ProductResponse(dto.getId(), dto.getName(), dto.getPrice());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}
