package com.vitor.safenotes.service;

import com.vitor.safenotes.model.Note;
import com.vitor.safenotes.model.User;
import com.vitor.safenotes.repository.NoteRepository;
import com.vitor.safenotes.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NoteService {

    private NoteRepository noteRepository;
    private UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;

    }

    public User getLoggedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    public Note createNote(Note note) {
        User user = getLoggedUser();
        note.setOwner(user);
        return noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        User user = getLoggedUser();
        return noteRepository.findAllByOwnerUsername(user.getUsername());
    }

    public Note getNoteById(Long id) {

        Note note = noteRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Nota não encontrada!"));

        User user = getLoggedUser();

        if (!note.getOwner().getUsername().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ACESSO NEGADO! Esta nota não é sua.");
        }

        return note;
    }


    @Transactional
    public void deleteAllNotes() {
        User user = getLoggedUser();
         noteRepository.deleteAllByOwner(user);
    }

    @Transactional
    public void deleteById(long id) {
        Note note = getNoteById(id);
         noteRepository.delete(note);
    }

}
