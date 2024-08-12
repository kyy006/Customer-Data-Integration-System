package com.example.customersystem.Entity;

import com.example.customersystem.embeddable.Address;
import com.example.customersystem.embeddable.Geo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsTest {

    @Test
    public void testUserDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1L);

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setSuite("Apt 4");
        address.setCity("Springfield");
        address.setZipcode("12345");

        Geo geo = new Geo();
        geo.setLat("40.7128");
        geo.setLng("74.0060");
        address.setGeo(geo);

        userDetails.setAddress(address);
        userDetails.setPhone("123-456-7890");

        assertEquals(1L, userDetails.getId());
        assertEquals("123 Main St", userDetails.getAddress().getStreet());
        assertEquals("Apt 4", userDetails.getAddress().getSuite());
        assertEquals("Springfield", userDetails.getAddress().getCity());
        assertEquals("12345", userDetails.getAddress().getZipcode());
        assertEquals("40.7128", userDetails.getAddress().getGeo().getLat());
        assertEquals("74.0060", userDetails.getAddress().getGeo().getLng());
        assertEquals("123-456-7890", userDetails.getPhone());
    }
}
