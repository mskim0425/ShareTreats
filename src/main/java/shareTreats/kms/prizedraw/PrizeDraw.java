package shareTreats.kms.prizedraw;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class PrizeDraw {
    private final int TICKET_PRICE = 100; // 뽑기 1회당 차감되는 돈
    private int wallet = 0; // 가상 지갑

    private final Map<String, List<Prize>> prizesByGrade; // 등급별

    private final Random random;
    private int max = 0;

    public PrizeDraw() {
        this.prizesByGrade = new HashMap<>();
        this.random = new Random();
        this.wallet = 10000; //초기값 만원
        // 개발자가 임의로 제공하는 10종류의 상품 생성
        List<Prize> prizes = Arrays.asList(
                new Prize("CHICKEN", "B", LocalDateTime.parse("2023-12-31T02:20:19")),
                new Prize("CIDER", "A", LocalDateTime.parse("2023-12-31T02:28:56")),
                new Prize("COLA", "A", LocalDateTime.parse("2023-12-31T01:00:32")),
                new Prize("COOKIE", "B", LocalDateTime.parse("2023-12-31T22:11:10")),
                new Prize("CRACKER", "B", LocalDateTime.parse("2023-12-31T23:59:59")),
                new Prize("FRIES", "B", LocalDateTime.parse("2023-12-31T14:30:00")),
                new Prize("HOTDOG", "A", LocalDateTime.parse("2023-01-01T00:00:00")),
                new Prize("PIZZA", "B", LocalDateTime.parse("2023-01-01T08:45:30")),
                new Prize("SANDWICH", "A", LocalDateTime.parse("2023-01-01T11:11:11")),
                new Prize("SODA", "A", LocalDateTime.parse("2023-01-01T21:45:00"))
        );

        // 상품 등급별로 목록 저장
        for (Prize prize : prizes) {
            String grade = prize.getGrade();
            if (!prizesByGrade.containsKey(grade)) {
                prizesByGrade.put(grade, new ArrayList<>());
            }
            prizesByGrade.get(grade).add(prize);
        }
    }

    // 고객이 돈을 충전하는 함수
    public void charge(int amount) {
        wallet += amount;
        info();
    }
    public void deduction(int amount){
        wallet -= amount;}

    public void draw(int opportunity, LocalDateTime dateTime) {
        int paid = opportunity * 100;
        if (paid > wallet) {
            System.out.println("잔액이 부족합니다. 충전을 해주세요");
            return;
        } else if ( opportunity < 1) {
            System.out.println("0회이상 실행할 수 있습니다.");
        }
        deduction(paid);//차감

        //10. A 상품의 확률을 먼저 확인하고 뽑히지 않는다면 B 상품의 뽑기를 시도 합니다. TODO: 2번서비스 요구사항 8, 10위치
        for (int i = 0; i < opportunity; i++) {
            System.out.print(String.format("%02d회차 : ",i+1));
            int randNum = (int) (Math.random() * 100); //0~99
            if (randNum < 90)  // A 상품 뽑기 90%확률
                drawProduct("A", dateTime);
            else {
                randNum = (int) (Math.random() * 100);
                if (randNum < 100 && randNum >= 90)  // B 상품 뽑기 10%확률
                    drawProduct("B", dateTime);
                else
                    System.out.println("꽝!");
            }
        }
    }
    //test를 위해 public
    public void drawProduct(String grade, LocalDateTime dateTime) {
        List<Prize> list = prizesByGrade.get(grade);
        Collections.shuffle(list);

        for(Prize prize:list) { //무작위로 주되, 유통기한이 지난 상품은 pass
            if (prize.time.isAfter(dateTime)) {
                if (max == 3 && grade.equals("B")) {
                    System.out.println("B 상품은 최대 3번까지만 뽑힙니다. 따라서 100원이 환불됩니다.");
                    charge(100);
                    break;
                } else {
                    if (grade.equals("B")) max++;
                    System.out.println(prize.grade + "등급의 " + prize.product + "가 뽑혔습니다!");
                    break;
                }
            }

        }

    }

    public void info() {
        System.out.println("현재 잔액: "+wallet);
    }
}
