package shareTreats.kms.prizedraw;

import java.time.LocalDateTime;

public class Prize {
    String product;
    String grade;
    LocalDateTime time;


    public Prize(String product, String grade, LocalDateTime time) {
        this.product = product;
        this.grade = grade;
        this.time = time;
    }

    public String getProduct() {
        return product;
    }

    public String getGrade() {
        return grade;
    }

    public LocalDateTime getTime() {
        return time;
    }


}
