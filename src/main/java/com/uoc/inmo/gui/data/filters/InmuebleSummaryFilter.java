package com.uoc.inmo.gui.data.filters;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InmuebleSummaryFilter {
    
    private String address;

    private String price;

    public Specification<InmuebleSummary> getSpecification(){

        return new Specification<InmuebleSummary>(){

            @Override
            public Predicate toPredicate(Root<InmuebleSummary> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                        Predicate predicate = cb.conjunction();

                        if(StringUtils.hasText(price) && NumberUtils.isParsable(price)){
                            Predicate pricePredicate = cb.equal(root.get("price"), Double.parseDouble(price));
                            predicate = cb.and(predicate, pricePredicate);
                        }
                        
                
                return predicate;
            }

        };
    }
}
