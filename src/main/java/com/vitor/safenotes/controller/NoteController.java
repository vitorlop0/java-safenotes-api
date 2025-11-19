package com.vitor.safenotes.controller;

import com.vitor.safenotes.model.Note;
import com.vitor.safenotes.service.NoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public Note create(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @GetMapping
    public List<Note> listAll() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public Note getById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }
}
