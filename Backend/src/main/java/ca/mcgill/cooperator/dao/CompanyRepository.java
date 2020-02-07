package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Company;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Integer> {

    Company findByNameAndCityAndRegionAndCountry(
            String name, String city, String region, String country);

    List<Company> findByName(String name);
}
