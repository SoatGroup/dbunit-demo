package fr.soat.icoundoul.dbunit.test;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

import fr.soat.icoundoul.dbunit.entity.Customer;
import fr.soat.icoundoul.dbunit.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class CustomerServiceTest {

	@Autowired
	private CustomerService customerService;

	@Test
	@DatabaseSetup("/sampleData.xml")
	public void testFind() throws Exception {
		List<Customer> customerList = customerService.find("hil");
		assertEquals(1, customerList.size());
		assertEquals("Phillip", customerList.get(0).getFirstName());
	}

	@Test
	@DatabaseSetup("/sampleData.xml")
	@ExpectedDatabase("/expectedData.xml")
	public void testRemove() throws Exception {
		customerService.remove(1);
	}
}
