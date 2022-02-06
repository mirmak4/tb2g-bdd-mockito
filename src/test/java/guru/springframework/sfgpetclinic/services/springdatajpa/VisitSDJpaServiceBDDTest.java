/*
 * Copyright 2022 Spring Framework Guru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author miron.maksymiuk
 */
@ExtendWith(MockitoExtension.class)
public class VisitSDJpaServiceBDDTest {
    
    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private VisitSDJpaService service;
    
    public VisitSDJpaServiceBDDTest() {
    }

    /**
     * Test of findAll method, of class VisitSDJpaService.
     */
    @Test
    public void testFindAll() {
        // given
        Set<Visit> visits = new HashSet<>();
        visits.add(new Visit(1L, LocalDate.now()));
        visits.add(new Visit(2L, LocalDate.now()));
        
        // when
        given(visitRepository.findAll()).willReturn(visits);
        Set<Visit> found = service.findAll();
        
        // then
        then(visitRepository).should().findAll();
        assertEquals(visits.size(), found.size());
        assertThat(found).hasSize(2);
    }

    /**
     * Test of findById method, of class VisitSDJpaService.
     */
    @Test
    public void testFindById() {
        // given
        Visit visit = new Visit(1L, LocalDate.now());
        given(visitRepository.findById(anyLong())).willReturn(Optional.of(visit));
        
        // when
        Visit found = service.findById(1L);
        
        // then
        then(visitRepository).should().findById(anyLong());
        assertNotNull(found);
        assertThat(found).isNotNull();
    }

    /**
     * Test of save method, of class VisitSDJpaService.
     */
    @Test
    public void testSave() {
        // given
        Visit visit = new Visit(1L, LocalDate.now());
        given(visitRepository.save(any(Visit.class))).willReturn(visit);
        
        // when
        Visit saved = service.save(visit);
        
        // then
        then(visitRepository).should().save(any(Visit.class));
        assertNotNull(saved);
    }
    
    @Test
    public void testSaveMultiple() {
        // given
        Visit visit = new Visit(1L, LocalDate.now());
        given(visitRepository.save(any(Visit.class))).willReturn(visit);
        
        // when
        Visit saved = service.save(visit);
        saved = service.save(visit);
        saved = service.save(visit);
        
        // then
        then(visitRepository).should(times(3)).save(any(Visit.class));
        assertNotNull(saved);
    }

    /**
     * Test of delete method, of class VisitSDJpaService.
     */
    @Test
    public void testDelete() {
        // given
        Visit visit = new Visit(1L, LocalDate.now());
        
        // when
        service.delete(visit);
        
        // then
        then(visitRepository).should().delete(any(Visit.class));
    }
    
    @Test
    public void testDeleteMultiple() {
        // given
        Visit visit = new Visit(1L, LocalDate.now());
        
        // when
        service.delete(visit);
        service.delete(visit);
        service.delete(visit);
        service.delete(visit);
        
        // then
        then(visitRepository).should(times(4)).delete(any(Visit.class));
    }

    /**
     * Test of deleteById method, of class VisitSDJpaService.
     */
    @Test
    public void testDeleteById() {
        // when
        service.deleteById(1L);
        
        // then
        then(visitRepository).should().deleteById(anyLong());
    }
    
}
