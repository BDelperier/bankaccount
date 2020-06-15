package com.benoit.kata.bankaccount.rest;

import com.benoit.kata.bankaccount.extensions.restassured.RestAssuredExtension;
import com.benoit.kata.bankaccount.model.ActionRequestWS;
import com.benoit.kata.bankaccount.model.OperationsResponseWS;
import com.benoit.kata.bankaccount.model.enums.ActionTypeEnum;
import com.benoit.kata.bankaccount.repository.GlobalCacheRepository;
import com.google.common.reflect.TypeToken;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@ExtendWith(RestAssuredExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankAccountTest extends IntegrationBase {

    @Autowired
    GlobalCacheRepository globalCacheRepository;

    @Test
    public void should_make_a_standard_deposit() {
        globalCacheRepository.initUser("benoit", new BigDecimal("1000"));

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .pathParam("userName", "benoit")
                .body(ActionRequestWS.builder()
                        .amount(new BigDecimal("84"))
                        .build())
        .when()
                .post("/bankaccount/deposit/{userName}")
        .then().assertThat()
                .body("balanceAfter", equalTo(1084))
                .body("operationId", notNullValue())
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_make_a_standard_withdrawal() {
        globalCacheRepository.initUser("benoit2", new BigDecimal("1000"));

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .pathParam("userName", "benoit2")
                .body(ActionRequestWS.builder()
                        .amount(new BigDecimal("84"))
                        .build())
        .when()
                .post("/bankaccount/withdrawal/{userName}")
        .then().assertThat()
                .body("balanceAfter", equalTo(916))
                .body("operationId", notNullValue())
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_get_all_operations() {
        globalCacheRepository.initUser("benoit3", new BigDecimal("1000"));

        //deposit of 84
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .pathParam("userName", "benoit3")
                .body(ActionRequestWS.builder()
                        .amount(new BigDecimal("84"))
                        .build())
        .when()
                .post("/bankaccount/deposit/{userName}");

        //withdrawal of 106
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .pathParam("userName", "benoit3")
                .body(ActionRequestWS.builder()
                        .amount(new BigDecimal("106"))
                        .build())
        .when()
                .post("/bankaccount/withdrawal/{userName}");

        List<OperationsResponseWS> body = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .pathParam("userName", "benoit3")
        .when()
                .get("/bankaccount/operations/{userName}")
        .then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().body().as(new TypeToken<List<OperationsResponseWS>>(){}.getType());


        Assertions.assertThat(body).isNotEmpty().hasSize(2);
        Assertions.assertThat(body.get(0).getActionType()).isEqualTo(ActionTypeEnum.DEPOSIT);
        Assertions.assertThat(body.get(0).getDateOperation()).isNotNull().isAfter(Instant.now().minusSeconds(60));
        Assertions.assertThat(body.get(0).getAmount()).isEqualTo(new BigDecimal("84"));
        Assertions.assertThat(body.get(0).getBalanceBefore()).isEqualTo(new BigDecimal("1000"));

        Assertions.assertThat(body.get(1).getActionType()).isEqualTo(ActionTypeEnum.WITHDRAWAL);
        Assertions.assertThat(body.get(1).getDateOperation()).isNotNull().isAfter(body.get(0).getDateOperation());
        Assertions.assertThat(body.get(1).getAmount()).isEqualTo(new BigDecimal("106"));
        Assertions.assertThat(body.get(1).getBalanceBefore()).isEqualTo(new BigDecimal("1084"));
    }
}
