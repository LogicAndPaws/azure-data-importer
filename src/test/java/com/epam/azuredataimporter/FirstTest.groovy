package com.epam.azuredataimporter

import com.epam.azuredataimporter.entity.Worker
import com.epam.azuredataimporter.validation.ObjectValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class FirstTest extends Specification {
    @Autowired
    ObjectValidator validator

    def "(Validator) Wrong Worker name"() {
        Worker worker = new Worker((String[]) ["11111111", "Not Valid. N. A. M. E.", "+12345678890"].toArray())
        expect:
        !validator.isValid(worker)
    }

    def "(Validator) Wrong Worker id"() {
        Worker worker = new Worker((String[]) ["1133322111", "Valid Name", "+12345678890"].toArray())
        expect:
        !validator.isValid(worker)
    }
}
