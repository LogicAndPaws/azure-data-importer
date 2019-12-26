package com.epam.azuredataimporter;


import com.epam.azuredataimporter.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("azure-data-importer")
public class ApplicationEndpoint {

    @Autowired
    ImportService importService;

    @GetMapping("/import/{workerCsv}/{locationCsv}/{companyCsv}/{modelCsv}/{phoneCsv}")
    public void test(@PathVariable String phoneCsv, @PathVariable String modelCsv, @PathVariable String workerCsv,
                     @PathVariable String locationCsv, @PathVariable String companyCsv) {
        ImportSequence sequence = new ImportSequence();
        sequence.addPare(workerCsv, Worker.class);
        sequence.addPare(locationCsv, Location.class);
        sequence.addPare(companyCsv, Company.class);
        sequence.addPare(modelCsv, Model.class);
        sequence.addPare(phoneCsv, Phone.class);
        importService.startImport(sequence);
    }

    @GetMapping("/import")
    public void blankTest() {
        ImportSequence sequence = new ImportSequence();
        sequence.addPare("worker.csv", Worker.class);
        sequence.addPare("location.csv", Location.class);
        sequence.addPare("company.csv", Company.class);
        sequence.addPare("model.csv", Model.class);
        sequence.addPare("phone.csv", Phone.class);
        importService.startImport(sequence);
    }

}
