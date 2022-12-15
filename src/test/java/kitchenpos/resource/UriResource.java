package kitchenpos.resource;

public enum UriResource {
    상품_API("/api/products");

    private final String uri;

    UriResource(String uri) {
        this.uri = uri;
    }

    public String uri() {
        return uri;
    }
}
