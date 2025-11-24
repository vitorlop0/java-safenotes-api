package com.vitor.safenotes.repository;

import com.vitor.safenotes.model.Note;
import com.vitor.safenotes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByOwnerUsername(String username);

    void deleteAllByOwner(User owner);
}
