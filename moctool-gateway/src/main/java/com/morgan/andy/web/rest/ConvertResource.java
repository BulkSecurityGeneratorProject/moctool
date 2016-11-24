package com.morgan.andy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.moc.automata.NfaToDfaConverter;
import com.morgan.andy.repository.UserRepository;
import com.morgan.andy.service.MailService;
import com.morgan.andy.service.ModelService;
import com.morgan.andy.service.UserService;
import com.morgan.andy.web.rest.vm.ModelVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * REST controller for managing a created model
 */
@RestController
@RequestMapping("/api")
public class ConvertResource {

    private final Logger log = LoggerFactory.getLogger(ConvertResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;

    @Inject
    private NfaToDfaConverter nfaToDfaConverter;

    @Inject
    private ModelService modelService;

    /**
     * POST  /register : register the user.
     *
     * @param modelVM the managed user View Model
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) if the user is registered or 400 (Bad Request) if the login or e-mail is already in use
     */
    @RequestMapping(value = "/convert/nfa/dfa",
                    method = RequestMethod.POST,
                    produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<?> convertNfaToDfa(@Valid @RequestBody ModelVM modelVM, HttpServletRequest request) {
        FiniteAutomaton finiteAutomaton = modelService.vmToAutomataStructure(modelVM);
        FiniteAutomaton convertedAutomaton = nfaToDfaConverter.convert(finiteAutomaton);
        ModelVM returnAutomaton = modelService.automataStructureToVm(convertedAutomaton);
        return new ResponseEntity<>(returnAutomaton, HttpStatus.OK);
    }

    @RequestMapping(value = "/convert/dfa/nfa",
        method = RequestMethod.GET,
        produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<?> convertDfaToNfa(@Valid @RequestBody ModelVM modelVM, HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
