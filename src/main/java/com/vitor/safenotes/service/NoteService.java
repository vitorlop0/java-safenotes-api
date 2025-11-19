package com.vitor.safenotes.service;

import com.vitor.safenotes.model.Note;
import com.vitor.safenotes.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(Note note) {
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
