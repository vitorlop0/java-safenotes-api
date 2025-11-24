package com.vitor.safenotes.service;

import com.vitor.safenotes.model.Note;
import com.vitor.safenotes.model.User;
import com.vitor.safenotes.repository.NoteRepository;
import com.vitor.safenotes.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Nota não encontrada!"));
    }


    public void deleteAllNotes() {
         noteRepository.deleteAll();
    }

    public void deleteById(long id) {
         noteRepository.deleteById(id);
    }

}
