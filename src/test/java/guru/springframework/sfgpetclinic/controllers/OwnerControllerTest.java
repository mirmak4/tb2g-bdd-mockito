package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
class OwnerControllerTest {

    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController controller;

    @Mock
    BindingResult bindingResult;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
//        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture()))
//                .willAnswer(invocation -> {
        Mockito.lenient().when(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture()))
                .thenAnswer(invocation -> {
            List<Owner> owners = new ArrayList<>();

            String name = invocation.getArgument(0);

            if (name.equals("%Buck%")) {
                owners.add(new Owner(1l, "Joe", "Buck"));
                return owners;
            } else if (name.equals("%DontFindMe%")) {
                return owners;
            } else if (name.equals("%FindMe%")) {
                owners.add(new Owner(1l, "Joe", "Buck"));
                owners.add(new Owner(2l, "Joe2", "Buck2"));
                return owners;
            }

            throw new RuntimeException("Invalid Argument");
        });
    }

    @Test
    void processFindFormWildcardFound() {
        //given
        Owner owner = new Owner(1l, "Joe", "FindMe");

        //when
        String viewName = controller.processFindForm(owner, bindingResult, Mockito.mock(Model.class));

        //then
        assertThat("%FindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);
    }

    @Test
    void processFindFormWildcardStringAnnotation() {
        //given
        Owner owner = new Owner(1l, "Joe", "Buck");

        //when
        String viewName = controller.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%Buck%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);
    }


    @Test
    void processFindFormWildcardNotFound() {
        //given
        Owner owner = new Owner(1l, "Joe", "DontFindMe");

        //when
        String viewName = controller.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%DontFindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);
    }

    @Test
    void processCreationFormHasErrors() {
        //given
        Owner owner = new Owner(1l, "Jim", "Bob");
        given(bindingResult.hasErrors()).willReturn(true);

        //when
        String viewName = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(viewName).isEqualToIgnoringCase(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);
    }

    @Test
    void processCreationFormNoErrors() {
        //given
        Owner owner = new Owner(5l, "Jim", "Bob");
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any())).willReturn(owner);

        //when
        String viewName = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(viewName).isEqualToIgnoringCase(REDIRECT_OWNERS_5);
    }
}