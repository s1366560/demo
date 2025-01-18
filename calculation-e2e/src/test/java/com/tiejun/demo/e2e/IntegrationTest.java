package com.tiejun.demo.e2e;

import com.tiejun.demo.vo.AccountVo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.math.BigDecimal;
import java.util.Objects;

import com.tiejun.demo.domain.Account;
import com.tiejun.demo.dto.TransactionRequest;
import com.tiejun.demo.dto.TransactionResult;
import com.tiejun.demo.domain.TransactionStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = TestConfig.class
)
@ActiveProfiles("test")
public class IntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(IntegrationTest.class);

    @Container
    static DockerComposeContainer<?> environment = new DockerComposeContainer<>(
            new File("src/test/resources/docker-compose.yml"))
            .withExposedService("mysql", 3306)
            .withExposedService("redis", 6379)
            .waitingFor("mysql", Wait.forLogMessage(".*ready for connections.*", 1))
            .waitingFor("redis", Wait.forLogMessage(".*Ready to accept connections.*", 1));

    @BeforeAll
    static void setUp() {
        log.info("MySQL Host: {}, Port: {}", 
            environment.getServiceHost("mysql", 3306),
            environment.getServicePort("mysql", 3306));
        log.info("Redis Host: {}, Port: {}", 
            environment.getServiceHost("redis", 6379),
            environment.getServicePort("redis", 6379));
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", 
            () -> String.format("jdbc:mysql://%s:%d/test_db?useSSL=false&allowPublicKeyRetrieval=true",
                environment.getServiceHost("mysql", 3306),
                environment.getServicePort("mysql", 3306)));
        registry.add("spring.datasource.username", () -> "test");
        registry.add("spring.datasource.password", () -> "test");
        
        registry.add("spring.data.redis.host",
            () -> environment.getServiceHost("redis", 6379));
        registry.add("spring.data.redis.port",
            () -> environment.getServicePort("redis", 6379));
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateAndRetrieveAccount() {
        try {
            // 创建账户
            AccountVo newAccount = new AccountVo();
            newAccount.setAccountNumber("TEST001");
            newAccount.setBalance(new BigDecimal("1000.00"));

            ResponseEntity<Account> createResponse = restTemplate.postForEntity(
                    "/api/accounts",
                    newAccount,
                    Account.class
            );

            log.info("Create account response: {}", createResponse.getBody());
            
            assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(createResponse.getBody()).isNotNull();
            assertThat(createResponse.getBody().getAccountNumber()).isEqualTo("TEST001");
            assertThat(createResponse.getBody().getBalance()).isEqualByComparingTo(new BigDecimal("1000.00"));

            // 查询账户
            ResponseEntity<Account> getResponse = restTemplate.getForEntity(
                    "/api/accounts/TEST001",
                    Account.class
            );

            assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(getResponse.getBody()).isNotNull();
            assertThat(getResponse.getBody().getAccountNumber()).isEqualTo("TEST001");
        } catch (Exception e) {
            log.error("Test failed", e);
            throw e;
        }
    }

    @Test
    void shouldProcessTransaction() {
        // 创建源账户和目标账户
        AccountVo sourceAccount = new AccountVo();
        sourceAccount.setAccountNumber("SOURCE001");
        sourceAccount.setBalance(new BigDecimal("1000.00"));

        AccountVo targetAccount = new AccountVo();
        targetAccount.setAccountNumber("TARGET001");
        targetAccount.setBalance(new BigDecimal("0.00"));

        restTemplate.postForEntity("/api/accounts", sourceAccount, Account.class);
        restTemplate.postForEntity("/api/accounts", targetAccount, Account.class);

        // 执行转账交易
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountNumber("SOURCE001");
        request.setTargetAccountNumber("TARGET001");
        request.setAmount(new BigDecimal("500.00"));

        ResponseEntity<TransactionResult> transactionResponse = restTemplate.postForEntity(
                "/api/transaction",
                request,
                TransactionResult.class
        );

        assertThat(transactionResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(transactionResponse.getBody()).isNotNull();
        assertThat(transactionResponse.getBody().getTransactionStatus()).isEqualTo(TransactionStatus.SUCCESS);

        // 验证账户余额
        ResponseEntity<Account> sourceResponse = restTemplate.getForEntity(
                "/api/accounts/SOURCE001",
                Account.class
        );
        ResponseEntity<Account> targetResponse = restTemplate.getForEntity(
                "/api/accounts/TARGET001",
                Account.class
        );

        assertThat(Objects.requireNonNull(sourceResponse.getBody()).getBalance()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(Objects.requireNonNull(targetResponse.getBody()).getBalance()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void shouldHandleInvalidTransaction() {
        // 创建余额不足的账户
        Account account = new Account();
        account.setAccountNumber("INVALID001");
        account.setBalance(new BigDecimal("100.00"));
        restTemplate.postForEntity("/api/accounts", account, Account.class);

        // 尝试转账超过余额的金额
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountNumber("INVALID001");
        request.setTargetAccountNumber("TARGET001");
        request.setAmount(new BigDecimal("1000.00"));

        ResponseEntity<TransactionResult> response = restTemplate.postForEntity(
                "/api/transaction",
                request,
                TransactionResult.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getTransactionStatus()).isEqualTo(TransactionStatus.FAILED);
    }
}