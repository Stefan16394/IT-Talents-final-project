package com.vmzone.demo;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.vmzone.demo.repository.UserRepository;

@RunWith(MockitoJUnitRunner.Silent.class) 
@SpringBootTest
public class UserServiceTests {
	@Mock
	private UserRepository userRepository;
	
}
