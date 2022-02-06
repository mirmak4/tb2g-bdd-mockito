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
package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.fauxspring.ModelAndView;
import guru.springframework.sfgpetclinic.fauxspring.WebDataBinder;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author miron.maksymiuk
 */
@ExtendWith(MockitoExtension.class)
public class OwnerControllerTest {
    
    @InjectMocks
    private OwnerController controller;
    @Mock
    private OwnerService service;
    @Mock
    private BindingResult result;
    
    public OwnerControllerTest() {
    }
    
    @BeforeEach
    public void setUp() {
    }

    /**
     * Test of processCreationForm method, of class OwnerController.
     */
    @Test
    public void testProcessCreationFormNoErrors() {
        // given
        Owner owner = new Owner(5L, "Mati", "Maks");
        BindingResult bind = new BindingResult() {
            @Override
            public void rejectValue(String lastName, String notFound, String not_found) {
                
            }
            @Override
            public boolean hasErrors() {
                return false;
            }
        };
        when(service.save(any(Owner.class))).thenReturn(owner);
        String expected = "redirect:/owners/5";
        // when
        String created = controller.processCreationForm(owner, bind);
        // then
        assertThat(created).isEqualTo(expected);
    }
    
    @Test
    public void testProcessCreationFormNoErrorsMocked() {
        // given
        Owner owner = new Owner(5L, "Mati", "Maks");
        when(result.hasErrors()).thenReturn(Boolean.FALSE);
        when(service.save(any(Owner.class))).thenReturn(owner);
        String expected = "redirect:/owners/5";
        // when
        String created = controller.processCreationForm(owner, result);
        // then
        assertThat(created).isEqualTo(expected);
    }

    @Test
    public void testProcessCreationFormWithErrors() {
        Owner owner = new Owner(1L, "Mati", "Maks");
        BindingResult bind = new BindingResult() {
            @Override
            public void rejectValue(String lastName, String notFound, String not_found) {
                
            }
            @Override
            public boolean hasErrors() {
                return true;
            }
        };
        String expected = "owners/createOrUpdateOwnerForm";
        String created = controller.processCreationForm(owner, bind);
        Assertions.assertThat(created).isEqualTo(expected);
    }
    
    @Test
    public void testProcessCreationFormWithErrorsMocked() {
        // given
        Owner owner = new Owner(1L, "Mati", "Maks");
        when(result.hasErrors()).thenReturn(Boolean.TRUE);
        String expected = "owners/createOrUpdateOwnerForm";
        String created = controller.processCreationForm(owner, result);
        Assertions.assertThat(created).isEqualTo(expected);
    }
}
