package com.slime.springbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents a Coffee entity with brand, origin, and characteristics as properties.
 */
@Data
@AllArgsConstructor
public class Coffee {
    /**
     * The brand of the coffee.
     */
    private String brand;

    /**
     * The origin of the coffee.
     */
    private String origin;

    /**
     * The characteristics of the coffee.
     */
    private String characteristics;

    /**
     * Default constructor for the Coffee class.
     */
    public Coffee() {
    }
}