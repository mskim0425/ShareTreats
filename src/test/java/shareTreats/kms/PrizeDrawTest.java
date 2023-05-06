package shareTreats.kms;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shareTreats.kms.prizedraw.PrizeDraw;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;


class PrizeDrawTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    PrizeDraw prizeDraw;

    @BeforeEach
    public void setUpStreams() {
        prizeDraw = new PrizeDraw();
        System.setOut(new PrintStream(outContent));
    }
    //요-1번, 2번, 4번, 5번,7번, 10번,11번
    @Test
    @DisplayName("100원씩 차감되는지, 뽑기가 잘작동되는지 20번씩 총 60번")
    void deduction(){
//        given: 10000원을 가진 PrizeDraw 객체 생성
//        when: 20번 뽑기를 시도하고, 가상 지갑에 2000원 충전
        prizeDraw.draw(20,LocalDateTime.now());
        prizeDraw.charge(2000);

        assertThat(outContent.toString(), anyOf(
                containsString("회차 : A등급의"),
                containsString("회차 : B등급의"),
                containsString("회차 : 꽝!")));
        outContent.reset();

        prizeDraw.draw(20,LocalDateTime.now());
        prizeDraw.charge(2000);

        assertThat(outContent.toString(), anyOf(
                containsString("회차 : A등급의"),
                containsString("회차 : B등급의"),
                containsString("회차 : 꽝!")));
        outContent.reset();

        prizeDraw.draw(20,LocalDateTime.now());
        prizeDraw.charge(2000);

        assertThat(outContent.toString(), anyOf(
                containsString("회차 : A등급의"),
                containsString("회차 : B등급의"),
                containsString("회차 : 꽝!")));
        outContent.reset();
//        then
        prizeDraw.info();
        assertEquals("현재 잔액: 10000\r\n", outContent.toString());
    }
    //요-3번
    @Test
    @DisplayName("충전이 잘되는지")
    void charge(){
        prizeDraw.charge(10000);
        assertEquals("현재 잔액: 20000\r\n", outContent.toString());
    }


    @Test
    @DisplayName("뽑기횟수가 0미만인경우")
    void draw_negativeNumberOfTickets() {

        prizeDraw.draw(-1,LocalDateTime.now());
        assertEquals("0회이상 실행할 수 있습니다.\r\n", outContent.toString());
    }
    
    //요-6번,7번,9번
    @Test
    @DisplayName("B상품은 최대 3번")
    void BProduct_max(){
        // 5번당첨 주어짐 2번은 무효 게임금액 반환
        for (int i = 0; i < 5; i++) {
            prizeDraw.deduction(100);
            prizeDraw.drawProduct("B", LocalDateTime.now());
        }
        assertThat(outContent.toString(), not(containsString("PIZZA"))); //B등급 기준의 유통기한이 지난 음식
        outContent.reset();
//        then
        prizeDraw.info();
        assertEquals("현재 잔액: 9700\r\n", outContent.toString());
    }
}