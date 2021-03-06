package com.morgan.andy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.moc.automata.NfaToDfaConverter;
import com.morgan.andy.moc.automata.NfaToReConverter;
import com.morgan.andy.moc.automata.REToNfaConverter;
import com.morgan.andy.service.ModelService;
import com.morgan.andy.web.rest.vm.AutomatonVM;
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
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing a created model
 */
@RestController
@RequestMapping("/api")
public class ConvertResource {

    private final Logger log = LoggerFactory.getLogger(ConvertResource.class);

    @Inject
    private NfaToDfaConverter nfaToDfaConverter;

    @Inject
    private ModelService modelService;

    @Inject
    private REToNfaConverter reToNfaConverter;

    @Inject
    private NfaToReConverter nfaToReConverter;

    /**
     * POST /convert/nfa/dfa
     * Converts a given NFA to a DFA
     *
     */
    @RequestMapping(value = "/convert/nfa/dfa",
                    method = RequestMethod.POST,
                    produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<?> convertNfaToDfa(@Valid @RequestBody AutomatonVM automatonVM, HttpServletRequest request) {
        FiniteAutomaton fa = modelService.convertVmToAutomaton(automatonVM);
        FiniteAutomaton convertedAutomaton = nfaToDfaConverter.convert(fa);
        automatonVM = modelService.convertAutomatonToVm(convertedAutomaton);
        return new ResponseEntity<>(automatonVM, HttpStatus.OK);
    }

    /**
     * POST /convert/re/nfa
     * Converts a given RE to an NFA
     */
    @RequestMapping(value = "/convert/re/nfa",
                    method = RequestMethod.POST,
                    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<?> convertReToNfa(@Valid @RequestBody String regex, HttpServletRequest request) {
        FiniteAutomaton convertedAutomaton = reToNfaConverter.convert(regex);
        AutomatonVM automatonVM = modelService.convertAutomatonToVm(convertedAutomaton);
        return new ResponseEntity<>(automatonVM, HttpStatus.OK);
    }

    @RequestMapping(value = "/convert/nfa/re",
                    method = RequestMethod.POST)
    @Timed
    public ResponseEntity<?> convertNfaToRe(@Valid @RequestBody AutomatonVM automatonVM) {
        Map<String, String> retMap = new HashMap<>();
        FiniteAutomaton fa = modelService.convertVmToAutomaton(automatonVM);
        String regex = nfaToReConverter.convert(fa);
        retMap.put("regex", regex);
        return new ResponseEntity<>(retMap, HttpStatus.OK);
    }

}
