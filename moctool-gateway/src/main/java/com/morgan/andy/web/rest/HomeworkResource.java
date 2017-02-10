package com.morgan.andy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.morgan.andy.domain.*;
import com.morgan.andy.repository.*;
import com.morgan.andy.web.rest.vm.HomeworkVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * REST controller for managing a created model
 */
@RestController
@RequestMapping("/api")
public class HomeworkResource {

    private final Logger log = LoggerFactory.getLogger(HomeworkResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private ClassRepository classRepository;

    @Inject
    private QuestionsRefRepository questionsRefRepository;

    @Inject
    private HomeworkRepository homeworkRepository;

    @Inject
    private HomeworkStatusRepository homeworkStatusRepository;

    @Inject
    private HomeworkQuestionsRepository homeworkQuestionsRepository;

    @RequestMapping(value = "/class/{classId}/users",
        method = RequestMethod.GET,
        produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<?> getUsersForClass(@PathVariable("classId") Long classId, HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{userId}/classes",
                    method = RequestMethod.GET)
    @Timed
    @Transactional
    public ResponseEntity<?> getClassesForUser(@PathVariable("userId") Long userId) {

        return new ResponseEntity<>(userRepository.findOne(userId).getClasses(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/instructing",
        method = RequestMethod.GET)
    @Timed
    @Transactional
    public ResponseEntity<?> getInstructingClasses(Principal principal) {
        User current = userRepository.findOneByLogin(principal.getName()).get();
        ArrayList<HWClass> classesInstructing = new ArrayList<>();
        classRepository.findAll().forEach(c -> {
            if(c.getInstructorId().equals(current.getId())) {
                classesInstructing.add(c);
            }
        });
        return new ResponseEntity<>(classesInstructing, HttpStatus.OK);
    }

    @RequestMapping(value = "/homework/questionrefs",
                    method = RequestMethod.GET)
    @Timed
    @Transactional
    public ResponseEntity<?> getPossibleQuestions() {
        return new ResponseEntity<>(questionsRefRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/homework/set",
                    method = RequestMethod.POST)
    @Timed
    @Transactional
    public ResponseEntity<?> setHomework(@RequestBody HomeworkVM homework, Principal principal) {
        Homework persistHw = new Homework();
        HWClass hwClass = classRepository.findOne(homework.getClassId());
        persistHw.setHwClass(hwClass);
        persistHw.setName(homework.getTitle());
        persistHw.setDueDate(homework.getDueDate());
        Set<HomeworkQuestions> hwQuestions = new HashSet<>();
        homework.getQuestions().forEach(q -> {
            if(q == null) {
                return;
            }
            HomeworkQuestions newQuestion = new HomeworkQuestions();
            newQuestion.setContext(q.getContext());
            newQuestion.setQuestionsRef(questionsRefRepository.findOne(q.getSelectedQuestion()));
            newQuestion.setHomework(persistHw);
            hwQuestions.add(newQuestion);
            homeworkQuestionsRepository.save(newQuestion);
        });
        persistHw.setHomeworkQuestions(hwQuestions);
        homeworkRepository.save(persistHw);

        persistHw.getHwClass().getMembers().forEach(m -> homeworkStatusRepository.save(new HomeworkStatus(m, persistHw, 0L)));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/homework/status",
                    method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<?> getHomeworkStatusForCurrentUser(Principal principal) {
        User currentUser = userRepository.findOneByLogin(principal.getName()).get();
        return new ResponseEntity<>(homeworkStatusRepository.getAllByUserId(currentUser.getId()), HttpStatus.OK);
    }

}
