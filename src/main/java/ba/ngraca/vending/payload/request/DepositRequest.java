package ba.ngraca.vending.payload.request;

import javax.validation.constraints.Min;

public class DepositRequest {
    @Min(0)
    private int fiveCentsCount;

    @Min(0)
    private int tenCentsCount;

    @Min(0)
    private int twentyCentsCount;

    @Min(0)
    private int fiftyCentsCount;

    @Min(0)
    private int hundredCentsCount;

    public int getFiveCentsCount() {
        return fiveCentsCount;
    }

    public void setFiveCentsCount(int fiveCentsCount) {
        this.fiveCentsCount = fiveCentsCount;
    }

    public int getTenCentsCount() {
        return tenCentsCount;
    }

    public void setTenCentsCount(int tenCentsCount) {
        this.tenCentsCount = tenCentsCount;
    }

    public int getTwentyCentsCount() {
        return twentyCentsCount;
    }

    public void setTwentyCentsCount(int twentyCentsCount) {
        this.twentyCentsCount = twentyCentsCount;
    }

    public int getFiftyCentsCount() {
        return fiftyCentsCount;
    }

    public void setFiftyCentsCount(int fiftyCentsCount) {
        this.fiftyCentsCount = fiftyCentsCount;
    }

    public int getHundredCentsCount() {
        return hundredCentsCount;
    }

    public void setHundredCentsCount(int hundredCentsCount) {
        this.hundredCentsCount = hundredCentsCount;
    }

    public int getTotal() {
        return (fiveCentsCount * 5) + (tenCentsCount * 10) + (twentyCentsCount * 20) + (fiftyCentsCount * 50) + (hundredCentsCount * 100);
    }
}
