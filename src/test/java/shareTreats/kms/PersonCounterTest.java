package shareTreats.kms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shareTreats.kms.personcounter.PersonCounter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PersonCounterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PersonCounter personCounter;

    @BeforeEach
    public void autoInsert(){
        System.setOut(new PrintStream(outContent));
        personCounter = new PersonCounter();
    }

    //요-1번
    @Test
    @DisplayName("회사내의 각 부서는 1개 이상")
    public void departmentMoreThanOne() {
        personCounter.showInfo();
        assertEquals("회사내의 각 부서는 1개 이상입니다.", outContent.toString().trim());
    }
    //요-3번
    @Test
    @DisplayName("각 부서별 정보출력")
    public void showInfo(){
        personCounter.save("AS", "10");
        personCounter.save("DEV", "0");
        personCounter.showInfo();
        assertEquals("AS, 10\r\nDEV, 0\r\n",outContent.toString());
    }
    //요-4번
    @Test
    @DisplayName("모든 직원수 보기")
    public void testShowTotalCount() {
        personCounter.save("AS", "10");
        personCounter.save("DEV", "0");
        personCounter.save("QA", "990");
        personCounter.save("SOLO", "100");

        personCounter.connectTree("* > AS");
        personCounter.connectTree("AS > DEV");
        personCounter.connectTree("AS > QA");

        personCounter.showTotalCount();
        assertEquals("AS는 1000명 입니다.\r\nSOLO는 100명 입니다.", outContent.toString().trim());
    }


    @Test
    @DisplayName("부서 구성도")
    public void showDepartment(){
        personCounter.save("AS", "10");
        personCounter.save("DEV", "0");
        personCounter.save("QA", "990");

        personCounter.connectTree("* > AS");
        personCounter.connectTree("AS > DEV");
        personCounter.connectTree("AS > QA");
        personCounter.showDepartment();
        assertEquals("*>AS\r\nAS>DEV\r\nAS>QA\r\n",outContent.toString());
    }

    @Test
    @DisplayName("한번에 다량의 입력이 들어왔을때")
    public void testConnectTree_invalidInput() {
        PersonCounter personCounter = new PersonCounter();
        personCounter.save("AS", "10");
        personCounter.save("DEV", "5");
        personCounter.connectTree("AS, DEV");
        assertEquals("> 로 표시되않았거나, 한번에 하나씩만 입력가능합니다.", outContent.toString().trim());
    }
    //고객 2번,3번
    @Test
    @DisplayName("고객의 입력실수")
    public void smallAlphabet() {
        personCounter.save("aa","1000");
        assertEquals("대문자만 가능합니다.\r\n",outContent.toString());
        outContent.reset();

        personCounter.save("AA","1001");
        assertEquals("0~1000명만 입력가능합니다.\r\n",outContent.toString());
        outContent.reset();

        personCounter.save("AA","-1");
        assertEquals("0~1000명만 입력가능합니다.\r\n",outContent.toString());
        outContent.reset();

        personCounter.connectTree("A<B");
        assertEquals("> 로 표시되않았거나, 한번에 하나씩만 입력가능합니다.\r\n",outContent.toString());
        outContent.reset();
    }

    //5,6
    @Test
    @DisplayName("*는 오직하나이고 1개의 하위부서는 1개의 상위부서만 가질수 있다.")
    void wildCardIsOnlyOne(){
    personCounter.save("AS", "10");
    personCounter.save("DEV", "0");
    personCounter.save("QA", "990");

    personCounter.connectTree("* > AS");
    personCounter.connectTree("AS > DEV");
    personCounter.connectTree("* > QA");
    assertEquals("이미 최상위 부서는 정해져있습니다.\r\n",outContent.toString());
    outContent.reset();

    personCounter.connectTree("QA > DEV");
    assertEquals("1개의 하위 부서는 1개의 상위 부서만을 가질 수 있습니다.\r\n",outContent.toString());
    }
}