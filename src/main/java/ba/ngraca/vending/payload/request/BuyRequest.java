package ba.ngraca.vending.payload.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class BuyRequest {
    @NotNull
    @Min(1)
    private Long productId;

    @NotNull
    @Min(1)
    private Integer amount;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
