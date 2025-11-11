package ru.wzrdmhm.schedule_inggu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.model.User;
import ru.wzrdmhm.schedule_inggu.model.UserState;
//import ru.wzrdmhm.schedule_inggu.repository.FacultyRepository;
//import ru.wzrdmhm.schedule_inggu.repository.GroupRepository;
//import ru.wzrdmhm.schedule_inggu.repository.InstitutionRepository;


/*@Service
public class RegistrationService {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    //@Autowired
    //private ProfileRepository profileRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    private StartRegistrationResponse startRegistrationResponse(Long telegramId) {
        User user = userService.findOrCreateUser(telegramId, "User");
        user.setState(User.UserState.CHOOSING_INSTITUTION);
        userService.saveUser(user);

        List<Institution> institutions = institutionRepository.findAll();
        return new StartRegistrationResponse(institutions);
    }

    private ChooseFacultyResponse chooseInstitution(Long telegramId, String institutionCode) {
        User user = userService.findOrCreateUser(telegramId);
        user.setTempInstitution(institutionCode);
        user.setState(UserState.CHOOSING_FACULTY);
        userService.saveUser(user);

        List<Faculty> faculties = facultyRepository.findByInstituutionCode(institutionCode);
        return new ChooseFacultyResponse(faculties);
    }

    public ChooseStudyProgramResponse chooseEducationLevel(Long telegramId, String levelCode) {
        User user = userService.getUser(telegramId);
        user.setTempEducationLevel(levelCode);
        user.setState(UserState.CHOOSING_STUDY_PROGRAM);
        userService.saveUser(user);

        List<StudyProgram> programs = studyProgramRepository.findByFacultyAndLevel(user.getTempFaculty(), levelCode);
        return new ChooseStudyProgramResponse(programs);
    }

    public ChooseCourseResponse chooseStudyProgram(Long telegramId, Long programId) {
        User user = userService.getUser(telegramId);
        user.setTempStudyProgramId(programId);
        user.setState(UserState.CHOOSING_COURSE);
        userService.saveUser(user);

        StudyProgram program = studyProgramRepository.findById(programId)
                .orElseThrow(() -> new StudyProgramNotFoundException());

        // Предлагаем курсы от 1 до длительности программы
        List<Integer> courses = IntStream.rangeClosed(1, program.getEducationLevel().getDurationYears())
                .boxed()
                .collect(Collectors.toList());

        return new ChooseCourseResponse(courses);
    }

    public ChooseGroupResponse chooseCourse(Long telegramId, Integer course) {
        User user = userService.getUser(telegramId);
        user.setTempCourse(course);
        user.setState(UserState.CHOOSING_GROUP);
        userService.saveUser(user);

        List<StudentGroup> groups = groupRepository.findByStudyProgramAndCourse(
                user.getTempStudyProgramId(), course);
        return new ChooseGroupResponse(groups);
    }

    public CompleteRegistrationResponse completeRegistration(Long telegramId, Long groupId) {
        User user = userService.getUser(telegramId);
        StudentGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException());

        user.setGroup(group);
        user.setState(UserState.REGISTERED);
        userService.saveUser(user);

        return new CompleteRegistrationResponse(group);
    }





}

 */
