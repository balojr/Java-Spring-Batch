package com.slime.springbatch.processor;

import com.slime.springbatch.domain.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

/**
 * This class implements the ItemProcessor interface provided by Spring Batch.
 * It processes Coffee items by transforming their properties to uppercase.
 */
@Slf4j
public class CoffeeItemProcessor implements ItemProcessor<Coffee, Coffee> {

    /**
     * This method takes a Coffee object as input, transforms its properties to uppercase,
     * and returns a new Coffee object with the transformed properties.
     *
     * @param coffee the Coffee object to be processed
     * @return a new Coffee object with its properties transformed to uppercase
     * @throws Exception if an error occurs during processing
     */
    @Override
    public Coffee process(final Coffee coffee) throws Exception {
        String brand = coffee.getBrand().toUpperCase();
        String origin = coffee.getOrigin().toUpperCase();
        String characteristics = coffee.getCharacteristics().toUpperCase();

        Coffee transformedCoffee = new Coffee(brand, origin, characteristics);
        log.info("Converting ( {} ) into ( {} )", coffee, transformedCoffee);

        return transformedCoffee;
    }
}