package com.example.concur.repository;

import com.example.concur.Entity.UserDetails;
import com.example.concur.embeddable.Address;
import com.example.concur.embeddable.Geo;
import com.example.concur.embeddable.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserDetailsRepositoryTest {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Test
    public void testFindByEmail_existingEmail() {
        // Create and save a UserDetails
        Geo geo = new Geo();
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setSuite("Suite 100");
        address.setCity("City");
        address.setZipcode("12345");
        address.setGeo(geo);

        Company company = new Company();
        company.setName("Company Name");
        company.setCatchPhrase("CatchPhrase");
        company.setBs("BS");

        UserDetails userDetails = new UserDetails("test@example.com", "John", "Doe", "jdoe", address, "123-456-7890", "www.example.com", company);
        userDetailsRepository.save(userDetails);

        // Find the UserDetails by email
        Optional<UserDetails> foundUserDetails = userDetailsRepository.findByEmail("test@example.com");
        assertTrue(foundUserDetails.isPresent());
        assertEquals("test@example.com", foundUserDetails.get().getEmail());
    }

    @Test
    public void testFindByEmail_nonExistingEmail() {
        // Try to find a UserDetails by email that doesn't exist
        Optional<UserDetails> foundUserDetails = userDetailsRepository.findByEmail("nonexistent@example.com");
        assertFalse(foundUserDetails.isPresent());
    }

    @Test
    public void testSaveUserDetails() {
        // Create and save a UserDetails
        Geo geo = new Geo();
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setSuite("Suite 100");
        address.setCity("City");
        address.setZipcode("12345");
        address.setGeo(geo);

        Company company = new Company();
        company.setName("Company Name");
        company.setCatchPhrase("CatchPhrase");
        company.setBs("BS");

        UserDetails userDetails = new UserDetails("new@example.com", "Jane", "Doe", "jdoe", address, "123-456-7890", "www.example.com", company);
        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);

        // Verify the UserDetails was saved
        assertNotNull(savedUserDetails.getId());
        assertEquals("new@example.com", savedUserDetails.getEmail());
    }

    @Test
    public void testDeleteUserDetails() {
        // Create and save a UserDetails
        Geo geo = new Geo();
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setSuite("Suite 100");
        address.setCity("City");
        address.setZipcode("12345");
        address.setGeo(geo);

        Company company = new Company();
        company.setName("Company Name");
        company.setCatchPhrase("CatchPhrase");
        company.setBs("BS");

        UserDetails userDetails = new UserDetails("delete@example.com", "John", "Doe", "jdoe", address, "123-456-7890", "www.example.com", company);
        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);

        // Delete the UserDetails
        userDetailsRepository.delete(savedUserDetails);

        // Verify the UserDetails was deleted
        Optional<UserDetails> foundUserDetails = userDetailsRepository.findByEmail("delete@example.com");
        assertFalse(foundUserDetails.isPresent());
    }
}