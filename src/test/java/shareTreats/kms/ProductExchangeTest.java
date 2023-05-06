package shareTreats.kms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shareTreats.kms.productExchange.ProductExchange;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ProductExchangeTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    ProductExchange productExchange;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        productExchange = new ProductExchange();
    }

    //요-1, 3, 5
    @Test
    @DisplayName("CHECK 명령어로 조회가능")
    void testCheck(){

        String[] inputArr = {"CHECK", "001", "2"};
        productExchange.check(inputArr);

        assertEquals("000000001는 교환 가능합니다.\r\n000000002는 교환 가능합니다.\r\n", outContent.toString());

        outContent.reset();

        String[] inputArr2 = {"CHecK", "1", "999999999"};
        productExchange.check(inputArr2);
        assertEquals("000000001는 교환 가능합니다.\r\n999999999는 존재하지않습니다.\r\n", outContent.toString());

    }

    @Test
    @DisplayName("HELP 명령어로 조회가능")
    void testHelp(){
        productExchange.help();
        assertEquals("=== SHARETREATS 사용법 안내 ===\r\n" +
                "CHECK [상품코드] : 해당 상품의 교환 가능 여부를 확인합니다.\r\n" +
                "HELP  사용법을 안내합니다.\r\n" +
                "CLAIM [상점코드] [상품코드... 상품코드] : 해당 상품을 교환합니다.\r\n" +
                "STOP 서비스를 종료합니다.\r\n",outContent.toString());
    }
    //요-8
    @Test
    @DisplayName("CLAIM 명령어로 조회가능")
    void testClaim(){
        String[] inputArr = {"CLaIM", "storeA", "000000001", "000000002"};
        productExchange.claim(inputArr);
        assertEquals("000000001가 교환되었습니다.\r\n000000002가 교환되었습니다.\r\n", outContent.toString());

        outContent.reset();
    }

    //요-4
    @Test
    @DisplayName("이미 교환한 상품 코드를 입력하면 교환이 불가능하다는 메시지가 출력된다.")
    void testCheckExchangeDuplicatedCode(){
        String[] inputArr = {"CLAIM", "storeA", "001", "002", "003"};
        productExchange.claim(inputArr);
        outContent.reset();

        String[] inputArr2 = {"CLAIM", "storeB", "1"};
        productExchange.claim(inputArr2);
        assertEquals("000000001는 이미 교환된 상품입니다.\r\n", outContent.toString());
        outContent.reset();

        String[] inputArr3 = {"ChecK", "1"};
        productExchange.check(inputArr3);
        assertEquals("000000001는 교환 불가능합니다.\r\n",outContent.toString());
    }
    //요-7,9
    @Test
    @DisplayName("유효하지 않은 입력값을 입력하면 오류 메시지가 출력된다.")
    void testCheckExchangeInvalidInput(){
        String[] inputArr = {"CHECK"};
        productExchange.check(inputArr);
        assertEquals("잘못된 입력입니다. 상품코드를 입력하세요\r\n", outContent.toString());
        outContent.reset();
        //상점 코드는 A~Z,a~z 까지의 대,소 영문자만 사용이 가능하며 6문자로 이루어져 있습니다.
        String[] inputArr1 = {"CHECK", "Store1"};
        productExchange.claim(inputArr1);
        assertEquals("올바르지 않은 상점 코드입니다.\r\n", outContent.toString());
        outContent.reset();
        String[] inputArr2 = {"CHECK", "StoreAA"};
        productExchange.claim(inputArr2);
        assertEquals("올바르지 않은 상점 코드입니다.\r\n", outContent.toString());
        outContent.reset();
    }
    //고객입력 1,2
    @Test
    @DisplayName("고객의 입력 오류")
    void invalidInput(){
        productExchange.check(new String[]{"cheCk","한글"});
        assertEquals("잘못된 입력입니다. 상품코드를 입력하세요\r\n", outContent.toString());
        outContent.reset();
        productExchange.claim(new String[]{"cheCk","1234567890123456789012345678901234567890"});
        assertEquals("잘못된 입력입니다. 상품코드를 입력하세요\r\n", outContent.toString());
        outContent.reset();
    }

}