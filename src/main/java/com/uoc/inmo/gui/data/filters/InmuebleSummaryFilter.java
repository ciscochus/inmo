package com.uoc.inmo.gui.data.filters;

import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InmuebleSummaryFilter {
    private final Integer MAX_ROOMS = 4;
    private final Integer MAX_BATHS = 3;
    
    private String address;

    private Double minPrice;
    private Double maxPrice;

    private Double minArea;
    private Double maxArea;

    private Set<String> rooms;
    private Set<String> baths;

    private String email;

    public Specification<InmuebleSummary> getSpecification(){

        return new Specification<InmuebleSummary>(){

            @Override
            public Predicate toPredicate(Root<InmuebleSummary> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                        Predicate predicate = cb.conjunction();

                        //Price

                        if(minPrice != null && minPrice.doubleValue() > 0){
                            Predicate minPricePredicate = cb.greaterThanOrEqualTo(root.get("price"), minPrice.doubleValue());
                            predicate = cb.and(predicate, minPricePredicate);
                        }

                        if(maxPrice != null && maxPrice.doubleValue() > 0){
                            Predicate maxPricePredicate = cb.lessThanOrEqualTo(root.get("price"), maxPrice.doubleValue());
                            predicate = cb.and(predicate, maxPricePredicate);
                        }

                        //Area

                        if(minArea != null && minArea.doubleValue() > 0){
                            Predicate minAreaPredicate = cb.greaterThanOrEqualTo(root.get("area"), minArea.doubleValue());
                            predicate = cb.and(predicate, minAreaPredicate);
                        }

                        if(maxArea != null && maxArea.doubleValue() > 0){
                            Predicate maxAreaPredicate = cb.lessThanOrEqualTo(root.get("area"), maxArea.doubleValue());
                            predicate = cb.and(predicate, maxAreaPredicate);
                        }

                        //Rooms

                        if(!CollectionUtils.isEmpty(rooms)){
                            Predicate roomsPredicate = getRoomsPredicate(root, cb);
                            predicate = cb.and(predicate, roomsPredicate);
                        }

                        //Baths

                        if(!CollectionUtils.isEmpty(baths)){
                            Predicate bathsPredicate = getBathsPredicate(root, cb);
                            predicate = cb.and(predicate, bathsPredicate);
                        }

                        //Email
                        if(StringUtils.hasText(email)){
                            Predicate emailNotNullPredicate = cb.isNotNull(root.get("email"));
                            Predicate emailPredicate = cb.equal(cb.upper(root.get("email")), email.toUpperCase());

                            predicate = cb.and(predicate, emailNotNullPredicate, emailPredicate);
                        }
                
                return predicate;
            }

            private Predicate getRoomsPredicate(Root<InmuebleSummary> root, CriteriaBuilder cb){
                Predicate predicate = cb.disjunction();

                if(!CollectionUtils.isEmpty(rooms)){
                    for (String roomStr : rooms) {
                        Integer numberRoom = convertRoomNumber(roomStr);
                        if(numberRoom != null){
                            if(!numberRoom.equals(MAX_ROOMS)){
                                Predicate roomsPredicate = cb.equal(root.get("rooms"), numberRoom.intValue());
                                predicate = cb.or(predicate, roomsPredicate);
                            } else {
                                Predicate roomsPredicate = cb.greaterThanOrEqualTo(root.get("rooms"), numberRoom.intValue());
                                predicate = cb.or(predicate, roomsPredicate);
                            }
                        }
                    }
                }

                return predicate;
            }

            private Predicate getBathsPredicate(Root<InmuebleSummary> root, CriteriaBuilder cb){
                Predicate predicate = cb.disjunction();

                if(!CollectionUtils.isEmpty(baths)){
                    for (String bathsStr : baths) {
                        Integer numberBath = convertBathNumber(bathsStr);
                        if(numberBath != null){
                            if(!numberBath.equals(MAX_BATHS)){
                                Predicate bathsPredicate = cb.equal(root.get("baths"), numberBath.intValue());
                                predicate = cb.or(predicate, bathsPredicate);
                            } else {
                                Predicate bathsPredicate = cb.greaterThanOrEqualTo(root.get("baths"), numberBath.intValue());
                                predicate = cb.or(predicate, bathsPredicate);
                            }
                        }
                    }
                }

                return predicate;
            }

        };
    }

    private Integer convertRoomNumber(String str){
        if(!StringUtils.hasText(str))
            return null;

        Integer rooms = null;

        try {
            rooms = Integer.parseInt(str);
        } catch (Exception e) {
            if(str.contains(MAX_ROOMS.toString()))
                return MAX_ROOMS;
        }

        return rooms;
    }

    private Integer convertBathNumber(String str){
        if(!StringUtils.hasText(str))
            return null;

        Integer rooms = null;

        try {
            rooms = Integer.parseInt(str);
        } catch (Exception e) {
            if(str.contains(MAX_BATHS.toString()))
                return MAX_BATHS;
        }

        return rooms;
    }

    public void clear(){
        this.address = null;

        this.minPrice = null;
        this.maxPrice = null;

        this.minArea = null;
        this.maxArea = null;

        this.rooms = null;
        this.baths = null;
    }
}
