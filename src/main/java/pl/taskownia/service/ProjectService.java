package pl.taskownia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.taskownia.model.Chat;
import pl.taskownia.model.Project;
import pl.taskownia.model.User;
import pl.taskownia.repository.ChatRepository;
import pl.taskownia.repository.ProjectRepository;
import pl.taskownia.repository.UserRepository;
import pl.taskownia.security.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String newProject(HttpServletRequest r, Project projectRequest) {
        User u = userRepository.findByUsername(jwtTokenProvider.getLogin(jwtTokenProvider.resolveToken(r)));

        projectRequest.setAuthor(u);
        projectRequest.setMaker(null);
        projectRequest.setCreated_at(new Date(System.currentTimeMillis()));
        projectRequest.setUpdated_at(new Date(System.currentTimeMillis()));
        projectRepository.save(projectRequest);
        return "Ok";
    }

    public String takeProject(HttpServletRequest r, Long projId) {
        User u = userRepository.findByUsername(jwtTokenProvider.getLogin(jwtTokenProvider.resolveToken(r)));
        Project p = projectRepository.findById(projId).orElse(null);
        if(p == null) {
            return "Project id is wrong!";
        }
        if(p.getMaker()!=null) {
            return "Project already taken!";
        }
        if(p.getAuthor().getId()==u.getId()) {
            return "Author can't take project!";
        }
        p.setMaker(u);
        projectRepository.save(p);
        return "Ok";
    }

    public List<Project> getAllProjects() { //TODO: get max 10-20-30 projects, not all
        return projectRepository.findAll();
    }

    public List<Project> getAllMyProjectsAuthor(HttpServletRequest r) {
        User u = userRepository.findByUsername(jwtTokenProvider.getLogin(jwtTokenProvider.resolveToken(r)));

        return projectRepository.findAllByAuthor(u);
    }

    public List<Project> getAllMyProjectsMaker(HttpServletRequest r) {
        User u = userRepository.findByUsername(jwtTokenProvider.getLogin(jwtTokenProvider.resolveToken(r)));

        return projectRepository.findAllByMaker(u);
    }

    public Page<Project> getLastProjects(Integer howMany) {
        Page<Project> projectPage = projectRepository.findAll(
                PageRequest.of(0, howMany, Sort.by(Sort.Direction.DESC, "id")));
        return projectPage;
    }
}
