package com.assignment.income;

import com.assignment.income.model.Income;
import com.assignment.income.model.SearchInput;
import com.assignment.income.model.TransactionResponse;
import com.assignment.income.model.TransactionType;
import com.assignment.income.model.UserBalanceResponse;
import com.assignment.income.repository.Repository;
import com.assignment.income.service.IncomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IncomeApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private Repository repository;

	@Autowired
	private IncomeService incomeService;

	@BeforeEach
	public void beforeEach() {
		repository.deleteIncome("123");
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void basicTest() {
		String body = this.restTemplate.getForObject("/", String.class);
		assertThat(body).contains("Not Found");
	}

	@Test
	public void testMakeTransaction() {
		final Income income = Income.builder()
		                            .accountNumber("123")
		                            .amount(100.0)
		                            .transactionType(TransactionType.ADD)
		                            .build();

		final ResponseEntity<TransactionResponse>
				response = this.restTemplate.postForEntity("/user/account", income, TransactionResponse.class);
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	}

	@Test
	public void TestGetUserBalance() {
		final Income income = Income.builder()
		                            .accountNumber("123")
		                            .amount(100.0)
		                            .transactionType(TransactionType.ADD)
		                            .build();
		final SearchInput searchInput = new SearchInput();
		searchInput.setAccountNumber("123");

		repository.addTransaction("123456", income);

		final ResponseEntity<UserBalanceResponse> response = this.restTemplate.postForEntity("/user/account/search",
				searchInput, UserBalanceResponse.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void testMakeTransactionError() {
		final Income income = Income.builder()
		                            .accountNumber("123")
		                            .amount(100.0)
		                            .transactionType(TransactionType.REMOVE)
		                            .build();

		final ResponseEntity<TransactionResponse>
				response = this.restTemplate.postForEntity("/user/account", income, TransactionResponse.class);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void TestGetUserBalanceError() {
		final SearchInput searchInput = new SearchInput();
		searchInput.setAccountNumber("123");

		final ResponseEntity<UserBalanceResponse> response = this.restTemplate.postForEntity("/user/account/search",
				searchInput, UserBalanceResponse.class);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
}
